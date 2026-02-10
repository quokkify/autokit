#!/bin/bash
set -euo pipefail

COMPOSE_FILES=(-f tools/environment/docker/docker-compose.yml)
if [[ "${CI:-}" == "true" ]]; then
  COMPOSE_FILES+=(-f tools/environment/docker/docker-compose.ci.yml)
else
  COMPOSE_FILES+=(-f tools/environment/docker/docker-compose.local.yml)
fi

service_name="reportportal-gateway"
admin_user="${REPORTPORTAL_ADMIN_USER:-superadmin}"
admin_password="${REPORTPORTAL_ADMIN_PASSWORD:-erebus}"

port_line="$(docker compose "${COMPOSE_FILES[@]}" port "${service_name}" 8080 | head -n1 || true)"
if [[ -z "$port_line" ]]; then
  echo "[reporting] cannot resolve exposed port for ${service_name}" >&2
  exit 1
fi

port="${port_line##*:}"
host="localhost"
if [[ "${CI:-}" == "true" && "${EXECUTION_MODE:-}" == "DIND" ]]; then
  host="dind"
fi

endpoint="http://${host}:${port}"

for _ in {1..60}; do
  if curl -sS -f "${endpoint}/ui/health" >/dev/null 2>&1; then
    break
  fi
  sleep 2
done

token_response="$(curl -sS -f --user 'ui:uiman' \
  -H 'Content-Type: application/x-www-form-urlencoded' \
  --data-urlencode 'grant_type=password' \
  --data-urlencode "username=${admin_user}" \
  --data-urlencode "password=${admin_password}" \
  "${endpoint}/uat/sso/oauth/token")"

token="$(printf '%s' "$token_response" | sed -n 's/.*"access_token"[[:space:]]*:[[:space:]]*"\([^"]*\)".*/\1/p')"
if [[ -z "$token" ]]; then
  echo "[reporting] access token is empty" >&2
  exit 1
fi

project_response="$(curl -sS -f \
  -H "Authorization: Bearer ${token}" \
  "${endpoint}/api/v1/project/list?page.page=1&page.size=1")"
project_name="$(printf '%s' "$project_response" | sed -n 's/.*"projectName"[[:space:]]*:[[:space:]]*"\([^"]*\)".*/\1/p')"
if [[ -z "$project_name" ]]; then
  project_name="default_personal"
fi

cat > tools/environment/.reportportal.env <<ENV
REPORTPORTAL_ENDPOINT=${endpoint}
REPORTPORTAL_PROJECT=${project_name}
REPORTPORTAL_API_KEY=${token}
REPORTPORTAL_BEARER_TOKEN=${token}
ENV

mkdir -p integrations/reportportal/testng/src/test/resources/local_resources
cat > integrations/reportportal/testng/src/test/resources/local_resources/reportportal-test.properties <<ENV
REPORTPORTAL_ENDPOINT=${endpoint}
REPORTPORTAL_API_KEY=${token}
ENV

echo "[reporting] endpoint: ${endpoint}"
echo "[reporting] project: ${project_name}"
echo "[reporting] env file written: tools/environment/.reportportal.env"
echo "[reporting] owner config written: integrations/reportportal/testng/src/test/resources/local_resources/reportportal-test.properties"
