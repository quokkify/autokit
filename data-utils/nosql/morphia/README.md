# data-utils/nosql/morphia

Morphia-based MongoDB access utility for test automation with thread-safe datastore management.

## Dependency

```gradle
testImplementation project(":data-utils:nosql:morphia")
```

## Environment variables

| Variable           | Default | Description            |
|--------------------|---------|------------------------|
| `MONGO_HOST`       | —       | MongoDB host           |
| `MONGO_PORT`       | —       | MongoDB port           |
| `MONGO_DATABASE`   | —       | MongoDB database name  |

## Initialization in BaseTest

```java
public abstract class BaseTest {

    protected MongoDatabaseSteps mongoSteps;

    @BeforeClass(alwaysRun = true)
    public void initDatabase() {
        String host = System.getenv("MONGO_HOST");
        String port = System.getenv("MONGO_PORT");
        String dbName = System.getenv("MONGO_DATABASE");

        MongoClient mongoClient = MongoClients.create("mongodb://" + host + ":" + port);
        NoSqlFactory noSqlFactory = new NoSqlFactory(mongoClient, dbName);
        mongoSteps = new MongoDatabaseSteps(noSqlFactory);
    }
}
```

## Usage in tests

```java
public class UserTest extends BaseTest {

    @Test
    public void checkSaveAndQueryUser() {
        User user = new User("alice@example.com", 25);
        mongoSteps.save(user);

        User found = mongoSteps.selectDsl(User.class)
            .filter(Filters.eq("email", "alice@example.com"))
            .first();

        Assertions.assertThat(found).isNotNull();
        Assertions.assertThat(found.getEmail()).isEqualTo("alice@example.com");
    }

    @Test
    public void checkBulkSaveAndFilter() {
        List<User> users = List.of(new User("bob@example.com", 20), new User("carol@example.com", 30));
        mongoSteps.save(users);

        List<User> adults = mongoSteps.selectDsl(User.class)
            .filter(Filters.gte("age", 18))
            .iterator().toList();

        Assertions.assertThat(adults).hasSizeGreaterThanOrEqualTo(2);
    }
}
```

## Key API

| Method | Description |
|--------|-------------|
| `new NoSqlFactory(mongoClient, dbName)` | Wrap MongoClient with thread-safe datastore |
| `noSqlFactory.getThreadLocalDatastore()` | Return thread-local Morphia `Datastore` |
| `mongoSteps.save(entity)` | Insert one entity |
| `mongoSteps.save(List<T> entities)` | Insert a list of entities |
| `mongoSteps.update(entity, operators)` | Apply update operators, return `UpdateResult` |
| `mongoSteps.delete(entity)` | Remove entity, return `DeleteResult` |
| `mongoSteps.selectDsl(Entity.class)` | Start a Morphia `Query` for the given entity type |
