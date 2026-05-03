# data-utils/nosql/morphia

Morphia-based MongoDB access utility for test automation with thread-safe datastore management.

## Dependency

```gradle
testImplementation project(":data-utils:nosql:morphia")
```

## Usage

Instantiate `NoSqlFactory` with a `MongoClient` and database name, then obtain a per-thread Morphia `Datastore` for queries and persistence:

```java
NoSqlFactory factory = new NoSqlFactory(mongoClient, "mydb");
Datastore ds = factory.getThreadLocalDatastore();
```

Use `MongoDatabaseSteps` for step-style save/find/delete operations against entities that implement `MongoEntityInterface`:

```java
mongoDatabaseSteps.save(myDocument);
mongoDatabaseSteps.findById(MyDocument.class, id);
```
