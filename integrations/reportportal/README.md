# ReportPortal integration

Run local ReportPortal stack:

```bash
./tools/environment/scripts/infra/run_app.sh reporting
```

This command creates `tools/environment/.reportportal.env` with connection details and token.
It also creates Owner-based test config:
`integrations/reportportal/testng/src/test/resources/local_resources/reportportal-test.properties`.

Run integration test:

```bash
./gradlew :integrations:reportportal:testng:test
```

CI-style run (same flow as workflow):

```bash
CI=true EXECUTION_MODE=CI ./tools/environment/scripts/infra/run_app.sh reporting
./gradlew :integrations:reportportal:testng:check --no-daemon --console=plain --stacktrace
CI=true ./tools/environment/scripts/infra/stop_app.sh reporting
```

Stop infrastructure:

```bash
./tools/environment/scripts/infra/stop_app.sh reporting
```
