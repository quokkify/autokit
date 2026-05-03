package io.automation.tyrus.client;

import io.automation.constant.PollingInterval;
import io.automation.constant.Timeout;
import io.automation.tyrus.steps.WsVerifier;
import io.automation.tyrus.steps.WsVerifierFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class WsClientTest {

  private WsSimulator simulator;
  private WsClient client;
  private WsVerifier verifier;

  @BeforeMethod
  public void setUp() {
    simulator = WsSimulator.create();
    client = simulator.asClient();
    verifier = WsVerifierFactory.create(client)
        .withTimeout(Timeout.SECONDS_5)
        .withPolling(PollingInterval.MILLIS_100);
  }

  @AfterMethod
  public void tearDown() {
    simulator.clear();
  }

  @Test
  public void messageCollector_storesMessages() {
    simulator.send("hello world").send("second message");

    Assert.assertEquals(client.getMessages().size(), 2);
    Assert.assertEquals(client.getMessages().get(0).payload(), "hello world");
    Assert.assertEquals(client.getMessages().get(1).payload(), "second message");
  }

  @Test
  public void clearMessages_emptiesQueue() {
    simulator.send("to be cleared");

    client.clearMessages();

    Assert.assertTrue(client.getMessages().isEmpty());
  }

  @Test
  public void verifier_containsMessage_passes() {
    simulator.send("order status updated");

    verifier.containsMessage("order status");
  }

  @Test
  public void verifier_containsMessage_byPredicate_passes() {
    simulator.send("payment confirmed");

    verifier.containsMessage(msg -> msg.payload().startsWith("payment"));
  }

  @Test
  public void verifier_doesNotContainMessage_passes() {
    simulator.send("expected message");

    WsVerifierFactory.create(client)
        .withTimeout(Timeout.SECONDS_3)
        .withPolling(PollingInterval.MILLIS_100)
        .doesNotContainMessage("absent substring");
  }

  @Test
  public void verifier_hasJsonField_passes() {
    simulator.send("{\"status\":\"active\",\"userId\":\"42\"}");

    verifier.hasJsonField("status", "active");
  }

  @Test
  public void verifier_hasMessageCount_passes() {
    simulator.send("first").send("second").send("third");

    verifier.hasMessageCount(3);
  }

  @Test
  public void verifier_messagesInOrder_passes() {
    simulator.send("step one complete")
        .send("step two complete")
        .send("step three complete");

    verifier.messagesInOrder("step one", "step two", "step three");
  }

  @Test
  public void verifier_containsMessage_withDelay_passes() {
    simulator.sendAfterDelay("delayed arrival", 200);

    verifier.containsMessage("delayed arrival");
  }
}
