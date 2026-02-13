# WebSockets integration

Run local WebSockets stack:

```bash
./tools/environment/scripts/infra/run_app.sh websockets
```

This command creates `tools/environment/.websockets.env` with endpoints and credentials.
It also creates Owner-based test config:
`integrations/websockets/src/test/resources/local_resources/websockets.properties`.

Run integration test:

```bash
./gradlew :integrations:websockets:test
```

CI-style run (same flow as workflow):

```bash
CI=true EXECUTION_MODE=CI ./tools/environment/scripts/infra/run_app.sh websockets
source ./tools/environment/scripts/infra/load_bootstrap_env.sh
./gradlew :integrations:websockets:check --no-daemon --console=plain --stacktrace
CI=true ./tools/environment/scripts/infra/stop_app.sh websockets
```

Stop infrastructure:

```bash
./tools/environment/scripts/infra/stop_app.sh websockets
```
