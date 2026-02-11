#!/bin/bash
set -euo pipefail
exec ./tools/environment/scripts/infra/modules/mock/upload_expectations.sh "$@"
