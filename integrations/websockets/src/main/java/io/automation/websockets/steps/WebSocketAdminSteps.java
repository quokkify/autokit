package io.automation.websockets.steps;

import io.automation.websockets.generators.WebSocketMessageGenerator;
import io.automation.websockets.pojos.WebSocketMessageRequestPojo;
import lombok.Getter;

import static io.restassured.RestAssured.given;

/**
 * Class that manages WebSocket interactions, handling connection, authorization,
 * subscription, and message retrieval. Provides methods to perform actions and
 * wait for specific messages based on conditions within a WebSocket channel.
 */
@Getter
public class WebSocketAdminSteps extends WebSocketStepsBase {

  private final String adminPassword;

  /**
   * Constructs an instance with a specified WebSocket connection endpoint.
   *
   * @param serverUrl     the URL of the WebSocket server
   * @param adminPassword the secret for authorization
   */
  public WebSocketAdminSteps(String serverUrl, String adminPassword) {
    super(serverUrl);
    this.adminPassword = adminPassword;
  }

  /**
   * Connects to the server, authorizes, and watch to all channels.
   */
  public void startReadingAllChannels() {
    startReadingAllChannels(SOCKET_ENDPOINT);
  }

  /**
   * Connects to the server, authorizes, and watch to all channels.
   *
   * @param serverEndpoint the specific endpoint for WebSocket connection
   */
  public void startReadingAllChannels(String serverEndpoint) {
    connectToWebSocketServer(serverEndpoint);
    authorize();
  }

  @Override
  protected void authorize() {
    String token = given()
        .formParam("password", adminPassword)
        .post(getHttpServerUrl(serverUrl) + "/auth/")
        .then()
        .statusCode(200)
        .extract().jsonPath()
        .getString("token");
    WebSocketMessageRequestPojo webSocketMessageRequestPojo =
        WebSocketMessageGenerator.generateAdminAuthorizationMessage(token);
    webSocketClientService.send(webSocketMessageRequestPojo.asJson());
  }
}
