#!/bin/bash
set -euo pipefail

source "$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/../compose_utils.sh"
init_compose_files

info "[infra] messaging hook: waiting for kafka readiness"
if ! wait_until 90 2 compose_cmd exec -T kafka \
  sh -lc "/opt/kafka/bin/kafka-topics.sh --bootstrap-server kafka:9092 --list 2>/dev/null | grep -Fxq messages"; then
  error "[infra] messaging hook: kafka is not ready after timeout"
  compose_cmd logs --tail=200 kafka zookeeper || true
  exit 1
fi

info "[infra] messaging hook: set kafka bootstrap and kafka-ui url"
require_port="false"
if [[ "${CI:-}" == "true" ]]; then
  require_port="true"
fi

if ! kafka_port="$(resolve_published_port kafka 29092 29092 "${require_port}")"; then
  error "[infra] messaging hook: cannot resolve kafka port"
  exit 1
fi
if ! kafka_ui_port="$(resolve_published_port kafka-ui 8080 8086 "${require_port}")"; then
  error "[infra] messaging hook: cannot resolve kafka-ui port"
  exit 1
fi

host="$(select_runtime_host_for_port "$(resolve_runtime_host)" "${kafka_port}")"

info "[infra] messaging hook: waiting for external kafka endpoint ${host}:${kafka_port}"
if ! wait_until 60 2 is_tcp_reachable "${host}" "${kafka_port}"; then
  error "[infra] messaging hook: external kafka endpoint is not reachable: ${host}:${kafka_port}"
  compose_cmd logs --tail=200 kafka zookeeper || true
  exit 1
fi

echo "KAFKA_BOOTSTRAP_SERVERS=${host}:${kafka_port}" > tools/environment/.kafka.env
echo "KAFKA_SERVER_ADDRESS=${host}:${kafka_port}" >> tools/environment/.kafka.env
echo "KAFKA_UI_URL=http://${host}:${kafka_ui_port}" >> tools/environment/.kafka.env
