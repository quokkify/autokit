#!/bin/bash

info() {
  echo -e "\033[1;34mInfo: $1\033[0m"
}

warning() {
  echo -e "\033[1;33mWarning: $1\033[0m"
}

error() {
  echo -e "\033[1;31mError: $1\033[0m" >&2
}

init_compose_files() {
  COMPOSE_FILES=(-f tools/environment/docker/docker-compose.yml)
  if [[ "${CI:-}" == "true" ]]; then
    COMPOSE_FILES+=(-f tools/environment/docker/docker-compose.ci.yml)
  else
    COMPOSE_FILES+=(-f tools/environment/docker/docker-compose.local.yml)
  fi
}

compose_cmd() {
  docker compose "${COMPOSE_FILES[@]}" "$@"
}

resolve_runtime_host() {
  if [[ "${CI:-}" == "true" && "${EXECUTION_MODE:-}" == "DIND" ]]; then
    echo "dind"
  else
    echo "localhost"
  fi
}

resolve_published_port() {
  local service="$1"
  local container_port="$2"
  local fallback_port="${3:-}"
  local required="${4:-false}"
  local port_line

  port_line="$(compose_cmd port "$service" "$container_port" | head -n1 || true)"
  if [[ -n "$port_line" ]]; then
    echo "${port_line##*:}"
    return 0
  fi

  if [[ "$required" == "true" ]]; then
    return 1
  fi

  if [[ -n "$fallback_port" ]]; then
    echo "$fallback_port"
    return 0
  fi

  return 1
}

wait_until() {
  local attempts="$1"
  local interval_seconds="$2"
  shift 2

  local try
  for ((try=1; try<=attempts; try++)); do
    if "$@"; then
      return 0
    fi
    sleep "$interval_seconds"
  done

  return 1
}
