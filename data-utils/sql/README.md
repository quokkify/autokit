# data-utils/sql

JPA/Hibernate + QueryDSL utilities for SQL database access in test automation.

## Dependency

```gradle
testImplementation project(":data-utils:sql")
```

## Environment variables

| Variable              | Default                  | Description        |
|-----------------------|--------------------------|--------------------|
| `SQL_DATABASE_URL`    | —                        | JDBC URL           |
| `SQL_DATABASE_USER`   | —                        | DB username        |
| `SQL_DATABASE_PASSWORD` | —                      | DB password        |
| `SQL_DATABASE_DRIVER` | `org.postgresql.Driver`  | JDBC driver class  |

## Initialization in BaseTest

```java
public abstract class BaseTest {

    protected SqlDatabaseSteps databaseSteps;

    @BeforeClass(alwaysRun = true)
    public void initDatabase() {
        PersistenceItem persistenceItem = new PersistenceItem(
            "my-persistence-unit",          // matches persistence.xml unit name
            Map.of(
                AvailableSettings.JAKARTA_JDBC_URL,      System.getenv("SQL_DATABASE_URL"),
                AvailableSettings.JAKARTA_JDBC_USER,     System.getenv("SQL_DATABASE_USER"),
                AvailableSettings.JAKARTA_JDBC_PASSWORD, System.getenv("SQL_DATABASE_PASSWORD")
            )
        );
        SqlFactory sqlFactory = DatabaseService.getInstance().createSqlQuery(persistenceItem);
        databaseSteps = new SqlDatabaseSteps(sqlFactory);
    }

    @AfterClass(alwaysRun = true)
    public void closeDatabase() {
        databaseSteps.closeConnection();
    }
}
```

## Custom steps class

Extend `AbstractDatabaseSteps` to add domain-specific queries:

```java
public class UserDbSteps extends AbstractDatabaseSteps {

    private final SqlDatabaseSteps databaseSteps;

    public UserDbSteps(SqlDatabaseSteps databaseSteps) {
        this.databaseSteps = databaseSteps;
    }

    @Override
    protected SqlDatabaseSteps getDatabaseSteps() {
        return databaseSteps;
    }

    public User getUserByEmail(String email) {
        QUser user = QUser.user;
        return fetchOne(steps -> steps.selectDsl(user).where(user.email.eq(email)));
    }

    public User waitForUser(String email) {
        QUser user = QUser.user;
        return waitUntilAppear(steps -> steps.selectDsl(user).where(user.email.eq(email)));
    }
}
```

## Usage in tests

```java
public class UserTest extends BaseTest {

    private UserDbSteps userSteps;

    @BeforeClass(alwaysRun = true)
    public void init() {
        userSteps = new UserDbSteps(databaseSteps);
    }

    @Test
    public void checkUserCreated() {
        User user = userSteps.waitForUser("alice@example.com");
        Assertions.assertThat(user.getStatus()).isEqualTo("active");
    }

    @Test
    public void checkSaveAndSelect() {
        User newUser = new User("bob@example.com");
        databaseSteps.save(newUser);

        QUser q = QUser.user;
        User found = databaseSteps.selectDsl(q)
            .where(q.email.eq("bob@example.com"))
            .fetchOne();

        Assertions.assertThat(found).isNotNull();
    }
}
```

## Key API

| Method | Description |
|--------|-------------|
| `databaseSteps.save(entity)` | Insert one or a list of entities |
| `databaseSteps.update(entity)` | Merge entity changes |
| `databaseSteps.delete(entity)` | Remove entity |
| `databaseSteps.selectDsl(QEntity)` | Start a QueryDSL select query |
| `fetchOne(fn)` | Execute query, throw if null |
| `fetchFirst(fn)` | First result or throw |
| `fetch(fn)` | Fetch list or throw |
| `waitUntilAppear(fn)` | Poll until result appears (60s / 5s) |
