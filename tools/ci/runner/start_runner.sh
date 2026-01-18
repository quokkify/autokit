#!/bin/bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

if [[ ! -f "${SCRIPT_DIR}/.env" ]]; then
  echo "[runner] Missing .env. Copy .env.example to .env and fill values."
  exit 1
fi

docker compose \
  -f "${SCRIPT_DIR}/docker-compose.runner.yml" \
  --env-file "${SCRIPT_DIR}/.env" \
  up -d
