# testng-extensions

TestNG extensions providing retry logic, group filtering, lifecycle listeners, and soft-assertion step chains for test automation.

## Dependency

```gradle
testImplementation project(":testng-extensions")
```

## Usage

Configure runtime behaviour via `testng.properties` or environment variables exposed by `TestNGExtension`:

```properties
RETRY_COUNT=3
TEST_THREAD_COUNT=10
EXECUTION_MODE=CI
```

Extend `AbstractSteps` to get a fluent `verify()` chain and `verifySoftly` for batched AssertJ soft assertions:

```java
public class UserSteps extends AbstractSteps<UserVerification> {
    public UserVerification verify() { return new UserVerification(); }
}

userSteps.verifySoftly(
    v -> v.checkStatus("active"),
    v -> v.checkName("Alice")
);
```

Register `CustomRetryAnalyzer` / `RetryListener` in your TestNG suite to automatically re-run flaky tests up to `RETRY_COUNT` times, and use `SingleGroupListener` with `@TestGroup` to restrict a run to a single named group.
