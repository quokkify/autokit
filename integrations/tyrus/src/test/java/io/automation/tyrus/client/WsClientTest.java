package io.automation.tyrus.client;

import java.time.Instant;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import io.automation.constant.PollingInterval;
import io.automation.constant.Timeout;
import io.automation.tyrus.steps.WsVerifier;
import io.automation.tyrus.steps.WsVerifierFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class WsClientTest {

  private Queue<WsMessage> queue;
  private WsClient client;
  private WsVerifier verifier;

  @BeforeMethod
  public void setUp() {
    queue = new ConcurrentLinkedQueue<>();
    client = new WsClient(queue);
    verifier = WsVerifierFactory.create(client)
        .withTimeout(Timeout.SECONDS_5)
        .withPolling(PollingInterval.MILLIS_100);
  }

  @AfterMethod
  public void tearDown() {
    queue.clear();
  }

  @Test
  public void messageCollector_storesMessages() {
    queue.add(new WsMessage("hello world", Instant.now()));
    queue.add(new WsMessage("second message", Instant.now()));

    Assert.assertEquals(client.getMessages().size(), 2);
    Assert.assertEquals(client.getMessages().get(0).payload(), "hello world");
    Assert.assertEquals(client.getMessages().get(1).payload(), "second message");
  }

  @Test
  public void clearMessages_emptiesQueue() {
    queue.add(new WsMessage("to be cleared", Instant.now()));

    client.clearMessages();

    Assert.assertTrue(client.getMessages().isEmpty());
  }

  @Test
  public void verifier_containsMessage_passes() {
    queue.add(new WsMessage("order status updated", Instant.now()));

    verifier.containsMessage("order status");
  }

  @Test
  public void verifier_containsMessage_byPredicate_passes() {
    queue.add(new WsMessage("payment confirmed", Instant.now()));

    verifier.containsMessage(msg -> msg.payload().startsWith("payment"));
  }

  @Test
  public void verifier_doesNotContainMessage_passes() {
    queue.add(new WsMessage("expected message", Instant.now()));

    WsVerifierFactory.create(client)
        .withTimeout(Timeout.SECONDS_3)
        .withPolling(PollingInterval.MILLIS_100)
        .doesNotContainMessage("absent substring");
  }

  @Test
  public void verifier_hasJsonField_passes() {
    queue.add(new WsMessage("{\"status\":\"active\",\"userId\":\"42\"}", Instant.now()));

    verifier.hasJsonField("status", "active");
  }

  @Test
  public void verifier_hasMessageCount_passes() {
    queue.add(new WsMessage("first", Instant.now()));
    queue.add(new WsMessage("second", Instant.now()));
    queue.add(new WsMessage("third", Instant.now()));

    verifier.hasMessageCount(3);
  }

  @Test
  public void verifier_messagesInOrder_passes() {
    queue.add(new WsMessage("step one complete", Instant.now()));
    queue.add(new WsMessage("step two complete", Instant.now()));
    queue.add(new WsMessage("step three complete", Instant.now()));

    verifier.messagesInOrder("step one", "step two", "step three");
  }

  @Test
  public void verifier_containsMessage_withDelay_passes() {
    Thread producer = new Thread(() -> {
      try {
        Thread.sleep(200);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        return;
      }
      queue.add(new WsMessage("delayed arrival", Instant.now()));
    });
    producer.setDaemon(true);
    producer.start();

    verifier.containsMessage("delayed arrival");
  }
}
