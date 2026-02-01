#!/bin/bash

echo "🚀 Starting MockServer container..."
COMPOSE_FILES=(-f tools/environment/docker/docker-compose.yml)
if [[ "${CI:-}" == "true" ]]; then
  COMPOSE_FILES+=(-f tools/environment/docker/docker-compose.ci.yml)
else
  COMPOSE_FILES+=(-f tools/environment/docker/docker-compose.local.yml)
fi

docker compose \
  "${COMPOSE_FILES[@]}" \
  up -d --quiet-pull mock-server

sleep=2
max_count=30
count=0
status=0

while true; do
  if [[ "${CI:-}" == "true" ]]; then
    container_id="$(docker compose "${COMPOSE_FILES[@]}" ps -q mock-server | head -n1 || true)"
    if [[ -n "$container_id" ]]; then
      status=$(docker run --rm --network "container:${container_id}" curlimages/curl:8.5.0 \
        -o /dev/null -s -w "%{http_code}" http://localhost:1080/mockserver/dashboard || true)
    else
      status=000
    fi
  else
    status=$(curl -o /dev/null -s -w "%{http_code}" http://localhost:1080/mockserver/dashboard)
  fi

  if [[ "$status" -eq 200 ]]; then
    echo "✅ MockServer container started successfully"
    break
  fi

  echo "⏳ Waiting for MockServer... ${count}s elapsed, status code: $status"
  sleep "$sleep"
  count=$((count + sleep))

  if [[ "$count" -gt "$max_count" ]]; then
    echo -e "\e[1;31m❌ MockServer did not start within ${max_count}s (http://localhost:1080)\e[0m"
    exit 1
  fi
done

get_mockserver_base_url() {
  local host="localhost"
  if [[ "${CI:-}" == "true" ]]; then
    host="dind"
  fi
  local port_line
  port_line=$(docker compose "${COMPOSE_FILES[@]}" port mock-server 1080 | head -n1 || true)
  if [[ -n "$port_line" ]]; then
    local port="${port_line##*:}"
    echo "http://${host}:${port}"
  else
    echo "http://${host}:1080"
  fi
}

MOCKSERVER_BASE_URL="$(get_mockserver_base_url)"
echo "BASE_API_URL=${MOCKSERVER_BASE_URL}" > tools/environment/.mock-server.env
