package io.automation.websockets.services.centrifugo;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import io.github.centrifugal.centrifuge.Client;
import io.github.centrifugal.centrifuge.ConnectedEvent;
import io.github.centrifugal.centrifuge.ConnectingEvent;
import io.github.centrifugal.centrifuge.DisconnectedEvent;
import io.github.centrifugal.centrifuge.ErrorEvent;
import io.github.centrifugal.centrifuge.EventListener;
import io.github.centrifugal.centrifuge.MessageEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CentrifugoEventListener extends EventListener {

  private static final Logger LOG = LogManager.getLogger(CentrifugoEventListener.class);
  private final AtomicBoolean connected = new AtomicBoolean(false);
  private final AtomicReference<String> connectionError = new AtomicReference<>();

  public boolean isConnected() {
    return connected.get();
  }

  public String getConnectionError() {
    return connectionError.get();
  }

  /**
   * Called when Centrifugo has successfully connected with a client.
   */
  @Override
  public void onConnected(Client client, ConnectedEvent event) {
    connected.set(true);
    connectionError.set(null);
    LOG.info("Centrifugo connected with client id {}", event.getClient());
  }

  /**
   * Called when Centrifugo is in the process of connecting with a client.
   */
  @Override
  public void onConnecting(Client client, ConnectingEvent event) {
    LOG.info("Centrifugo connecting: {}", event.getReason());
  }

  /**
   * Called when Centrifugo is disconnected from a client.
   */
  @Override
  public void onDisconnected(Client client, DisconnectedEvent event) {
    connected.set(false);
    LOG.info("Centrifugo disconnected code: '{}' reason: '{}'", event.getCode(), event.getReason());
  }

  /**
   * Handles the event when an error occurs in the Centrifugo connection.
   */
  @Override
  public void onError(Client client, ErrorEvent event) {
    connected.set(false);
    connectionError.set(event.getError().toString());
    LOG.error("Centrifugo connection error: {}", event.getError().toString());
  }

  /**
   * Processes incoming messages from Centrifugo.
   */
  @Override
  public void onMessage(Client client, MessageEvent event) {
    String data = new String(event.getData(), StandardCharsets.UTF_8);
    LOG.debug("Centrifugo message received: {}", data);
  }
}
