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
      ;;
    *)
      : # no-op
      ;;
  esac
done
