package io.automation.websockets.steps;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import com.fasterxml.jackson.databind.JsonNode;
import io.automation.constant.PollingInterval;
import io.automation.constant.Timeout;
import io.automation.util.JsonConverter;
import io.automation.util.Waiter;
import io.automation.websockets.client.WsClient;
import io.automation.websockets.client.WsMessage;

public final class WsVerifier {

  private final WsClient client;
  private final Timeout timeout;
  private final PollingInterval pollingInterval;

  WsVerifier(WsClient client) {
    this(client, Timeout.SECONDS_10, PollingInterval.MILLIS_500);
  }

  private WsVerifier(WsClient client, Timeout timeout, PollingInterval pollingInterval) {
    this.client = client;
    this.timeout = timeout;
    this.pollingInterval = pollingInterval;
  }

  public WsVerifier withTimeout(Timeout timeout) {
    return new WsVerifier(client, timeout, pollingInterval);
  }

  public WsVerifier withPolling(PollingInterval pollingInterval) {
    return new WsVerifier(client, timeout, pollingInterval);
  }

  public WsVerifier containsMessage(String substring) {
    Waiter.awaitCondition(
        () -> client.getMessages().stream().anyMatch(m -> m.payload().contains(substring)),
        "Expected WS message containing: " + substring,
        timeout, pollingInterval
    );
    return this;
  }

  public WsVerifier containsMessage(Predicate<WsMessage> predicate) {
    Waiter.awaitCondition(
        () -> client.getMessages().stream().anyMatch(predicate),
        "Expected WS message matching predicate",
        timeout, pollingInterval
    );
    return this;
  }

  public WsVerifier doesNotContainMessage(String substring) {
    Waiter.assertNeverTrue(
        () -> client.getMessages().stream().anyMatch(m -> m.payload().contains(substring)),
        Timeout.SECONDS_3, PollingInterval.MILLIS_500,
        "Unexpected WS message containing: " + substring
    );
    return this;
  }

  public WsVerifier hasJsonField(String field, String expectedValue) {
    Waiter.awaitCondition(
        () -> client.getMessages().stream().anyMatch(m -> {
          try {
            JsonNode node = JsonConverter.fromString(m.payload(), JsonNode.class);
            String actual = node.path(field).asText(null);
            return expectedValue.equals(actual);
          } catch (Exception e) {
            return false;
          }
        }),
        "Expected WS message with JSON field '" + field + "' = '" + expectedValue + "'",
        timeout, pollingInterval
    );
    return this;
  }

  public WsVerifier hasMessageCount(int expected) {
    Waiter.awaitCondition(
        () -> client.getMessages().size() >= expected,
        "Expected at least " + expected + " WS messages",
        timeout, pollingInterval
    );
    return this;
  }

  public WsVerifier messagesInOrder(String... substrings) {
    Waiter.awaitCondition(
        () -> {
          List<WsMessage> messages = client.getMessages();
          int idx = 0;
          for (WsMessage msg : messages) {
            if (idx < substrings.length && msg.payload().contains(substrings[idx])) {
              idx++;
            }
          }
          return idx == substrings.length;
        },
        "Expected WS messages in order: " + Arrays.toString(substrings),
        timeout, pollingInterval
    );
    return this;
  }
}
