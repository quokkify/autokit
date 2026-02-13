package io.automation.websockets.services.centrifugo;

import java.net.Proxy;
import java.net.URI;
import java.util.List;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.stream.Collectors;

import io.automation.constant.PollingInterval;
import io.automation.constant.Timeout;
import io.automation.util.Waiter;
import io.automation.websockets.entities.WebSocketMessage;
import io.github.centrifugal.centrifuge.Client;
import io.github.centrifugal.centrifuge.DuplicateSubscriptionException;
import io.github.centrifugal.centrifuge.Options;
import io.github.centrifugal.centrifuge.StreamPosition;
import io.github.centrifugal.centrifuge.Subscription;
import io.github.centrifugal.centrifuge.SubscriptionOptions;
import org.assertj.core.api.Assertions;

public class CentrifugoService {

  private static final String DEFAULT_CENTRUFUGO_ENDPOINT = "/connection/websocket";
  private static final long CLOSE_CONNECTION_TIMEOUT_MILLIS = 3 * 1000;
  private final CentrifugoEventListener eventListener = new CentrifugoEventListener();
  private final CentrifugoSubscriptionEventListener subscriptionEventListener =
      new CentrifugoSubscriptionEventListener();
  private Client client;

  public CentrifugoService connectToCentrifugo(String host, String token) {
    return connectToCentrifugo(host, DEFAULT_CENTRUFUGO_ENDPOINT, token);
  }

  public CentrifugoService connectToCentrifugo(String host, String endpoint, String token) {
    RuntimeException lastError = null;
    List<String> hosts = resolveConnectionHosts(host);
    for (String candidateHost : hosts) {
      eventListener.resetConnectionState();
      try {
        connect(candidateHost, endpoint, token);
        waitUntilConnected(candidateHost, endpoint);
        return this;
      } catch (RuntimeException e) {
        lastError = e;
        disconnectQuietly();
      }
    }

    throw new RuntimeException(
        "Unable to connect to Centrifugo. Tried hosts: %s".formatted(String.join(", ", hosts)),
        lastError
    );
  }

  public void disconnectFromCentrifugo() {
    if (client == null) {
      return;
    }
    try {
      client.disconnect();
      client.close(CLOSE_CONNECTION_TIMEOUT_MILLIS);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException("Unable to disconnect from centrifugo", e);
    }
  }

  public void subscribe(String channelName) {
    Subscription subscription;
    try {
      subscription = client.newSubscription(channelName, subscriptionEventListener);
    } catch (DuplicateSubscriptionException e) {
      throw new RuntimeException("Unable to subscribe to channel '%s'".formatted(channelName), e);
    }
    subscribeWithWait(subscription);
  }

  public void unsubscribe(String channelName) {
    Subscription subscription = client.getSubscription(channelName);
    if (Objects.isNull(subscription)) {
      throw new RuntimeException("Unable to unsubscribe from channel '%s' because subscription was not found"
          .formatted(channelName));
    }
    client.removeSubscription(subscription);
  }

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

  public List<String> getCopiedMessages() {
    return subscriptionEventListener.getWebSocketMessages().stream()
        .map(WebSocketMessage::getMessage)
        .collect(Collectors.toList());
  }

  public void clearMessages() {
    subscriptionEventListener.getWebSocketMessages().clear();
  }

  public List<WebSocketMessage> getCopiedWebSocketMessages() {
    return List.copyOf(subscriptionEventListener.getWebSocketMessages());
  }

  public void waitUntilSubscribed(String channelName) {
    Waiter.awaitAssertion(() -> {
      String subscriptionError = subscriptionEventListener.getSubscriptionError(channelName);
      Assertions.assertThat(subscriptionError)
          .as("Subscription error for channel '%s'", channelName)
          .isNull();
      Assertions.assertThat(subscriptionEventListener.isSubscribed(channelName))
          .as("Channel '%s' is not subscribed", channelName)
          .isTrue();
    });
  }

  public void subscribeWithWait(Subscription subscription) {
    subscription.subscribe();
    waitUntilSubscribed(subscription.getChannel());
  }

  private void waitUntilConnected(String host, String endpoint) {
    Waiter.awaitAssertion(() -> {
      String connectionError = eventListener.getConnectionError();
      Assertions.assertThat(connectionError)
          .as("Unable to connect to Centrifugo at '%s%s'", host, endpoint)
          .isNull();
      Assertions.assertThat(eventListener.isConnected())
          .as("Centrifugo client is not connected at '%s%s'", host, endpoint)
          .isTrue();
    }, Timeout.SECONDS_10, PollingInterval.MILLIS_1000);
  }

  private void connect(String host, String endpoint, String token) {
    Options opts = new Options();
    opts.setToken(token);
    opts.setProxy(Proxy.NO_PROXY);
    client = new Client("%s%s".formatted(host, endpoint), opts, eventListener);
    client.connect();
  }

  private List<String> resolveConnectionHosts(String host) {
    LinkedHashSet<String> candidates = new LinkedHashSet<>();
    candidates.add(host);

    URI uri;
    try {
      uri = URI.create(host);
    } catch (IllegalArgumentException e) {
      return List.copyOf(candidates);
    }

    String scheme = uri.getScheme();
    String currentHost = uri.getHost();
    int port = uri.getPort();
    if (scheme == null || currentHost == null || port <= 0) {
      return List.copyOf(candidates);
    }

    addCandidate(candidates, scheme, currentHost, port, "localhost");
    addCandidate(candidates, scheme, currentHost, port, "127.0.0.1");
    addCandidate(candidates, scheme, currentHost, port, "dind");
    return List.copyOf(candidates);
  }

  private void addCandidate(LinkedHashSet<String> candidates, String scheme, String currentHost,
                            int port, String candidateHost) {
    if (!currentHost.equalsIgnoreCase(candidateHost)) {
      candidates.add("%s://%s:%d".formatted(scheme, candidateHost, port));
    }
  }

  private void disconnectQuietly() {
    if (client == null) {
      return;
    }
    try {
      client.disconnect();
      client.close(CLOSE_CONNECTION_TIMEOUT_MILLIS);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    } catch (RuntimeException ignored) {
      // best effort
    }
  }
}
