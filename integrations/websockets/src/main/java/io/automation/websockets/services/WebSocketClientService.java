package io.automation.websockets.services;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import io.automation.generator.LocalDateTimeGenerator;
import io.automation.websockets.entities.WebSocketMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

/**
 * Service class that extends {@link WebSocketClient} to manage WebSocket connections
 * and handle messages received from the server. This class logs connection events,
 * stores received messages, and provides thread-safe access to the message history.
 *
 * @see WebSocketClient
 */
public class WebSocketClientService extends WebSocketClient {

  private static final Logger LOG = LogManager.getLogger(WebSocketClientService.class);
  private final List<WebSocketMessage> webSocketMessages = new ArrayList<>();

  /**
   * Constructs a {@code WebSocketClientService} instance with the specified server URI.
   *
   * @param serverUri the URI of the WebSocket server to connect to
   */
  public WebSocketClientService(URI serverUri) {
    super(serverUri);
  }

  /**
   * Get messages as String collection.
   *
   * @return copy of the list of messages created at the moment of calling this method
   */
  public List<String> getCopiedMessages() {
    return webSocketMessages.stream()
        .map(WebSocketMessage::getMessage)
        .collect(Collectors.toList());
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
    return List.copyOf(webSocketMessages);
  }

  /**
   * Clears the list of stored messages.
   * This method can be used to reset the message history.
   */
  public void clearMessages() {
    webSocketMessages.clear();
  }

  @Override
  public void onOpen(ServerHandshake handshakeData) {
    LOG.debug("Success connection to WebSockets server");
  }

  @Override
  public void onMessage(String message) {
    LOG.debug("WS message: {}", message);
    webSocketMessages.add(new WebSocketMessage().setTimestamp(LocalDateTimeGenerator.generateNow())
        .setMessage(message));
  }

  @Override
  public void onClose(int code, String reason, boolean isRemote) {
    LOG.debug("WebSockets server connection is closed by '{}' side, Code: '{}', Reason: '{}'",
        isRemote ? "remote" : "local", code, reason);
  }

  @Override
  public void onError(Exception exception) {
    LOG.error("Error: ", exception);
  }
}
