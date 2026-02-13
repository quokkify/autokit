package io.automation.websockets.services.centrifugo;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import io.automation.util.Waiter;
import io.automation.websockets.entities.WebSocketMessage;
import io.github.centrifugal.centrifuge.Client;
import io.github.centrifugal.centrifuge.DuplicateSubscriptionException;
import io.github.centrifugal.centrifuge.Options;
import io.github.centrifugal.centrifuge.StreamPosition;
import io.github.centrifugal.centrifuge.Subscription;
import io.github.centrifugal.centrifuge.SubscriptionOptions;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;

@Log4j2
public class CentrifugoService {

  private static final String DEFAULT_CENTRUFUGO_ENDPOINT = "/connection/websocket";
  private static final long CLOSE_CONNECTION_TIMEOUT_MILLIS = 3 * 1000;
  private final CentrifugoEventListener eventListener = new CentrifugoEventListener();
  private final CentrifugoSubscriptionEventListener subscriptionEventListener =
      new CentrifugoSubscriptionEventListener();
  private Client client;

  /**
   * Establishes a connection to Centrifugo using the provided host and token with default endpoint.
   *
   * @param host  the host URL of the Centrifugo server
   * @param token the authentication token for the connection
   * @return the CentrifugoService object after establishing the connection
   */
  public CentrifugoService connectToCentrifugo(String host, String token) {
    return connectToCentrifugo(host, DEFAULT_CENTRUFUGO_ENDPOINT, token);
  }

  /**
   * Establishes a connection to Centrifugo using the provided host, endpoint, and token.
   *
   * @param host     the host URL of the Centrifugo server
   * @param endpoint the endpoint URL to connect to on the Centrifugo server
   * @param token    the authentication token for the connection
   * @return the CentrifugoService object after establishing the connection
   */
  public CentrifugoService connectToCentrifugo(String host, String endpoint, String token) {
    Options opts = new Options();
    opts.setToken(token);
    client = new Client("%s%s".formatted(host, endpoint), opts, eventListener);
    client.connect();
    return this;
  }

  /**
   * Disconnects from the Centrifugo server.
   */
  @SneakyThrows(InterruptedException.class)
  public void disconnectFromCentrifugo() {
    client.disconnect();
    client.close(CLOSE_CONNECTION_TIMEOUT_MILLIS);
  }

  /**
   * Subscribe to a specified channel in Centrifugo for receiving real-time messages.
   *
   * @param channelName the name of the channel to subscribe
   */
  public void subscribe(String channelName) {
    Subscription subscription;
    try {
      subscription = client.newSubscription(channelName, subscriptionEventListener);
    } catch (DuplicateSubscriptionException e) {
      throw new RuntimeException("Unable to subscribe to channel '%s'".formatted(channelName), e);
    }
    subscribeWithWait(subscription);
  }

  /**
   * Unsubscribe from a specified channel in Centrifugo and remove it from the client registry.
   *
   * @param channelName the name of the channel to unsubscribe from
   */
  public void unsubscribe(String channelName) {
    Subscription subscription = client.getSubscription(channelName);
    if (Objects.isNull(subscription)) {
      throw new RuntimeException("Unable to unsubscribe from channel '%s' because subscription was not found"
          .formatted(channelName));
    }
    client.removeSubscription(subscription);
  }

  /**
   * Subscribe to a specified historical channel in Centrifugo for receiving real-time messages.
   *
   * @param channelName the name of the channel to subscribe to
   * @param offset      require message start position in history channel
   * @param epoch       the identifier of current history
   */
  public void subscribeHistorical(String channelName, long offset, String epoch) {
    Subscription subscription;
    try {
      SubscriptionOptions subscriptionOptions = new SubscriptionOptions();
      subscriptionOptions.setRecoverable(true);
      subscriptionOptions.setSince(new StreamPosition(offset, epoch));
      subscription = client.newSubscription(channelName, subscriptionOptions, subscriptionEventListener);
    } catch (DuplicateSubscriptionException e) {
      throw new RuntimeException("Unable to subscribe to historical channel '%s'".formatted(channelName), e);
    }
    subscribeWithWait(subscription);
  }

  /**
   * Get messages as String collection.
   *
   * @return copy of the list of messages created at the moment of calling this method
   */
  public List<String> getCopiedMessages() {
    return subscriptionEventListener.getWebSocketMessages().stream()
        .map(WebSocketMessage::getMessage)
        .collect(Collectors.toList());
  }

  /**
   * Clear all previously received messages.
   */
  public void clearMessages() {
    subscriptionEventListener.getWebSocketMessages().clear();
  }

  /**
   * Get messages as WebSocketMessage with Timestamp field.
   * It is recommended to use this method because the collection is modified asynchronously and
   * {@link java.util.ConcurrentModificationException} exception may be thrown if the collection
   * is modified (element is added or deleted) during the collection foreach process.
   *
   * @return copy of the list of WebSocket messages with Timestamp created at the moment of calling this method
   */
  public List<WebSocketMessage> getCopiedWebSocketMessages() {
    return List.copyOf(subscriptionEventListener.getWebSocketMessages());
  }

  /**
   * Waits until the provided channel is subscribed.
   *
   * @param channelName the name of the channel to wait for subscription
   */
  public void waitUntilSubscribed(String channelName) {
    Waiter.awaitAssertion(() -> Assertions.assertThat(subscriptionEventListener.isSubscribed(channelName))
        .as("Channel '%s' is not subscribed", channelName)
        .isTrue());
  }

  public void subscribeWithWait(Subscription subscription) {
    subscription.subscribe();
    waitUntilSubscribed(subscription.getChannel());
  }
}
