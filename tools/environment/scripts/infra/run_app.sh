#!/bin/bash
set -euo pipefail

source "$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/compose_utils.sh"

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

find_free_port() {
  local port
  for _ in {1..100}; do
    port=$(( (RANDOM % 20000) + 20000 ))
    if ! (echo >/dev/tcp/127.0.0.1/"${port}") >/dev/null 2>&1; then
      echo "${port}"
      return 0
    fi
  done
  return 1
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

init_compose_files

echo "[infra] docker compose up: ${PROFILES_ARGS[*]}"
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
    if [[ "$profile" == "messaging" ]]; then
      export KAFKA_EXTERNAL_HOST="$(resolve_runtime_host)"
      if [[ -z "${KAFKA_PUBLISHED_PORT:-}" || "${KAFKA_PUBLISHED_PORT:-}" == "0" ]]; then
        kafka_dynamic_port="$(find_free_port || true)"
        if [[ -z "${kafka_dynamic_port}" ]]; then
          error "[infra] messaging hook: cannot allocate free host port for kafka"
          exit 1
        fi
        export KAFKA_PUBLISHED_PORT="${kafka_dynamic_port}"
      fi
      export KAFKA_EXTERNAL_PORT="${KAFKA_PUBLISHED_PORT}"
    fi
    if [[ "$profile" == "rabbitmq" ]]; then
      if [[ -z "${RABBITMQ_PUBLISHED_PORT:-}" || "${RABBITMQ_PUBLISHED_PORT:-}" == "0" ]]; then
        rabbit_dynamic_port="$(find_free_port || true)"
        if [[ -z "${rabbit_dynamic_port}" ]]; then
          error "[infra] rabbitmq hook: cannot allocate free host port for rabbitmq amqp"
          exit 1
        fi
        export RABBITMQ_PUBLISHED_PORT="${rabbit_dynamic_port}"
      fi
      if [[ -z "${RABBITMQ_MANAGEMENT_PUBLISHED_PORT:-}" || "${RABBITMQ_MANAGEMENT_PUBLISHED_PORT:-}" == "0" ]]; then
        rabbit_mgmt_dynamic_port="$(find_free_port || true)"
        if [[ -z "${rabbit_mgmt_dynamic_port}" ]]; then
          error "[infra] rabbitmq hook: cannot allocate free host port for rabbitmq management"
          exit 1
        fi
        export RABBITMQ_MANAGEMENT_PUBLISHED_PORT="${rabbit_mgmt_dynamic_port}"
      fi
    fi
  done
  if [[ "$needs_nginx_build" == "true" ]]; then
    echo "[infra] building nginx image for CI"
    compose_cmd build nginx
  fi
fi

compose_cmd "${PROFILES_ARGS[@]}" up -d

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
      if [[ "${CI:-}" == "true" ]]; then
        host="nginx"
        port="80"
      else
        host="localhost"
        if ! port="$(resolve_published_port nginx 80 80 false)"; then
          error "[infra] web hook: cannot resolve nginx port"
          exit 1
        fi
      fi
      echo "NGINX_BASE_URL=http://${host}:${port}" > tools/environment/.nginx.env
      ;;
    storage)
      info "[infra] storage hook: set mongo url"
      require_port="false"
      if [[ "${CI:-}" == "true" ]]; then
        require_port="true"
      fi
      if ! port="$(resolve_published_port mongodb 27017 27017 "$require_port")"; then
        error "[infra] storage hook: cannot resolve mongodb port"
        exit 1
      fi
      host="$(resolve_runtime_host)"
      echo "MONGODB_URL=mongodb://${host}:${port}" > tools/environment/.mongo.env
      ;;
    redis)
      info "[infra] redis hook: waiting for PING"
      ready="false"
      for _ in {1..30}; do
        if compose_cmd exec -T redis redis-cli ping >/dev/null 2>&1; then
          ready="true"
          break
        fi
        sleep 1
      done
      if [[ "$ready" != "true" ]]; then
        warning "[infra] redis hook: PING not ready after timeout"
      fi
      info "[infra] redis hook: set redis host and port"
      require_port="false"
      if [[ "${CI:-}" == "true" ]]; then
        require_port="true"
      fi
      if ! port="$(resolve_published_port redis 6379 6379 "$require_port")"; then
        error "[infra] redis hook: cannot resolve redis port"
        exit 1
      fi
      host="$(resolve_runtime_host)"
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
      for _ in {1..90}; do
        if compose_cmd exec -T kafka \
          kafka-topics.sh --bootstrap-server kafka:9092 --list 2>/dev/null | grep -Fxq messages; then
          ready="true"
          break
        fi
        sleep 2
      done
      if [[ "$ready" != "true" ]]; then
        error "[infra] messaging hook: kafka is not ready after timeout"
        compose_cmd logs --tail=200 kafka zookeeper || true
        exit 1
      fi

      info "[infra] messaging hook: set kafka bootstrap and kafka-ui url"
      require_port="false"
      if [[ "${CI:-}" == "true" ]]; then
        require_port="true"
      fi
      if ! kafka_port="$(resolve_published_port kafka 29092 29092 "$require_port")"; then
        error "[infra] messaging hook: cannot resolve kafka port"
        exit 1
      fi
      if ! kafka_ui_port="$(resolve_published_port kafka-ui 8080 8086 "$require_port")"; then
        error "[infra] messaging hook: cannot resolve kafka-ui port"
        exit 1
      fi

      host="$(resolve_runtime_host)"
      echo "KAFKA_BOOTSTRAP_SERVERS=${host}:${kafka_port}" > tools/environment/.kafka.env
      echo "KAFKA_SERVER_ADDRESS=${host}:${kafka_port}" >> tools/environment/.kafka.env
      echo "KAFKA_UI_URL=http://${host}:${kafka_ui_port}" >> tools/environment/.kafka.env
      ;;
    rabbitmq)
      info "[infra] rabbitmq hook: bootstrap rabbitmq environment"
      ./tools/environment/scripts/rabbitmq/bootstrap_rabbitmq.sh
      ;;
    *)
      : # no-op
      ;;
  esac
done
