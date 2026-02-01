#!/bin/bash
set -euo pipefail

if [[ "${CI:-}" == "true" ]]; then
  export GRADLE_OPTS="-Dorg.gradle.console=plain"
else
  export GRADLE_OPTS="-Dorg.gradle.console=rich"
fi

# Blue
info() {
  echo -e "\033[1;34mInfo: $1\033[0m"
}

# Yellow
warning() {
  echo -e "\033[1;33mWarning: $1\033[0m"
}

# Red
error() {
  echo -e "\033[1;31mError: $1\033[0m"
}

PROFILE="${1:-none}"

if [[ "$PROFILE" == "none" || -z "$PROFILE" ]]; then
  echo "[infra] profile=none -> nothing to start"
  exit 0
fi

PROFILES_ARGS=()
IFS=',' read -ra TOKENS <<<"$PROFILE"
for profile in "${TOKENS[@]}"; do
  profile="$(echo "$profile" | xargs)" # trim
  [[ -n "$profile" ]] && PROFILES_ARGS+=("--profile" "$profile")
done

echo "[infra] docker compose up: ${PROFILES_ARGS[*]}"
COMPOSE_FILES=(-f tools/environment/docker/docker-compose.yml)
if [[ "${CI:-}" == "true" ]]; then
  COMPOSE_FILES+=(-f tools/environment/docker/docker-compose.ci.yml)
else
  COMPOSE_FILES+=(-f tools/environment/docker/docker-compose.local.yml)
fi

if [[ "${CI:-}" == "true" ]]; then
  for profile in "${TOKENS[@]}"; do
    profile="$(echo "$profile" | xargs)"
    if [[ "$profile" == "web" ]]; then
      echo "[infra] building nginx image for CI"
      docker compose "${COMPOSE_FILES[@]}" build nginx
      break
    fi
  done
fi

docker compose \
  "${COMPOSE_FILES[@]}" \
  "${PROFILES_ARGS[@]}" up -d

for profile in "${TOKENS[@]}"; do
  profile="$(echo "$profile" | xargs)"
  case "$profile" in
    mock)
      info "[infra] mock hook: upload expectations"
      ./tools/environment/scripts/mock/run_mock_server.sh
      ./tools/environment/scripts/mock/upload_expectations.sh
      ;;
    web)
      info "[infra] web hook: start selenium grid"
      ./tools/environment/scripts/selenium/run_selenium_grid.sh
      info "[infra] web hook: set nginx url"
      COMPOSE_FILES=(-f tools/environment/docker/docker-compose.yml)
      if [[ "${CI:-}" == "true" ]]; then
        COMPOSE_FILES+=(-f tools/environment/docker/docker-compose.ci.yml)
      else
        COMPOSE_FILES+=(-f tools/environment/docker/docker-compose.local.yml)
      fi
      port_line=$(docker compose "${COMPOSE_FILES[@]}" port nginx 80 | head -n1 || true)
      if [[ "${CI:-}" == "true" ]]; then
        host="nginx"
        port="80"
      else
        host="localhost"
        if [[ -n "$port_line" ]]; then
          port="${port_line##*:}"
        else
          port="80"
        fi
      fi
      echo "NGINX_BASE_URL=http://${host}:${port}" > tools/environment/.nginx.env
      ;;
    storage)
      info "[infra] storage hook: set mongo url"
      COMPOSE_FILES=(-f tools/environment/docker/docker-compose.yml)
      if [[ "${CI:-}" == "true" ]]; then
        COMPOSE_FILES+=(-f tools/environment/docker/docker-compose.ci.yml)
      else
        COMPOSE_FILES+=(-f tools/environment/docker/docker-compose.local.yml)
      fi
      port_line=$(docker compose "${COMPOSE_FILES[@]}" port mongodb 27017 | head -n1 || true)
      if [[ -n "$port_line" ]]; then
        port="${port_line##*:}"
      else
        port="27017"
      fi
      host="localhost"
      if [[ "${CI:-}" == "true" ]]; then
        host="dind"
      fi
      echo "MONGODB_URL=mongodb://${host}:${port}" > tools/environment/.mongo.env
      ;;
    *)
      : # no-op
      ;;
  esac
done
