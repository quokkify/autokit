# data-utils/sql

JPA/Hibernate + QueryDSL utilities for SQL database access in test automation — thread-safe factories, step objects, and DB assertions.

## Dependency

```gradle
testImplementation project(":data-utils:sql")
```

## Usage

Create a thread-safe `SqlFactory` from an `EntityManagerFactory` and obtain a per-thread `EntityManager` or QueryDSL `JPAQueryFactory`:

```java
SqlFactory factory = new SqlFactory(entityManagerFactory);
EntityManager em = factory.getThreadLocalEntityManager();
JPAQueryFactory qf = factory.getThreadLocalQueryFactory();
```

Assert database state with `DatabaseVerification` after executing step-style operations via `SqlDatabaseSteps`:

```java
databaseSteps.save(myEntity);
databaseVerification.assertThat(QMyEntity.myEntity.status).isEqualTo("active");
```
