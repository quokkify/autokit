#!/bin/bash
set -euo pipefail

source "$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/../compose_utils.sh"
init_compose_files

info "[infra] web hook: start selenium grid"
./tools/environment/scripts/infra/modules/selenium/run_selenium_grid.sh

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
