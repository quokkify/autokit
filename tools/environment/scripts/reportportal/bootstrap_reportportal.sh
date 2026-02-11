#!/bin/bash
set -euo pipefail

source "$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/../infra/compose_utils.sh"
init_compose_files

service_name="reportportal-gateway"
admin_user="${REPORTPORTAL_ADMIN_USER:-superadmin}"
admin_password="${REPORTPORTAL_ADMIN_PASSWORD:-erebus}"

require_port="false"
if [[ "${CI:-}" == "true" ]]; then
  require_port="true"
fi
port="$(resolve_published_port "${service_name}" 8080 8084 "${require_port}" || true)"
if [[ -z "$port" ]]; then
  echo "[reporting] cannot resolve exposed port for ${service_name}" >&2
  exit 1
fi

host="$(resolve_runtime_host)"

endpoint="http://${host}:${port}"

ready="false"
for _ in {1..90}; do
  if curl -sS -f "${endpoint}/ui/health" >/dev/null 2>&1 \
      && curl -sS -f "${endpoint}/uat/health" >/dev/null 2>&1 \
      && curl -sS -f "${endpoint}/api/health" >/dev/null 2>&1; then
    ready="true"
    break
  fi
  sleep 2
done
if [[ "$ready" != "true" ]]; then
  echo "[reporting] report portal services are not healthy on ${endpoint}" >&2
  exit 1
fi

candidate_users="${REPORTPORTAL_CANDIDATE_USERS:-${admin_user},superadmin,default}"
last_response=""
token=""
IFS=',' read -ra USERS <<<"$candidate_users"
for user in "${USERS[@]}"; do
  user="$(echo "$user" | xargs)"
  [[ -z "$user" ]] && continue
  for _ in {1..40}; do
    token_response="$(curl -sS --show-error \
      --user 'ui:uiman' \
      -H 'Content-Type: application/x-www-form-urlencoded' \
      --data-urlencode 'grant_type=password' \
      --data-urlencode "username=${user}" \
      --data-urlencode "password=${admin_password}" \
      "${endpoint}/uat/sso/oauth/token" || true)"
    last_response="$token_response"
    token="$(printf '%s' "$token_response" \
      | sed -n 's/.*"access_token"[[:space:]]*:[[:space:]]*"\([^"]*\)".*/\1/p')"
    if [[ -n "$token" ]]; then
      break 2
    fi
    sleep 2
  done
done

if [[ -z "$token" ]]; then
  echo "[reporting] access token is empty" >&2
  if [[ -n "$last_response" ]]; then
    echo "[reporting] oauth response (truncated): ${last_response:0:500}" >&2
  fi
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
