package io.automation.websockets;

import java.util.UUID;
import java.util.function.Predicate;

import io.automation.generator.SignatureGenerator;
import io.automation.websockets.configs.WebSocketsConfiguration;
import io.automation.websockets.constants.WebSocketMethod;
import io.automation.websockets.entities.WebSocketChannelEntity;
import io.automation.websockets.models.TestMessage;
import io.automation.websockets.pojos.ParamsPojo;
import io.automation.websockets.pojos.WebSocketMessageRequestPojo;
import io.automation.websockets.pojos.WebSocketMessageResponsePojo;
import io.automation.websockets.steps.WebSocketClientSteps;
import io.qameta.allure.TmsLink;
import org.aeonbits.owner.ConfigFactory;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class WebsocketsTest {

  private static final WebSocketsConfiguration CONFIG = ConfigFactory.create(WebSocketsConfiguration.class);

  @TmsLink("WEBSOCKETS_ID_1")
  @Test(description = "Verify the publication of Websockets message")
  public void testWebsocketsPublishMessage() {
    String serverAddress = "%s:%d".formatted(CONFIG.webSocketsHost(), CONFIG.webSocketsPort());
    String webSocketsUrl = "ws://" + serverAddress;
    WebSocketChannelEntity webSocketChannelEntity =
        new WebSocketChannelEntity(CONFIG.webSocketsChannelName(), CONFIG.webSocketsUserId());
    WebSocketClientSteps webSocketClientSteps =
        new WebSocketClientSteps(webSocketsUrl, CONFIG.webSocketsServerSecret(), webSocketChannelEntity);
    webSocketClientSteps.startReadingChannel();
    sendMessage(serverAddress, webSocketChannelEntity, CONFIG.webSocketsMessage());
    Predicate<WebSocketMessageResponsePojo<TestMessage>> condition = message ->
        message.getBody().getData().test.equals(CONFIG.webSocketsMessage());
    WebSocketMessageResponsePojo<TestMessage> messageResponse =
        webSocketClientSteps.getMessageWithWaitingUntilAppear(condition, TestMessage.class);
    Assertions.assertThat(messageResponse.getBody().getData().test)
        .as("Invalid message response")
        .isEqualTo(CONFIG.webSocketsMessage());
  }

  private void sendMessage(String serverAddress, WebSocketChannelEntity channelEntity, String message) {
    TestMessage testMessage = TestMessage.builder()
        .test(message)
        .build();
    WebSocketMessageRequestPojo webSocketMessageRequestPojo = WebSocketMessageRequestPojo.builder()
        .uid(UUID.randomUUID().toString())
        .method(WebSocketMethod.PUBLISH.lowerCase())
        .params(ParamsPojo.builder()
            .channel(channelEntity.getTitle())
            .data(testMessage)
            .build())
        .build();
    String token =
        SignatureGenerator.generateHmacSignature(webSocketMessageRequestPojo.asJson(), CONFIG.webSocketsServerSecret());
    given()
        .header("Content-Type", "application/json")
        .header("X-API-Sign", token)
        .body(webSocketMessageRequestPojo.asJson())
        .when()
        .post("http://%s/api/".formatted(serverAddress))
        .then()
        .statusCode(200);
  }
}
