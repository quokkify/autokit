package io.automation.websockets.steps;

import io.automation.websockets.client.WsClient;

public final class WsVerifierFactory {

  private WsVerifierFactory() {
  }

  public static WsVerifier create(WsClient client) {
    return new WsVerifier(client);
  }
}
