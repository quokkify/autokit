# common-utils/awaitility

Fluent wrapper around Awaitility for polling assertions and conditions in tests, with preset timeout/interval constants.

## Dependency

```gradle
testImplementation project(":common-utils:awaitility")
```

## Usage

Poll until a JUnit/Hamcrest assertion passes (60s timeout, 5s interval):

```java
Waiter.awaitAssertion(() -> assertThat(queue.size(), equalTo(3)));
```

Short-lived condition check (5s timeout, 500ms interval):

```java
Waiter.awaitQuickAssertion(() -> assertTrue(service.isReady()));
```

Wait with a Hamcrest matcher and custom timeout:

```java
Waiter.awaitCondition(() -> fetchStatus(), equalTo("DONE"), "Status never reached DONE", 30, 2);
```
