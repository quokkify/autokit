package io.automation.websockets.steps;

import java.util.Objects;
import java.util.function.Predicate;

import io.automation.constant.PollingInterval;
import io.automation.constant.Timeout;
import io.automation.util.JsonConverter;
import io.automation.util.Waiter;
import io.automation.websockets.entities.WebSocketChannelEntity;
import io.automation.websockets.generators.WebSocketMessageGenerator;
import io.automation.websockets.pojos.WebSocketMessageRequestPojo;
import io.automation.websockets.pojos.WebSocketMessageResponsePojo;
import io.qameta.allure.Step;
import lombok.Getter;
import org.hamcrest.Matchers;

/**
 * Class that manages WebSocket interactions, handling connection, authorization,
 * subscription, and message retrieval. Provides methods to perform actions and
 * wait for specific messages based on conditions within a WebSocket channel.
 */
@Getter
public class WebSocketClientSteps extends WebSocketStepsBase {

  private final String serverSecret;
  private final WebSocketChannelEntity channelEntity;

  /**
   * Constructs an instance with a specified WebSocket connection endpoint.
   * Connects to the server, authorizes, and subscribes to the channel.
   *
   * @param serverUrl     the URL of the WebSocket server
   * @param serverSecret  the secret for authorization
   * @param channelEntity the WebSocket channel to connect to
   */
  public WebSocketClientSteps(String serverUrl, String serverSecret, WebSocketChannelEntity channelEntity) {
    super(serverUrl);
    this.serverSecret = serverSecret;
    this.channelEntity = channelEntity;
  }

  /**
   * Connects to the server, authorizes, and subscribes to the channel.
   */
  public void startReadingChannel() {
    startReadingChannel(CENTRIFUGO_ENDPOINT);
  }

  /**
   * Connects to the server, authorizes, and subscribes to the channel.
   *
   * @param serverEndpoint the specific endpoint for WebSocket connection
   */
  public void startReadingChannel(String serverEndpoint) {
    connectToWebSocketServer(serverEndpoint);
    authorize();
    subscribe();
  }

  /**
   * Retrieves the first message that matches a specified condition,
   * with a default timeout for waiting.
   *
   * @param condition the predicate to check each message
   * @param dataClass the class of data expected in the message
   * @param <T>       the type of data in the message
   * @return the first matching message or null if none are found within the timeout
   */
  @Step("Get message with wait until appear")
  public <T> WebSocketMessageResponsePojo<T> getMessageWithWaitingUntilAppear(
      Predicate<? super WebSocketMessageResponsePojo<T>> condition,
      Class<T> dataClass) {
    return getMessageWithWaitingUntilAppear(condition, dataClass, Timeout.SECONDS_60.seconds());
  }

  /**
   * Retrieves the first message that matches a specified condition,
   * with a custom timeout.
   *
   * @param condition          the predicate to check each message
   * @param dataClass          the class of data expected in the message
   * @param waitTimeoutSeconds the timeout in seconds
   * @param <T>                the type of data in the message
   * @return the first matching message or null if none are found within the timeout
   */
  @Step("Get message with wait until appear with '{waitTimeoutSeconds}' seconds timeout")
  public <T> WebSocketMessageResponsePojo<T> getMessageWithWaitingUntilAppear(
      Predicate<? super WebSocketMessageResponsePojo<T>> condition, Class<T> dataClass, int waitTimeoutSeconds) {
    return Waiter.awaitCondition(() -> getMessageWithCondition(condition, dataClass), Matchers.notNullValue(),
        "No message in WebSocket channel '%s' with condition '%s'".formatted(channelEntity, condition),
        waitTimeoutSeconds,
        PollingInterval.MILLIS_1000.getMillis());
  }

  /**
   * Checks if any message exists that matches a specified condition within a timeout period.
   *
   * @param condition         the predicate to check each message
   * @param dataClass         the class of data expected in the message
   * @param waitTimeoutMillis the wait time in milliseconds
   * @param <T>               the type of data in the message
   * @return true if a matching message is found, otherwise false
   */
  @Step("Check message with wait until appear")
  public <T> boolean isMessageWithConditionExist(Predicate<? super WebSocketMessageResponsePojo<T>> condition,
                                                 Class<T> dataClass, int waitTimeoutMillis) {
    Waiter.threadSleep(waitTimeoutMillis);
    return Objects.nonNull(getMessageWithCondition(condition, dataClass));
  }

  private <T> WebSocketMessageResponsePojo<T> getMessageWithCondition(
      Predicate<? super WebSocketMessageResponsePojo<T>> condition, Class<T> dataClass) {
    return webSocketClientService.getCopiedMessages().stream()
        .map(message -> (WebSocketMessageResponsePojo<T>)
            JsonConverter.fromStringParametric(message, WebSocketMessageResponsePojo.class, dataClass))
        .filter(message -> Objects.nonNull(message.getBody()) && Objects.nonNull(message.getBody().getData()))
        .filter(message -> message.getBody().getChannel().equalsIgnoreCase(channelEntity.getTitle()))
        .filter(condition)
        .findFirst()
        .orElse(null);
  }

  @Override
  protected void authorize() {
    WebSocketMessageRequestPojo webSocketMessageRequestPojo =
        WebSocketMessageGenerator.generateAuthorizationMessage(serverSecret, String.valueOf(channelEntity.getUserId()));
    webSocketClientService.send(webSocketMessageRequestPojo.asJson());
  }

  private void subscribe() {
    WebSocketMessageRequestPojo webSocketMessageRequestPojo =
        WebSocketMessageGenerator.generateSubscriptionMessage(channelEntity.getTitle());
    webSocketClientService.send(webSocketMessageRequestPojo.asJson());
  }

  public void unsubscribe() {
    WebSocketMessageRequestPojo webSocketMessageRequestPojo =
        WebSocketMessageGenerator.generateUnsubscriptionMessage(channelEntity.getTitle());
    webSocketClientService.send(webSocketMessageRequestPojo.asJson());
  }
}
