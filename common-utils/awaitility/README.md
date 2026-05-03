# common-utils/awaitility

Fluent wrapper around Awaitility for polling assertions and conditions in tests, with preset timeout and interval constants.

## Dependency

```gradle
testImplementation project(":common-utils:awaitility")
```

## Usage

Poll until an AssertJ assertion passes (60s timeout, 5s poll):

```java
Waiter.awaitAssertion(() -> assertThat(order.getStatus()).isEqualTo("CONFIRMED"));
```

Quick check with a custom timeout (500ms poll):

```java
Waiter.awaitQuickAssertion(() -> assertTrue(cache.containsKey("session-123")), Timeout.SECONDS_10);
```

Wait for a supplier value to match a Hamcrest matcher:

```java
Waiter.awaitCondition(
        () -> fetchJobStatus(),
        Matchers.equalTo("COMPLETED"),
        "Job never reached COMPLETED",
        Timeout.SECONDS_30,
        PollingInterval.MILLIS_1000
);
```

Assert a flag never flips to true (e.g. no error popup appears for 10 seconds):

```java
Waiter.assertNeverTrue(
        () -> errorPopup.isDisplayed(),
        Timeout.SECONDS_10,
        PollingInterval.MILLIS_500,
        "Error popup appeared unexpectedly"
);
```

Assert a condition holds true for the full window (e.g. status stays ACTIVE):

```java
Waiter.assertAlwaysTrue(
        () -> "ACTIVE".equals(fetchStatus()),
        Timeout.SECONDS_30,
        PollingInterval.MILLIS_1000,
        "Status dropped from ACTIVE before expected"
);
```

## Key API

| Method                                                          | Timeout | Poll    | Notes                         |
| --------------------------------------------------------------- | ------- | ------- | ----------------------------- |
| `awaitAssertion(assertion)`                                     | 60s     | 5s      | AssertJ / TestNG assertion    |
| `awaitQuickAssertion(assertion)`                                | 5s      | 500ms   | fast path, no overrides       |
| `awaitQuickAssertion(assertion, timeout)`                       | custom  | 500ms   | custom `Timeout` constant     |
| `awaitCondition(callable, message, timeout, interval)`          | custom  | custom  | boolean `Callable`            |
| `awaitCondition(supplier, matcher, message, timeout, interval)` | custom  | custom  | Hamcrest `Matcher`            |
| `awaitConditionWithAction(condition, action, message)`          | default | default | runs `action` each tick       |
| `threadSleep(millis)`                                           | —       | —       | safe sleep, handles interrupt |
| `waitForNextSecond()`                                           | —       | —       | waits until clock ticks       |
| `assertNeverTrue(condition, timeout, interval, message)`        | custom  | custom  | fails if condition ever becomes `true`  |
| `assertNeverTrue(condition, message)`                           | 60s     | 1s      | short form with defaults                |
| `assertAlwaysTrue(condition, timeout, interval, message)`       | custom  | custom  | fails if condition ever drops to `false` |
| `assertAlwaysTrue(condition, message)`                          | 60s     | 1s      | short form with defaults                |
