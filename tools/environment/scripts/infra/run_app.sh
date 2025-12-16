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
docker compose \
  -f tools/environment/docker/docker-compose.yml \
  "${PROFILES_ARGS[@]}" up -d

for profile in "${TOKENS[@]}"; do
  profile="$(echo "$profile" | xargs)"
  case "$profile" in
    mock)
      info "[infra] mock hook: upload expectations"
      ./tools/environment/scripts/mock/run_mock_server.sh
      ./tools/environment/scripts/mock/upload_expectations.sh
      ;;
    *)
      : # no-op
      ;;
  esac
done
