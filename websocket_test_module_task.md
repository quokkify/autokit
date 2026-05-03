
# WebSocket Test Module Task

## Goal
Design and implement a Java module for working with WebSockets, optimized for automated testing.

The API must allow fluent verification like:

```java
wsSteps.channel.verify().containsMessage(...)
```

---

## Functional Requirements

### Core Capabilities
- Connect to WebSocket
- Subscribe / join channel
- Send message
- Receive and store messages
- Close connection

### Verification Features
- Contains message
- Does not contain message
- Partial match
- JSON field validation
- Message order validation
- Duplicate handling

### Non-Functional
- Thread-safe
- Deterministic (no flaky tests)
- Timeout-based waiting

---

## Architecture Requirements

### Core Module
- WebSocket client wrapper
- Message collector (queue-based)
- Verifier layer (fluent API)
- Matchers (string, JSON, custom)

### API Example
```java
wsSteps.channel.verify()
    .containsMessage("order_created")
    .hasJsonField("type", "order_created");
```

---

## Testing Requirements

### Unit Tests (no Docker)
- Message collector behavior
- Verifier logic
- Timeout handling
- Matching logic
- Concurrency safety

### Integration Tests
- Real WebSocket connection
- Message flow validation
- Multiple messages handling

Prefer embedded server. Use Docker/Testcontainers only if necessary.

---

## Test Scenarios

- Connect / disconnect
- Receive single message
- Receive multiple messages
- Wait for expected message
- Assert message absence
- JSON validation
- Order validation
- Duplicate messages
- Concurrent messages
- Reconnect scenario

---

## Technical Stack

- Java
- Tyrus (preferred) or similar WebSocket client
- JUnit 5
- Awaitility (recommended)

---

## Additional Notes

- Prefer queue over cache for message storage
- Caffeine can be used optionally for deduplication only
- API should prioritize readability and maintainability
