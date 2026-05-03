# common-utils/jackson/yaml

Load YAML classpath resources into typed Java objects using SnakeYAML with safe construction.

## Dependency

```gradle
testImplementation project(":common-utils:jackson:yaml")
```

## Usage

Load a single YAML document as a typed object:

```java
AppConfig cfg = YamlParser.loadAsObjectFromResources("app.yaml", AppConfig.class);
```

Load a YAML sequence as a list, or a mapping with preserved key order:

```java
List<User> users     = YamlParser.loadListFromResources("users.yaml", User.class);
Map<String, Item> map = YamlParser.loadAsMapFromResources("items.yaml", Item.class);
```
