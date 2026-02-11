#!/bin/bash
set -euo pipefail
exec ./tools/environment/scripts/infra/modules/mock/run_mock_server.sh "$@"
