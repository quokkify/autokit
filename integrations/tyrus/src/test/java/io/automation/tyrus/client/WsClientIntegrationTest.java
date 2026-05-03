package io.automation.tyrus.client;

import io.automation.constant.PollingInterval;
import io.automation.constant.Timeout;
import io.automation.tyrus.server.EchoWebSocketServer;
import io.automation.tyrus.steps.WsSteps;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class WsClientIntegrationTest {

  private static final String URL = "ws://localhost:8787";

  private EchoWebSocketServer server;
  private WsSteps wsSteps;

  @BeforeClass
  public void startServer() throws InterruptedException {
    server = new EchoWebSocketServer(8787);
    server.start();
    Thread.sleep(300);
    wsSteps = new WsSteps();
    wsSteps.connect(URL);
  }

  @AfterClass
  public void stopServer() throws InterruptedException {
    wsSteps.disconnect();
    if (server != null) {
      server.stop();
    }
  }

  @Test
  public void connect_receivesEchoedMessage() {
    wsSteps.clearMessages()
        .sendMessage("hello")
        .verify()
        .withTimeout(Timeout.SECONDS_5)
        .withPolling(PollingInterval.MILLIS_100)
        .containsMessage("hello");
  }

  @Test
  public void sendJson_verifyJsonField() {
    wsSteps.clearMessages()
        .sendMessage("{\"type\":\"order_created\",\"orderId\":\"42\"}")
        .verify()
        .hasJsonField("type", "order_created")
        .hasJsonField("orderId", "42");
  }

  @Test
  public void multipleMessages_verifiedInOrder() {
    wsSteps.clearMessages()
        .sendMessage("step one")
        .sendMessage("step two")
        .sendMessage("step three")
        .verify()
        .messagesInOrder("step one", "step two", "step three");
  }

  @Test
  public void absence_assertedCorrectly() {
    wsSteps.clearMessages()
        .verify()
        .doesNotContainMessage("ghost message");
  }
}
