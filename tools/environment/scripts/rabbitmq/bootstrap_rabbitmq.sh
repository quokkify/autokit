#!/bin/bash
set -euo pipefail
exec ./tools/environment/scripts/infra/modules/rabbitmq/bootstrap_rabbitmq.sh "$@"
