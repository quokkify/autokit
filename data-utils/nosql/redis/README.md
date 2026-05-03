# data-utils/nosql/redis

Provides [Redisson](https://github.com/redisson/redisson) as a managed dependency for Redis access in tests.

## Dependency

```gradle
testImplementation project(":data-utils:nosql:redis")
```

## Usage

Configure and create a `RedissonClient` via environment variables or system properties (`REDIS_HOST`, `REDIS_PORT`, `REDIS_PASSWORD`).

```java
Config config = new Config();
config.setCodec(StringCodec.INSTANCE);
config.useSingleServer()
    .setAddress("redis://127.0.0.1:6379");

RedissonClient client = Redisson.create(config);

RBucket<String> bucket = client.getBucket("my:key");
bucket.set("value");
String value = bucket.get();

client.shutdown();
```

Cluster mode — set `REDIS_CLUSTER_NODES` as a comma-separated list of `host:port` addresses.

## Run local Redis

```bash
./tools/environment/scripts/infra/run_app.sh redis
```
