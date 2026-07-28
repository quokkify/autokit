# Changelog

## [1.0.4](https://github.com/ylazakovich/quokkify/compare/v1.0.3...v1.0.4) (2026-07-28)


### 🐛 Bug Fixes

* **deps:** update allure to v2.35.4 ([#377](https://github.com/ylazakovich/quokkify/issues/377)) ([5a66f4c](https://github.com/ylazakovich/quokkify/commit/5a66f4cef29787f2f6b9453f09c83b3bc288fec9))
* **deps:** update com.github.spotbugs:spotbugs-annotations to v4.10.3 ([#371](https://github.com/ylazakovich/quokkify/issues/371)) ([64e442b](https://github.com/ylazakovich/quokkify/commit/64e442b699224432020f60fb3c2d16869174321b))
* **deps:** update org.mongodb:mongodb-driver-sync to v5.9.1 ([#378](https://github.com/ylazakovich/quokkify/issues/378)) ([de7e54e](https://github.com/ylazakovich/quokkify/commit/de7e54ea8cce9592c0bcc6b122fe797706ee9fce))


### 🧹 Chores

* **deps:** update mongo docker tag to v8.3.7 ([#373](https://github.com/ylazakovich/quokkify/issues/373)) ([f2166f3](https://github.com/ylazakovich/quokkify/commit/f2166f312a3813e69ff6417edfde4bbf54643898))
* **deps:** update nginx docker tag to v1.31.3 ([#368](https://github.com/ylazakovich/quokkify/issues/368)) ([05400be](https://github.com/ylazakovich/quokkify/commit/05400be71ef719683d3602babe18c0352950f5a7))
* **deps:** update redis docker tag to v8.8.1 ([#374](https://github.com/ylazakovich/quokkify/issues/374)) ([f23486c](https://github.com/ylazakovich/quokkify/commit/f23486c6a633b7ed7b96a826deb06fde3e272b92))
* **deps:** update reportportal/service-auto-analyzer docker tag to v5.15.4 ([#375](https://github.com/ylazakovich/quokkify/issues/375)) ([51f09e9](https://github.com/ylazakovich/quokkify/commit/51f09e9ecd2e0365f119b1e183b29c1e11e47b38))
* **deps:** update traefik docker tag to v3.7.8 ([#370](https://github.com/ylazakovich/quokkify/issues/370)) ([ab25bc0](https://github.com/ylazakovich/quokkify/commit/ab25bc0190cecbf25ffe706550ab4a73d8a27889))
* **deps:** update traefik docker tag to v3.7.9 ([#376](https://github.com/ylazakovich/quokkify/issues/376)) ([bb9f276](https://github.com/ylazakovich/quokkify/commit/bb9f276a8700fb978dc86642b16c5a932dda74cf))

## [1.0.3](https://github.com/ylazakovich/quokkify/compare/v1.0.2...v1.0.3) (2026-07-15)


### 🐛 Bug Fixes

* **deps:** update checkstyle to v13.8.0 ([#361](https://github.com/ylazakovich/quokkify/issues/361)) ([aad5824](https://github.com/ylazakovich/quokkify/commit/aad58249c1a2ee0f945a9b0a814697b9a32ae51f))
* **deps:** update hibernate-orm monorepo to v7.4.5.final ([#360](https://github.com/ylazakovich/quokkify/issues/360)) ([33b3057](https://github.com/ylazakovich/quokkify/commit/33b3057c13fea7590e248c9924048b771434f947))
* **deps:** update org.bouncycastle:bcprov-jdk18on to v1.85 ([#365](https://github.com/ylazakovich/quokkify/issues/365)) ([5f9f067](https://github.com/ylazakovich/quokkify/commit/5f9f0679d35ac7db30253f1bdb8dcead8ee28b9d))
* **deps:** update selenide to v7.17.0 ([#362](https://github.com/ylazakovich/quokkify/issues/362)) ([1dbec12](https://github.com/ylazakovich/quokkify/commit/1dbec126af62e5faf2280043e3b040272227f64b))


### 🧹 Chores

* **deps:** update actions/setup-node action to v7 ([#363](https://github.com/ylazakovich/quokkify/issues/363)) ([27af211](https://github.com/ylazakovich/quokkify/commit/27af2114e8d1af7912814f9a58765227e9911528))

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
