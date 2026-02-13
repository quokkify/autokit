package io.automation.websockets.steps;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import io.automation.util.Waiter;
import io.automation.websockets.entities.WebSocketMessage;
import io.automation.websockets.services.WebSocketClientService;
import io.qameta.allure.Step;
import lombok.Getter;
import lombok.SneakyThrows;

/**
 * Class that manages WebSocket interactions, handling connection, authorization,
 * subscription, and message retrieval. Provides methods to perform actions and
 * wait for specific messages based on conditions within a WebSocket channel.
 */
@Getter
public abstract class WebSocketStepsBase {

  protected static final String CENTRIFUGO_ENDPOINT = "/connection/websocket";
  protected static final String SOCKET_ENDPOINT = "/socket";
  protected final String serverUrl;
  protected WebSocketClientService webSocketClientService;

  protected WebSocketStepsBase(String serverUrl) {
    this.serverUrl = getWebSocketServerUrl(serverUrl);
  }

  protected abstract void authorize();

  /**
   * Clears all stored messages in the WebSocket client.
   */
  @Step("Clear all messages")
  public void clearMessages() {
    webSocketClientService.clearMessages();
  }

  /**
   * Retrieves all messages with a specified wait time in milliseconds.
   *
   * @param waitMillis the wait time in milliseconds before retrieving messages
   * @return a list of messages received during the wait
   */
  @Step("Get messages with '{waitMillis}' milliseconds wait")
  public List<String> getMessagesWithWait(int waitMillis) {
    Waiter.threadSleep(waitMillis);
    return getMessages();
  }

  /**
   * Retrieves all messages without a specified wait time.
   *
   * @return a list of messages received during the wait
   */
  @Step("Get messages")
  public List<String> getMessages() {
    return webSocketClientService.getCopiedMessages();
  }

  /**
   * Retrieves all messages without a specified wait time.
   *
   * @return a list of WebSocket messages with Timestamp
   */
  @Step("Get WebSocket messages with Timestamp")
  public List<WebSocketMessage> getWebSocketMessages() {
    return webSocketClientService.getCopiedWebSocketMessages();
  }

  @SneakyThrows({URISyntaxException.class, InterruptedException.class})
  protected void connectToWebSocketServer(String serverEndpoint) {
    webSocketClientService = new WebSocketClientService(new URI("%s%s".formatted(serverUrl, serverEndpoint)));
    if (!webSocketClientService.connectBlocking()) {
      throw new RuntimeException("Failed connect to the WebSocket server: %s".formatted(serverUrl));
    }
  }

  protected String getHttpServerUrl(String serverUrl) {
    return serverUrl.replaceAll("^ws", "http");
  }

  private String getWebSocketServerUrl(String serverUrl) {
    return serverUrl.replace("http", "ws");
  }
}
