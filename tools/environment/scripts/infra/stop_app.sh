#!/bin/bash
set -euo pipefail
COMPOSE_FILES=(-f tools/environment/docker/docker-compose.yml)
if [[ "${CI:-}" == "true" ]]; then
  COMPOSE_FILES+=(-f tools/environment/docker/docker-compose.ci.yml)
fi

PROFILE="${1:-all}"

PROFILES_ARGS=()
if [[ "$PROFILE" != "none" && -n "$PROFILE" ]]; then
  if [[ "$PROFILE" == "all" ]]; then
    PROFILES_ARGS=(--profile web --profile messaging --profile mock --profile realtime --profile storage)
  else
    IFS=',' read -ra TOKENS <<<"$PROFILE"
    for profile in "${TOKENS[@]}"; do
      profile="$(echo "$profile" | xargs)" # trim
      [[ -n "$profile" ]] && PROFILES_ARGS+=("--profile" "$profile")
    done
  fi
fi

DOWN_ARGS=(down)
if [[ "${CI:-}" == "true" ]]; then
  DOWN_ARGS+=( -v )
fi

docker compose \
  "${COMPOSE_FILES[@]}" \
  "${PROFILES_ARGS[@]}" \
  "${DOWN_ARGS[@]}"
