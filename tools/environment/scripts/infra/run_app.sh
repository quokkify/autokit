#!/bin/bash
set -euo pipefail

source "$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/compose_utils.sh"

if [[ "${CI:-}" == "true" ]]; then
  export GRADLE_OPTS="-Dorg.gradle.console=plain"
else
  export GRADLE_OPTS="-Dorg.gradle.console=rich"
fi

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
  # Run in current shell so exported dynamic ports are visible to docker compose interpolation.
  source ./tools/environment/scripts/infra/hooks/pre_up_ci.sh "${TOKENS[@]}"
fi

compose_cmd "${PROFILES_ARGS[@]}" up -d

for profile in "${TOKENS[@]}"; do
  profile="$(echo "$profile" | xargs)"
  case "$profile" in
    mock)
      ./tools/environment/scripts/infra/hooks/mock.sh
      ;;
    web)
      ./tools/environment/scripts/infra/hooks/web.sh
      ;;
    storage)
      ./tools/environment/scripts/infra/hooks/storage.sh
      ;;
    redis)
      ./tools/environment/scripts/infra/hooks/redis.sh
      ;;
    reporting)
      ./tools/environment/scripts/infra/hooks/reporting.sh
      ;;
    messaging)
      ./tools/environment/scripts/infra/hooks/messaging.sh
      ;;
    rabbitmq)
      ./tools/environment/scripts/infra/hooks/rabbitmq.sh
      ;;
    websockets|realtime)
      ./tools/environment/scripts/infra/hooks/websockets.sh
      ;;
    *)
      warning "[infra] unknown profile hook skipped: ${profile}"
      ;;
  esac
done
