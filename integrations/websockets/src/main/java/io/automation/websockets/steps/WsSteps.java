package io.automation.websockets.steps;

import java.util.List;

import io.automation.websockets.client.WsClient;
import io.automation.websockets.client.WsMessage;
import io.qameta.allure.Step;

public final class WsSteps {

  private WsClient client;

  @Step("Connect to WebSocket: {url}")
  public WsSteps connect(String url) {
    client = WsClient.connect(url);
    return this;
  }

  @Step("Connect to WebSocket")
  public WsSteps connect() {
    client = WsClient.connect();
    return this;
  }

  @Step("Send WebSocket message: {message}")
  public WsSteps sendMessage(String message) {
    client.sendMessage(message);
    return this;
  }

  @Step("Clear collected WebSocket messages")
  public WsSteps clearMessages() {
    client.clearMessages();
    return this;
  }

  @Step("Disconnect from WebSocket")
  public WsSteps disconnect() {
    client.close();
    return this;
  }

  public WsVerifier verify() {
    return new WsVerifier(client);
  }

  public List<WsMessage> getMessages() {
    return client.getMessages();
  }
}
