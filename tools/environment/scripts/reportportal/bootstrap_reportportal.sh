#!/bin/bash
set -euo pipefail
exec ./tools/environment/scripts/infra/modules/reportportal/bootstrap_reportportal.sh "$@"
