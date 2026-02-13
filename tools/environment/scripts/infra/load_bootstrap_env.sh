#!/bin/bash
set -euo pipefail

ENV_FILES=(
  tools/environment/.selenium-grid.env
  tools/environment/.mock-server.env
  tools/environment/.mongo.env
  tools/environment/.redis.env
  tools/environment/.nginx.env
  tools/environment/.reportportal.env
  tools/environment/.kafka.env
  tools/environment/.rabbitmq.env
  tools/environment/.websockets.env
)

for env_file in "${ENV_FILES[@]}"; do
  if [[ -f "${env_file}" ]]; then
    set -a
    # shellcheck disable=SC1090
    source "${env_file}"
    set +a
  fi
done
