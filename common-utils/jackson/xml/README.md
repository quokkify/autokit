# common-utils/jackson/xml

Parse XML streams and classpath resources into typed Java objects using Jackson XmlMapper.

## Dependency

```gradle
testImplementation project(":common-utils:jackson:xml")
```

## Usage

Parse an XML classpath resource or an `InputStream`:

```java
Catalog catalog = XmlParser.parse("data/catalog.xml", Catalog.class);

try (InputStream is = Files.newInputStream(path)) {
    Catalog catalog = XmlParser.parse(is, Catalog.class);
}
```

Deserialize an XML string directly:

```java
Order order = XmlConverter.fromString(xmlString, Order.class);
```
