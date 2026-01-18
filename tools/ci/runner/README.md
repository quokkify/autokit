# Self-hosted runner (Docker + DinD)

This runner uses Docker-in-Docker to avoid exposing the host Docker socket.

## Setup

1. Copy `.env.example` to `.env` and fill values.
2. Get a registration token (valid for 1 hour):
   ```bash
   gh api -X POST repos/ylazakovich/quokkify/actions/runners/registration-token -q .token
   ```
3. Start the runner:
   ```bash
   ./tools/ci/runner/start_runner.sh
   ```

## Stop

```bash
./tools/ci/runner/stop_runner.sh
```

Notes:

- Runner labels: `self-hosted, linux, compose, internal`
- Runner is ephemeral: it unregisters after each job run.
