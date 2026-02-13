#!/bin/bash
set -euo pipefail

source "$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/../compose_utils.sh"

info "[infra] websockets hook: bootstrap websockets environment"
./tools/environment/scripts/infra/modules/websockets/bootstrap_websockets.sh
