package io.automation.websockets.steps;

import java.util.List;
import java.util.Objects;

import io.automation.websockets.entities.WebSocketMessage;
import io.automation.websockets.services.centrifugo.CentrifugoService;
import io.qameta.allure.Step;

public class CentrifugoClientSteps extends MessagesSteps {

  private static final ThreadLocal<CentrifugoService> CENTRIFUGO_SERVICE_THREAD_LOCAL = new ThreadLocal<>();
  private final String centrifugoUrl;
  private final String centrifugoToken;

  public CentrifugoClientSteps(String centrifugoUrl, String centrifugoToken) {
    this.centrifugoUrl = centrifugoUrl;
    this.centrifugoToken = centrifugoToken;
  }

  public CentrifugoService getCentrifugoService() {
    if (Objects.isNull(CENTRIFUGO_SERVICE_THREAD_LOCAL.get())) {
      CENTRIFUGO_SERVICE_THREAD_LOCAL.set(new CentrifugoService());
    }
    return CENTRIFUGO_SERVICE_THREAD_LOCAL.get();
  }

  /**
   * Start reading Channel.
   *
   * @param channelName the name of the channel to start reading
   */
  @Step("Start reading channel '{channelName}'")
  public void startReadingChannel(String channelName) {
    getCentrifugoService().connectToCentrifugo(centrifugoUrl, centrifugoToken);
    getCentrifugoService().subscribe(channelName);
  }

  /**
   * Start reading Historical Channel.
   *
   * @param channelName the name of the channel to start reading
   * @param offset      require message start position in history channel
   * @param epoch       the identifier of current history
   */
  @Step("Start reading Historical channel '{channelName}' with offset '{offset}' and epoch '{epoch}'")
  public void startReadingHistoricalChannel(String channelName, long offset, String epoch) {
    getCentrifugoService().connectToCentrifugo(centrifugoUrl, centrifugoToken);
    getCentrifugoService().subscribeHistorical(channelName, offset, epoch);
  }

  /**
   * Stop reading Channel.
   *
   * @param channelName the name of the channel to stop reading
   */
  @Step("Stop reading channel '{channelName}'")
  public void stopReadingChannel(String channelName) {
    getCentrifugoService().unsubscribe(channelName);
  }

  /**
   * Get messages as text.
   * This method retrieves a list of messages as text from the Centrifugo service.
   *
   * @return a list of messages in string format, which is a copy of the list of messages
   */
  @Step("Get messages as text")
  public List<String> getMessages() {
    return getCentrifugoService().getCopiedMessages();
  }

  /**
   * Clear all previously received messages.
   */
  @Step("Clear all previously received messages")
  public void clearMessages() {
    getCentrifugoService().clearMessages();
  }

  /**
   * Get messages as WebSocket messages with timestamp.
   *
   * @return a list of WebSocketMessage objects containing messages and timestamps
   */
  @Step("Get messages as WebSocket messages with timestamp")
  public List<WebSocketMessage> getWebSocketMessages() {
    return getCentrifugoService().getCopiedWebSocketMessages();
  }
}
