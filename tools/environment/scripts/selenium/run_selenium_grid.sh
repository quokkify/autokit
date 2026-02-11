#!/bin/bash
set -euo pipefail
exec ./tools/environment/scripts/infra/modules/selenium/run_selenium_grid.sh "$@"
