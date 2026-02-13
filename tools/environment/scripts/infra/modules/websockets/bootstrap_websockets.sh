#!/bin/bash
set -euo pipefail

source "$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/../../compose_utils.sh"
init_compose_files

websockets_secret="${WEBSOCKETS_SERVER_SECRET:-6eca2493-8f27-4940-aa9e-df7a87b053c9}"
centrifugo_api_key="${CENTRIFUGO_API_KEY:-api_key}"

require_port="false"
if [[ "${CI:-}" == "true" ]]; then
  require_port="true"
fi

legacy_port="$(resolve_published_port centrifugo-legacy 8001 8001 "${require_port}" || true)"
centrifugo_port="$(resolve_published_port centrifugo 8000 8005 "${require_port}" || true)"

if [[ -z "${legacy_port}" || -z "${centrifugo_port}" ]]; then
  error "[websockets] cannot resolve exposed ports for websocket services"
  exit 1
fi

legacy_host="$(select_runtime_host_for_port "$(resolve_runtime_host)" "${legacy_port}")"
centrifugo_host="$(select_runtime_host_for_port "$(resolve_runtime_host)" "${centrifugo_port}")"

if ! wait_until 30 1 is_tcp_reachable "${legacy_host}" "${legacy_port}"; then
  error "[websockets] legacy websocket endpoint is not reachable on ${legacy_host}:${legacy_port}"
  compose_cmd logs --tail=200 centrifugo-legacy || true
  exit 1
fi

if ! wait_until 30 1 is_tcp_reachable "${centrifugo_host}" "${centrifugo_port}"; then
  error "[websockets] centrifugo endpoint is not reachable on ${centrifugo_host}:${centrifugo_port}"
  compose_cmd logs --tail=200 centrifugo || true
  exit 1
fi

cat > tools/environment/.websockets.env <<ENV
WEBSOCKETS_HOST=${legacy_host}
WEBSOCKETS_PORT=${legacy_port}
WEBSOCKETS_SERVER_SECRET=${websockets_secret}
WEBSOCKETS_URL=ws://${legacy_host}:${legacy_port}
WEBSOCKETS_HTTP_URL=http://${legacy_host}:${legacy_port}

CENTRIFUGO_HOST=${centrifugo_host}
CENTRIFUGO_PORT=${centrifugo_port}
CENTRIFUGO_API_KEY=${centrifugo_api_key}
CENTRIFUGO_URL=ws://${centrifugo_host}:${centrifugo_port}
CENTRIFUGO_HTTP_URL=http://${centrifugo_host}:${centrifugo_port}
ENV

mkdir -p integrations/websockets/src/test/resources/local_resources
cat > integrations/websockets/src/test/resources/local_resources/websockets.properties <<ENV
WEBSOCKETS_HOST=${legacy_host}
WEBSOCKETS_PORT=${legacy_port}
WEBSOCKETS_SERVER_SECRET=${websockets_secret}

CENTRIFUGO_HOST=${centrifugo_host}
CENTRIFUGO_PORT=${centrifugo_port}
CENTRIFUGO_API_KEY=${centrifugo_api_key}
ENV

info "[websockets] legacy endpoint: ${legacy_host}:${legacy_port}"
info "[websockets] centrifugo endpoint: ${centrifugo_host}:${centrifugo_port}"
info "[websockets] env file written: tools/environment/.websockets.env"
info "[websockets] owner config written: integrations/websockets/src/test/resources/local_resources/websockets.properties"
