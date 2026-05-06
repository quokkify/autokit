package io.automation.tyrus.steps;

import io.automation.tyrus.client.WsClient;

public final class WsVerifierFactory {

  private WsVerifierFactory() { }

  public static WsVerifier create(WsClient client) {
    return new WsVerifier(client);
  }
}
