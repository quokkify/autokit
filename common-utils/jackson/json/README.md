# common-utils/jackson/json

Jackson-based JSON serialization/deserialization utility with module auto-discovery and sensible defaults.

## Dependency

```gradle
testImplementation project(":common-utils:jackson:json")
```

## Usage

Serialize and deserialize plain objects:

```java
String json   = JsonConverter.toJson(order);
Order restored = JsonConverter.fromString(json, Order.class);
```

Handle generic wrapper types and skip nulls on output:

```java
Response<Item> response = JsonConverter.fromStringParametric(json, Response.class, Item.class);

String compact = JsonConverter.toJsonIgnoreNulls(order);
```
