# Changelog

## 2026-05-03
- `common-utils/*`, `data-utils/*`, `testng-extensions` — added README for all undocumented modules

## 2026-04-XX
- `integrations/rabbitmq` — enabled RabbitMQ module
- `integrations/kafka` — enabled Kafka module
- `integrations/reportportal` — enabled ReportPortal module
- `integrations/rest-assured` — migrated from custom HTTP client to Feign
- `common-utils/jackson` — added auto-discovery for Jackson modules
- `data-utils/nosql/redis` — new module for working with Redis (Redisson)
- `tools/ci` — fallback to public runner when self-hosted is offline
- `tools/ci` — improved GitHub workflows, Docker Compose configuration layering
