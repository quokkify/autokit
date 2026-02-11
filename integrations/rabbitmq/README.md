# RabbitMQ integration

Run local RabbitMQ stack:

```bash
./tools/environment/scripts/infra/run_app.sh rabbitmq
```

This command creates `tools/environment/.rabbitmq.env` with RabbitMQ connection details.
It also creates Owner-based test config:
`integrations/rabbitmq/src/test/resources/local_resources/rabbit.properties`.

Run integration test:

```bash
./gradlew :integrations:rabbitmq:test
```

CI-style run (same flow as workflow):

```bash
CI=true EXECUTION_MODE=CI ./tools/environment/scripts/infra/run_app.sh rabbitmq
set -a && source tools/environment/.rabbitmq.env && set +a
./gradlew :integrations:rabbitmq:check --no-daemon --console=plain --stacktrace
CI=true ./tools/environment/scripts/infra/stop_app.sh rabbitmq
```

Stop infrastructure:

```bash
./tools/environment/scripts/infra/stop_app.sh rabbitmq
```
