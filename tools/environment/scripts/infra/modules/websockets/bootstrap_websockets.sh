#!/bin/bash
set -euo pipefail

source "$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/../../compose_utils.sh"
init_compose_files

centrifugo_api_key="${CENTRIFUGO_API_KEY:-api_key}"

require_port="false"
if [[ "${CI:-}" == "true" ]]; then
  require_port="true"
fi

centrifugo_port="$(resolve_published_port centrifugo 8000 8005 "${require_port}" || true)"

if [[ -z "${centrifugo_port}" ]]; then
  error "[websockets] cannot resolve exposed port for centrifugo service"
  exit 1
fi

centrifugo_host="$(normalize_runtime_host "$(select_runtime_host_for_port "$(resolve_runtime_host)" "${centrifugo_port}")")"

if ! wait_until 30 1 is_tcp_reachable "${centrifugo_host}" "${centrifugo_port}"; then
  error "[websockets] centrifugo endpoint is not reachable on ${centrifugo_host}:${centrifugo_port}"
  compose_cmd logs --tail=200 centrifugo || true
  exit 1
fi

cat > tools/environment/.websockets.env <<ENV
CENTRIFUGO_HOST=${centrifugo_host}
CENTRIFUGO_PORT=${centrifugo_port}
CENTRIFUGO_API_KEY=${centrifugo_api_key}
CENTRIFUGO_URL=ws://${centrifugo_host}:${centrifugo_port}
CENTRIFUGO_HTTP_URL=http://${centrifugo_host}:${centrifugo_port}
ENV

mkdir -p integrations/websockets/src/test/resources/local_resources
cat > integrations/websockets/src/test/resources/local_resources/websockets.properties <<ENV
CENTRIFUGO_HOST=${centrifugo_host}
CENTRIFUGO_PORT=${centrifugo_port}
CENTRIFUGO_API_KEY=${centrifugo_api_key}
ENV

info "[websockets] centrifugo endpoint: ${centrifugo_host}:${centrifugo_port}"
info "[websockets] env file written: tools/environment/.websockets.env"
info "[websockets] owner config written: integrations/websockets/src/test/resources/local_resources/websockets.properties"
