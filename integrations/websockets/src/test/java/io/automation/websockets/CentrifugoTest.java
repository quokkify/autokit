package io.automation.websockets;

import java.util.function.Predicate;

import io.automation.websockets.configs.WebSocketsConfiguration;
import io.automation.websockets.models.TestMessage;
import io.automation.websockets.steps.CentrifugoClientSteps;
import io.qameta.allure.TmsLink;
import org.aeonbits.owner.ConfigFactory;
import org.testng.annotations.Test;

public class CentrifugoTest {

  private static final WebSocketsConfiguration CONFIG = ConfigFactory.create(WebSocketsConfiguration.class);

  @TmsLink("CENTRIFUGO_ID_1")
  @Test(description = "Verify the publication of Centrifugo message")
  public void testCentrifugoPublishMessage() {
    String serverAddress = "%s:%d".formatted(CONFIG.centrifugoHost(), CONFIG.centrifugoPort());
    String centrifugoUrl = "ws://" + serverAddress;
    CentrifugoApiSteps centrifugoApiSteps = new CentrifugoApiSteps(serverAddress, CONFIG.centrifugoApiKey());
    String jwtToken = JwtGenerator.getJwtToken(
        CONFIG.centrifugoKeysPath(),
        CONFIG.centrifugoChannelName(),
        CONFIG.centrifugoUserId());
    CentrifugoClientSteps centrifugoClientSteps = new CentrifugoClientSteps(centrifugoUrl, jwtToken);
    centrifugoClientSteps.startReadingChannel(CONFIG.centrifugoChannelName());
    centrifugoApiSteps.sendMessage(CONFIG.centrifugoChannelName(), CONFIG.centrifugoMessage());
    Predicate<TestMessage> condition = message -> message.getTest().equals(CONFIG.centrifugoMessage());
    centrifugoClientSteps.getMessageWithWaitingUntilAppear(condition, TestMessage.class);
  }
}
