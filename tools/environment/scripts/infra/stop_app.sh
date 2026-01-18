#!/bin/bash
COMPOSE_FILES=(-f tools/environment/docker/docker-compose.yml)
if [[ "${CI:-}" == "true" ]]; then
  COMPOSE_FILES+=(-f tools/environment/docker/docker-compose.ci.yml)
fi

DOWN_ARGS=(down)
if [[ "${CI:-}" == "true" ]]; then
  DOWN_ARGS+=( -v )
fi

docker compose \
  "${COMPOSE_FILES[@]}" \
  "${DOWN_ARGS[@]}"
