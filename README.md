# Q4J

[![Renovate enabled](https://img.shields.io/badge/Renovate-enabled-brightgreen.svg?logo=renovate&style=flat)](https://renovatebot.com/)
[![Java 21](https://img.shields.io/badge/Java-21-ED8B00?logo=openjdk)](https://openjdk.org/projects/jdk/21/)
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

**Q4J** is a modular Java toolkit for test automation and quality engineering. Pick only the libraries needed by a project instead of importing one monolithic framework.

- Maven group: `dev.quokkify`
- Java packages: `dev.quokkify.*`
- Java baseline: 21
- Build: Gradle

## Install a module

```kotlin
dependencies {
    implementation("dev.quokkify:q4j-core:<version>")
    testImplementation("dev.quokkify:q4j-testng:<version>")
}
```

```groovy
dependencies {
    implementation "dev.quokkify:q4j-rest-assured:<version>"
}
```

## Modules

### Foundation

| Artifact | Purpose |
| --- | --- |
| `q4j-core` | Shared types, formatting, generators, and utility APIs |
| `q4j-config` | Typed configuration and locale providers |
| `q4j-testng` | TestNG listeners, retries, annotations, and lifecycle extensions |
| `q4j-awaitility` | Polling and timeout abstractions |
| `q4j-reflection` | Classpath scanning and reflection |
| `q4j-files` | Files, archives, locking, and local resources |
| `q4j-html` | HTML parsing and browser-compatibility models |
| `q4j-jwt` | JWT models, generators, and formatting |
| `q4j-crypto` | Encryption, keys, and digital signatures |
| `q4j-ssh` | SSH execution and port forwarding |

### Data formats

| Artifact | Purpose |
| --- | --- |
| `q4j-jackson-support` | Shared Jackson dependencies and configuration |
| `q4j-jackson-json` | JSON mapping and JSON Pointer utilities |
| `q4j-jackson-yaml` | YAML parsing and resource providers |
| `q4j-jackson-xml` | XML parsing and conversion |
| `q4j-jackson-csv` | CSV parsing and conversion |

### Data access

| Artifact | Purpose |
| --- | --- |
| `q4j-sql` | SQL, JPA, persistence, and database verification |
| `q4j-morphia` | MongoDB and Morphia persistence helpers |
| `q4j-redis` | Redis operations and verification |

### Test integrations

| Artifact | Purpose |
| --- | --- |
| `q4j-rest-assured` | REST Assured API testing |
| `q4j-selenide` | Browser automation with Selenide |
| `q4j-selenide-proxy` | Proxy and HAR support for Selenide |
| `q4j-selenide-grid` | Selenium Grid support for Selenide |
| `q4j-kafka` | Kafka producers, consumers, and assertions |
| `q4j-rabbitmq` | RabbitMQ integration testing |
| `q4j-tyrus` | WebSocket testing with Tyrus |
| `q4j-jira-core` | Jira client and ticket abstractions |
| `q4j-jira-testng` | Jira integration for TestNG |
| `q4j-jira-testrail` | Jira ticket sources for TestRail workflows |
| `q4j-testrail-core` | TestRail API models and services |
| `q4j-testrail-testng` | TestRail lifecycle integration for TestNG |
| `q4j-reportportal-core` | ReportPortal configuration and API services |
| `q4j-reportportal-testng` | ReportPortal listeners for TestNG |
| `q4j-reportportal-testrail` | TestRail descriptions for ReportPortal |

The internal `q4j-nosql` Gradle project is only a structural parent and is not published.

## Package migration

Q4J replaces the former `io.automation.*` namespace with `dev.quokkify.*`. This is an intentional pre-1.0 breaking change. Consumers must update imports, TestNG listener class names, service-provider declarations, and any package names stored in configuration.

## Documentation

- [Maven Central publishing](docs/publishing.md)
- [ReportPortal integration](integrations/reportportal/README.md)
- [RabbitMQ integration](integrations/rabbitmq/README.md)

## License

Q4J is available under the [MIT License](LICENSE).
