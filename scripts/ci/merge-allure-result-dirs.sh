#!/usr/bin/env bash
# Merge multiple Allure result directories into DEST. Concatenates ci-env-fragment.properties into MERGED_ENV_OUT.
#
# Allure CLI (allure generate) consumes a single results directory; there is no built-in command to merge
# parallel CI jobs. This script is the supported aggregation path before generate (see report.yml).
#
# Usage: merge-allure-result-dirs.sh <dest_dir> <merged_env_out_file> <src_dir> [<src_dir> ...]
set -euo pipefail

DEST="${1:?dest}"
MERGED_ENV_OUT="${2:?merged env path}"
shift 2

mkdir -p "${DEST}"
: > "${MERGED_ENV_OUT}"

for src in "$@"; do
  [[ -d "${src}" ]] || continue
  frag="${src}/ci-env-fragment.properties"
  if [[ -f "${frag}" ]]; then
    cat "${frag}" >> "${MERGED_ENV_OUT}"
    echo "" >> "${MERGED_ENV_OUT}"
  fi
done

for src in "$@"; do
  [[ -d "${src}" ]] || continue
  shopt -s nullglob
  for p in "${src}"/*; do
    base="$(basename "${p}")"
    case "${base}" in
      ci-env-fragment.properties | environment.properties) continue ;;
    esac
    cp -a "${p}" "${DEST}/"
  done
  shopt -u nullglob
done
