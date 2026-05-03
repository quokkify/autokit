# testng-extensions

TestNG extensions providing retry logic, group filtering, lifecycle listeners, and soft-assertion step chains for test automation.

## Dependency

```gradle
testImplementation project(":testng-extensions")
```

## Environment variables

| Variable             | Default          | Description                              |
|----------------------|------------------|------------------------------------------|
| `RETRY_COUNT`        | `2`              | Number of retries for failed tests       |
| `TEST_THREAD_COUNT`  | `5`              | Parallel thread count                    |
| `TEST_GROUP`         | —                | Group name filter (used by `SingleGroupListener`) |
| `SUITE_NAME`         | `Default suite`  | TestNG suite name                        |
| `EXECUTION_MODE`     | `LOCAL`          | Execution environment: `LOCAL`/`CI`/`DIND` |

Config is read from environment variables or `testng.properties`:

```properties
RETRY_COUNT=3
TEST_THREAD_COUNT=10
EXECUTION_MODE=CI
```

## Initialization in BaseTest

```java
@Listeners({RetryListener.class, SuiteListener.class, SingleGroupListener.class})
public abstract class BaseTest {

    protected static final TestNGExtension CONFIG =
        ConfigRegistry.getReloadable(TestNGExtension.class);
}
```

## Usage in tests

Define a step class with soft-assertion verification:

```java
public class UserSteps extends AbstractSteps<UserVerification> {

    @Override
    public UserVerification verify() {
        return new UserVerification();
    }
}
```

Use `verifySoftly` to batch multiple assertions without early failure:

```java
@Listeners({RetryListener.class, SuiteListener.class})
public class UserTest extends BaseTest {

    private final UserSteps userSteps = new UserSteps();

    @TestGroup("smoke")
    @Test
    public void checkUserProfile() {
        userSteps.verifySoftly(
            v -> v.checkStatus("active"),
            v -> v.checkName("Alice")
        );
    }

    @SingleThread
    @Test
    public void checkSequentialOperation() {
        userSteps.verifySoftly(
            v -> v.checkStatus("active")
        );
    }
}
```

## Key API

| Class / Annotation | Description |
|--------------------|-------------|
| `TestNGExtension` | Owner config interface; read via `ConfigRegistry.getReloadable(TestNGExtension.class)` |
| `AbstractSteps<V>` | Base step class with `verify()` and `verifySoftly(consumers...)` for soft assertions |
| `RetryListener` + `CustomRetryAnalyzer` | Re-run failed tests up to `RETRY_COUNT` times |
| `SingleGroupListener` | Run only tests tagged with `TEST_GROUP` |
| `SuiteListener` / `TestListener` / `StepTestListener` | Lifecycle hooks for suite/test/step events |
| `CustomCasesInterceptor` | Filter test cases before execution |
| `@TestGroup("name")` | Tag a test method with a group name |
| `@SingleThread` | Force single-thread execution for annotated test |
