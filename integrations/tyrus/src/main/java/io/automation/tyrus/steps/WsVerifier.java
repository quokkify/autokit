package io.automation.tyrus.steps;

import io.automation.tyrus.client.WsClient;

public final class WsVerifier extends BaseWsVerification<WsVerifier> {

  WsVerifier(WsClient client) {
    super(client);
  }

  @Override
  protected WsVerifier self() {
    return this;
  }
}
