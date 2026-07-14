# Changelog

## [1.0.2](https://github.com/ylazakovich/quokkify/compare/v1.0.1...v1.0.2) (2026-07-14)


### ⚙️ CI

* **release:** add emoji headings in release changelog ([#357](https://github.com/ylazakovich/quokkify/issues/357)) ([4774552](https://github.com/ylazakovich/quokkify/commit/4774552357021927eb46baa0c0b807281730ed5f))

## [1.0.1](https://github.com/ylazakovich/quokkify/compare/v1.0.0...v1.0.1) (2026-07-11)


### Documentation

* **changelog:** curate initial release notes ([#352](https://github.com/ylazakovich/quokkify/issues/352)) ([b0677e7](https://github.com/ylazakovich/quokkify/commit/b0677e7f02a2cc07f5c2306a793e18992e909623))
* **changelog:** format release notes ([#350](https://github.com/ylazakovich/quokkify/issues/350)) ([2756ad0](https://github.com/ylazakovich/quokkify/commit/2756ad08cd0da74bbdfb4deef2ca375856fdeca3))


### CI

* **prettier:** ignore generated changelog ([#351](https://github.com/ylazakovich/quokkify/issues/351)) ([90ea1d4](https://github.com/ylazakovich/quokkify/commit/90ea1d4d144d93d5e3ab222df28d2b25eade0a11))
* **release:** configure changelog sections ([#353](https://github.com/ylazakovich/quokkify/issues/353)) ([353dba1](https://github.com/ylazakovich/quokkify/commit/353dba1ec5ee636fa53cbd81ef250bff769a7708))

## 1.0.0 (2026-07-11)

### Features

- Added the initial multi-module Quokkify platform structure with common utilities, data utilities, integrations, and TestNG extension modules.
- Added Selenide support with Page Object helpers, component abstractions, browser configuration, and dedicated Selenide integration modules ([#51](https://github.com/ylazakovich/quokkify/issues/51), [#101](https://github.com/ylazakovich/quokkify/issues/101), [#201](https://github.com/ylazakovich/quokkify/issues/201)).
- Added Tyrus WebSocket testing support with client, steps, verifier, and embedded echo-server integration tests ([#195](https://github.com/ylazakovich/quokkify/issues/195)).
- Added data utility modules for SQL, MongoDB/Morphia, and Redis-backed test workflows.
- Added integration modules for REST Assured, Kafka, RabbitMQ, Jira, TestRail, ReportPortal, and Tyrus.
- Added SPI-based TestNG extension configuration and listener loading across modules ([#107](https://github.com/ylazakovich/quokkify/issues/107)).
- Added fluent verification APIs with `verify().withTimeout().withPolling()` style chains across modules ([#252](https://github.com/ylazakovich/quokkify/issues/252)).
- Added ReportPortal integration decoupling via SPI and expanded unit/integration coverage ([#237](https://github.com/ylazakovich/quokkify/issues/237), [#247](https://github.com/ylazakovich/quokkify/issues/247)).

### Bug Fixes

- Closed REST Assured connections when `max_response_time` is exceeded ([#202](https://github.com/ylazakovich/quokkify/issues/202)).
- Stabilized GitHub Actions runner selection with self-hosted preference and GitHub-hosted fallback ([#115](https://github.com/ylazakovich/quokkify/issues/115)).
- Fixed workflow permissions and reporting issues needed for reliable CI/report publication.

### Dependencies

- Updated the dependency baseline across testing, serialization, persistence, messaging, logging, and browser/integration modules.

### Documentation

- Reworked module READMEs with BaseTest initialization examples and practical usage patterns.
- Documented previously uncovered modules including awaitility, config, console, file, html, introspection, jackson, jwt, signature, morphia, sql, redis, and testng-extensions.

### CI

- Added parallel module CI with one-module-per-runner execution ([#98](https://github.com/ylazakovich/quokkify/issues/98)).
- Added Prettier formatting checks, Build/Test matrices, Allure report publishing, GitHub Pages report deployment, PR title validation, and Release Please release automation.
