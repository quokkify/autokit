# Kafka integration

Run local Kafka stack:

```bash
./tools/environment/scripts/infra/run_app.sh messaging
```

This command creates `tools/environment/.kafka.env` with Kafka bootstrap server and Kafka UI URL.

Run integration test:

```bash
./gradlew :integrations:kafka:test
```

CI-style run (same flow as workflow):

```bash
CI=true EXECUTION_MODE=CI ./tools/environment/scripts/infra/run_app.sh messaging
set -a && source tools/environment/.kafka.env && set +a
export KAFKA_SERVER_ADDRESS="${KAFKA_BOOTSTRAP_SERVERS}"
./gradlew :integrations:kafka:check --no-daemon --console=plain --stacktrace
CI=true ./tools/environment/scripts/infra/stop_app.sh messaging
```

Stop infrastructure:

```bash
./tools/environment/scripts/infra/stop_app.sh messaging
```
