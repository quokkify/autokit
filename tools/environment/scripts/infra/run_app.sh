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
  needs_nginx_build="false"
  for profile in "${TOKENS[@]}"; do
    profile="$(echo "$profile" | xargs)"
    if [[ "$profile" == "storage" ]]; then
      export MONGODB_PUBLISHED_PORT=0
    fi
    if [[ "$profile" == "redis" ]]; then
      export REDIS_PUBLISHED_PORT=0
    fi
    if [[ "$profile" == "web" ]]; then
      needs_nginx_build="true"
    fi
  done
  if [[ "$needs_nginx_build" == "true" ]]; then
    echo "[infra] building nginx image for CI"
    docker compose "${COMPOSE_FILES[@]}" build nginx
  fi
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
      if [[ "${CI:-}" == "true" && "${EXECUTION_MODE:-}" == "DIND" ]]; then
        host="dind"
      fi
      echo "MONGODB_URL=mongodb://${host}:${port}" > tools/environment/.mongo.env
      ;;
    redis)
      info "[infra] redis hook: waiting for PING"
      ready="false"
      for _ in {1..30}; do
        if docker compose "${COMPOSE_FILES[@]}" exec -T redis redis-cli ping >/dev/null 2>&1; then
          ready="true"
          break
        fi
        sleep 1
      done
      if [[ "$ready" != "true" ]]; then
        warning "[infra] redis hook: PING not ready after timeout"
      fi
      info "[infra] redis hook: set redis host and port"
      port_line=$(docker compose "${COMPOSE_FILES[@]}" port redis 6379 | head -n1 || true)
      if [[ -n "$port_line" ]]; then
        port="${port_line##*:}"
      else
        port="6379"
      fi
      host="localhost"
      if [[ "${CI:-}" == "true" && "${EXECUTION_MODE:-}" == "DIND" ]]; then
        host="dind"
      fi
      echo "REDIS_HOST=${host}" > tools/environment/.redis.env
      echo "REDIS_PORT=${port}" >> tools/environment/.redis.env
      ;;
    reporting)
      info "[infra] reporting hook: bootstrap report portal environment"
      ./tools/environment/scripts/reportportal/bootstrap_reportportal.sh
      ;;
    messaging)
      info "[infra] messaging hook: waiting for kafka readiness"
      ready="false"
      for _ in {1..40}; do
        if docker compose "${COMPOSE_FILES[@]}" exec -T kafka \
          kafka-topics.sh --bootstrap-server kafka:9092 --list >/dev/null 2>&1; then
          ready="true"
          break
        fi
        sleep 1
      done
      if [[ "$ready" != "true" ]]; then
        warning "[infra] messaging hook: kafka not ready after timeout"
      fi

      info "[infra] messaging hook: set kafka bootstrap and kafka-ui url"
      kafka_port_line=$(docker compose "${COMPOSE_FILES[@]}" port kafka 29092 | head -n1 || true)
      kafka_ui_port_line=$(docker compose "${COMPOSE_FILES[@]}" port kafka-ui 8080 | head -n1 || true)
      if [[ -n "$kafka_port_line" ]]; then
        kafka_port="${kafka_port_line##*:}"
      else
        kafka_port="29092"
      fi
      if [[ -n "$kafka_ui_port_line" ]]; then
        kafka_ui_port="${kafka_ui_port_line##*:}"
      else
        kafka_ui_port="8086"
      fi

      host="localhost"
      if [[ "${CI:-}" == "true" && "${EXECUTION_MODE:-}" == "DIND" ]]; then
        host="dind"
      fi
      echo "KAFKA_BOOTSTRAP_SERVERS=${host}:${kafka_port}" > tools/environment/.kafka.env
      echo "KAFKA_SERVER_ADDRESS=${host}:${kafka_port}" >> tools/environment/.kafka.env
      echo "KAFKA_UI_URL=http://${host}:${kafka_ui_port}" >> tools/environment/.kafka.env
      ;;
    *)
      : # no-op
      ;;
  esac
done
