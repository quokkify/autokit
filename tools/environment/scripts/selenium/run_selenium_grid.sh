#!/bin/bash
set -euo pipefail

COMPOSE_FILES=(-f tools/environment/docker/docker-compose.yml)
if [[ "${CI:-}" == "true" ]]; then
  COMPOSE_FILES+=(-f tools/environment/docker/docker-compose.ci.yml)
fi

extract_grid_image() {
  local line
  line="$(grep -E '^[[:space:]]*configs[[:space:]]*=' tools/environment/selenium-grid/config.toml | head -n1 || true)"
  if [[ -z "$line" ]]; then
    return
  fi
  echo "$line" \
    | sed -E 's/.*\[[[:space:]]*"([^"]+)".*/\1/' \
    | grep -E '.+/.+:.+' || true
}

GRID_IMAGE="$(extract_grid_image)"
if [[ -n "$GRID_IMAGE" ]]; then
  echo "[selenium-grid] pulling grid image: ${GRID_IMAGE}"
  docker pull "$GRID_IMAGE"
fi

echo "[selenium-grid] starting selenium hub + node"
docker compose "${COMPOSE_FILES[@]}" up -d selenium-hub selenium-node-docker

get_hub_url() {
  local host="localhost"
  if [[ "${CI:-}" == "true" ]]; then
    host="dind"
  fi
  local port_line
  port_line=$(docker compose "${COMPOSE_FILES[@]}" port selenium-hub 4444 | head -n1 || true)
  if [[ -n "$port_line" ]]; then
    local port="${port_line##*:}"
    echo "http://${host}:${port}/wd/hub"
  else
    echo "http://${host}:4444/wd/hub"
  fi
}

HUB_URL="$(get_hub_url)"
STATUS_URL="${HUB_URL}/status"

wait_interval_in_seconds=1
max_wait_time_in_seconds=30
end_time=$((SECONDS + max_wait_time_in_seconds))
time_left=$max_wait_time_in_seconds

echo "[selenium-grid] waiting for ${STATUS_URL}"
while [ $SECONDS -lt $end_time ]; do
  if [[ "${CI:-}" == "true" ]]; then
    hub_container="$(docker compose "${COMPOSE_FILES[@]}" ps -q selenium-hub | head -n1 || true)"
    if [[ -n "$hub_container" ]]; then
      response="$(docker run --rm --network "container:${hub_container}" curlimages/curl:8.5.0 -sL "$STATUS_URL" || true)"
    else
      response=""
    fi
  else
    response="$(curl -sL "$STATUS_URL" || true)"
  fi
  if echo "$response" | tr -d '\n ' | grep -q '"ready":true'; then
    echo "[selenium-grid] ready"
    break
  else
    echo "[selenium-grid] not ready yet, ${time_left}s left"
    sleep "$wait_interval_in_seconds"
    time_left=$((time_left - wait_interval_in_seconds))
  fi
done

if [ $SECONDS -ge $end_time ]; then
  echo "[selenium-grid] timeout after ${max_wait_time_in_seconds}s"
  exit 1
fi

echo "BROWSER_REMOTE_URL=${HUB_URL}" > tools/environment/.selenium-grid.env
