# common-utils/config

Centralized, type-safe access to Owner configuration interfaces backed by env vars and classpath properties.

## Dependency

```gradle
testImplementation project(":common-utils:config")
```

## Usage

Define a typed config interface, then retrieve it via `ConfigRegistry`:

```java
@Config.Sources({"system:env", "classpath:app.properties"})
interface AppConfig extends Config {
    @Key("API_URL") String apiUrl();
}

AppConfig cfg = ConfigRegistry.get(AppConfig.class);
```

Apply runtime overrides and reload:

```java
ConfigRegistry.overlay(cfg, Map.of("API_URL", "https://staging.example.com"));
```

Use `getMutable` for writable access or `getReloadable` when the config must reload after overrides.
