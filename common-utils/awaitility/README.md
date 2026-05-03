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

## Key API

| Method | Timeout | Poll | Notes |
|---|---|---|---|
| `awaitAssertion(assertion)` | 60s | 5s | AssertJ / TestNG assertion |
| `awaitQuickAssertion(assertion)` | 5s | 500ms | fast path, no overrides |
| `awaitQuickAssertion(assertion, timeout)` | custom | 500ms | custom `Timeout` constant |
| `awaitCondition(callable, message, timeout, interval)` | custom | custom | boolean `Callable` |
| `awaitCondition(supplier, matcher, message, timeout, interval)` | custom | custom | Hamcrest `Matcher` |
| `awaitConditionWithAction(condition, action, message)` | default | default | runs `action` each tick |
| `threadSleep(millis)` | — | — | safe sleep, handles interrupt |
| `waitForNextSecond()` | — | — | waits until clock ticks |
