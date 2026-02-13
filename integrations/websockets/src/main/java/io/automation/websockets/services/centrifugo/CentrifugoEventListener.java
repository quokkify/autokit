package io.automation.websockets.services.centrifugo;

import java.nio.charset.StandardCharsets;

import io.github.centrifugal.centrifuge.Client;
import io.github.centrifugal.centrifuge.ConnectedEvent;
import io.github.centrifugal.centrifuge.ConnectingEvent;
import io.github.centrifugal.centrifuge.DisconnectedEvent;
import io.github.centrifugal.centrifuge.ErrorEvent;
import io.github.centrifugal.centrifuge.EventListener;
import io.github.centrifugal.centrifuge.MessageEvent;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class CentrifugoEventListener extends EventListener {

  /**
   * Called when Centrifugo has successfully connected with a client.
   *
   * @param client the client object representing the connected client
   * @param event the event object containing event-specific data
   */
  @Override
  public void onConnected(Client client, ConnectedEvent event) {
    log.info("Centrifugo connected with client id %s".formatted(event.getClient()));
  }

  /**
   * Called when Centrifugo is in the process of connecting with a client.
   *
   * @param client the client object representing the current client
   * @param event  the connecting event object providing additional information
   */
  @Override
  public void onConnecting(Client client, ConnectingEvent event) {
    log.info("Centrifugo connecting: %s".formatted(event.getReason()));
  }

  /**
   * Called when Centrifugo is disconnected from a client.
   *
   * @param client the client object representing the disconnected client
   * @param event  the disconnected event object containing disconnection-specific data
   */
  @Override
  public void onDisconnected(Client client, DisconnectedEvent event) {
    log.info("Centrifugo disconnected code: '%d' reason: '%s'".formatted(event.getCode(), event.getReason()));
  }

  /**
   * Handles the event when an error occurs in the Centrifugo connection.
   *
   * @param client the client object representing the connection
   * @param event  the error event object containing details of the error
   */
  @Override
  public void onError(Client client, ErrorEvent event) {
    log.error("Centrifugo connection error: %s".formatted(event.getError().toString()));
  }

  /**
   * Processes incoming messages from Centrifugo.
   *
   * @param client the client object representing the connection
   * @param event  the message event object containing message-specific data
   */
  @Override
  public void onMessage(Client client, MessageEvent event) {
    String data = new String(event.getData(), StandardCharsets.UTF_8);
    log.debug("Centrifugo message received: %s".formatted(data));
  }
}
