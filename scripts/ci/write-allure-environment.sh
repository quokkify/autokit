#!/usr/bin/env bash
# Writes Allure 3 variables JSON from merged CI metadata. Keeps secrets out of reports.
set -euo pipefail

ALLURE_RESULTS_DIR="${ALLURE_RESULTS_DIR:-allure-results}"
REPO_ROOT="${REPO_ROOT:-${GITHUB_WORKSPACE:-.}}"
OUT="${ALLURE_RESULTS_DIR}/environment.properties"

mkdir -p "${ALLURE_RESULTS_DIR}"

write_kv() {
  local key="$1"
  local val="$2"
  val="${val//$'\r'/}"
  val="${val//$'\n'/ }"
  printf '%s=%s\n' "${key}" "${val}" >> "${OUT}"
}

: > "${OUT}"

if [[ -n "${ALLURE_MERGED_CI_ENV:-}" && -f "${ALLURE_MERGED_CI_ENV}" ]]; then
  cat "${ALLURE_MERGED_CI_ENV}" >> "${OUT}"
fi

write_kv "CI" "${CI:-false}"
[[ -n "${RUNNER_OS:-}" ]] && write_kv "Runner.OS" "${RUNNER_OS}"
[[ -n "${RUNNER_ARCH:-}" ]] && write_kv "Runner.Arch" "${RUNNER_ARCH}"
[[ -n "${GITHUB_REPOSITORY:-}" ]] && write_kv "GitHub.Repository" "${GITHUB_REPOSITORY}"
[[ -n "${GITHUB_REF_NAME:-}" ]] && write_kv "GitHub.Ref" "${GITHUB_REF_NAME}"
[[ -n "${ALLURE_GITHUB_WORKFLOW:-${GITHUB_WORKFLOW:-}}" ]] && write_kv "GitHub.Workflow" "${ALLURE_GITHUB_WORKFLOW:-${GITHUB_WORKFLOW:-}}"
[[ -n "${ALLURE_SOURCE_RUN_ID:-${GITHUB_RUN_ID:-}}" ]] && write_kv "GitHub.RunId" "${ALLURE_SOURCE_RUN_ID:-${GITHUB_RUN_ID:-}}"

sha="${GITHUB_SHA:-}"
[[ -n "${sha}" ]] && write_kv "GitHub.SHA" "${sha:0:7}"

if command -v java >/dev/null 2>&1; then
  write_kv "Java" "$(java -version 2>&1 | head -n 1 | tr -d '\r')"
fi
if [[ -x "${REPO_ROOT}/gradlew" ]]; then
  gradle_version="$("${REPO_ROOT}/gradlew" --version --no-daemon 2>/dev/null | awk '/^Gradle / {print $2; exit}' || true)"
  [[ -n "${gradle_version}" ]] && write_kv "Gradle" "${gradle_version}"
fi

ALLURE_VARIABLES_JSON="${ALLURE_VARIABLES_JSON:-${REPO_ROOT}/artifacts/allure-variables.json}"
if ! command -v node >/dev/null 2>&1; then
  echo "::error::node is required to write ${ALLURE_VARIABLES_JSON}"
  exit 1
fi
mkdir -p "$(dirname "${ALLURE_VARIABLES_JSON}")"
node --input-type=module - "${OUT}" "${ALLURE_VARIABLES_JSON}" <<'NODE'
import fs from "node:fs";

const [, , propertiesPath, outputPath] = process.argv;
const data = {};

if (fs.existsSync(propertiesPath)) {
  for (const rawLine of fs.readFileSync(propertiesPath, "utf8").split(/\r?\n/)) {
    const line = rawLine.trim();
    if (!line || line.startsWith("#") || !line.includes("=")) continue;
    const index = line.indexOf("=");
    const key = line.slice(0, index).trim();
    const value = line.slice(index + 1).trim();
    if (key && value) data[key] = value;
  }
}

fs.writeFileSync(outputPath, JSON.stringify(data), "utf8");
NODE

rm -f "${OUT}"
