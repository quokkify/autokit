package io.automation.websockets;

import io.automation.websockets.models.TestMessage;
import io.automation.websockets.pojos.CentrifugoMessagePublishPojo;

import static io.restassured.RestAssured.given;

public class CentrifugoApiSteps {

  private  final String serverAddress;
  private  final String apiKey;

  public CentrifugoApiSteps(String serverAddress, String apiKey) {
    this.serverAddress = serverAddress;
    this.apiKey = apiKey;
  }

  public void sendMessage(String channelName, String message) {
    CentrifugoMessagePublishPojo centrifugoMessagePublishPojo =
        new CentrifugoMessagePublishPojo(channelName, new TestMessage(message));
    given()
        .header("Content-Type", "application/json")
        .header("X-API-Key", apiKey)
        .body(centrifugoMessagePublishPojo.asJson())
        .when()
        .post("http://%s/api/publish".formatted(serverAddress))
        .then()
        .statusCode(200);
  }
}
