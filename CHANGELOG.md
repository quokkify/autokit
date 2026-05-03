# Changelog

## 2026-05-03

- `docs` — rewrote all module READMEs with BaseTest initialization and usage patterns
- `docs` — added README for data-utils/nosql/redis
- `docs` — fixed data-utils/sql README with correct initialization chain and QueryDSL examples
- `docs` — added README for all previously undocumented modules (awaitility, config, console, file, html, introspection, jackson, jwt, signature, morphia, sql, testng-extensions)
- `ci` — added fallback to public GitHub runner when self-hosted runner is offline
- `common-utils/awaitility` — added `assertNeverTrue` and `assertAlwaysTrue` methods to `Waiter`, ported from ptaf-core
- `common-utils/file` — added `readAsString(Path)`, `getResourceAsString(String)`, `getResourcePath(String, String)`, and `getDirectoriesAsEnumValuesFromConfiguration` methods, ported from ptaf-core
- `integrations/rest-assured` — added ReportPortal rest-assured logger dependency (`logger-java-rest-assured:5.3.4`)
- `integrations/selenide` — added `sendKeys(Double)`, `sendKeys(LocalDate, DateType)`, and `sendKeys(LocalDateTime, DateType)` overloads to `Input`, ported from ptaf-core
- `docs` — added SPI-based listener loading section to `testng-extensions` README

## 2026-02-11

- `ci/workflows` — improved GitHub Actions workflow configuration
- `environment` — refactored Docker Compose configuration layering and environment bootstrap scripts
- `integrations/rabbitmq` — enabled RabbitMQ integration module with client, steps, and integration tests

## 2026-02-10

- `integrations/kafka` — enabled Kafka integration module
- `integrations/reportportal` — enabled ReportPortal integration module with TestNG listener and configuration
- `integrations/testrail` — migrated custom HTTP client to Feign
- `ci` — resolved CI issue for self-hosted runner and disabled flaky test on main branch
- `data-utils/nosql/redis` — added new Redis module (Redisson-based) with smoke tests
- `data-utils/nosql/morphia` — refactored MongoDB module structure under morphia submodule

## 2026-02-09

- `ci/runner` — enabled Renovate to trigger CI via self-hosted runner
- `common-utils/jackson` — added auto-discovery for Jackson modules via SPI in JSON, XML, YAML, and CSV converters

## 2026-02-01

- `integrations/testrail` — added TestRail integration module with full API client, TestNG listeners, and Jira ticket source
- `data-utils/nosql` — enabled MongoDB module with Morphia-based entity support and integration tests

## 2026-01-18

- `ci` — hotfix for self-hosted runner type detection
- `ci` — added self-hosted runner preference with safe fallback to GitHub-hosted runner

## 2026-01-01

- `testng-extensions` — created TestNG extension configuration with suite lifecycle listener, wired via SPI across all modules

## 2025-12-16

- `integrations/selenide` — added Selenide module with Page Object component library (buttons, inputs, tables, dropdowns) and browser configuration
- `common-utils/html` — added HTML constants and parser exception utilities

## 2025-12-15

- `ci` — configured concurrent CI jobs with one module per runner for parallel execution
- `ci` — fixed workflow summary generation issue
- `docs` — added Renovate badge to README

## 2025-10-06

- `ci` — enabled Prettier lint check via GitHub Actions workflow

## 2025-10-03

- `data-utils/sql` — replaced HSQL with H2 as in-memory database for SQL module tests

## 2025-10-01

- `common-utils/jackson` — fixed Jackson deprecations across JSON/XML/YAML converters
- `build` — replaced deprecated SpotBugs usages and applied checkstyle fixes

## 2025-09-30

- `build` — initial multi-module Gradle layout, GitHub Actions workflow, checkstyle configuration
- `data-utils/sql` — added SQL module implementation
- `tools/environment` — added local Docker Compose environment with mock-server expectations

## 2025-09-15

- `project` — initial project bootstrap

## 2025-09-12

- `project` — initial commit
