package io.automation.reportportal.test;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.Objects;

import io.automation.model.CustomPojo;
import io.automation.reportportal.config.ReportPortalConnectionConfig;
import io.qameta.allure.TmsLink;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.SkipException;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ReportPortalConnectionTest {

  private static final Logger LOG = LoggerFactory.getLogger(ReportPortalConnectionTest.class);

  private static final byte[] MINIMAL_PNG = Base64.getDecoder().decode(
      "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNkYAAAAAYAAjCB0C8AAAAASUVORK5CYII=");

  @TmsLink("RP_CONN_1")
  @Test(description = "Verify ReportPortal endpoint and token can access project list")
  public void shouldConnectToReportPortalApi() {
    skipIfNotConfigured();

    CustomPojo result = new CustomPojo(
        buildSpec()
            .queryParam("page.page", 1)
            .queryParam("page.size", 1)
            .when().get("/api/v1/project/list")
            .then().extract().asString());

    assertThat(result.json().has("content"))
        .as("Project list response should contain 'content' key")
        .isTrue();
  }

  @TmsLink("RP_LOG_1")
  @Test(description = "Verify text log can be sent to ReportPortal and returns log entry ID")
  public void shouldSendTextLogToReportPortal() {
    skipIfNotConfigured();
    String launchUuid = startTestLaunch();
    try {
      String logBody = new CustomPojo()
          .setField("launchUuid", launchUuid)
          .setField("time", Instant.now().toString())
          .setField("level", "INFO")
          .setField("message", "Integration test: text log verification")
          .asJsonArray();

      JsonPath jsonPath = buildSpec()
          .body(logBody)
          .when().post("/api/v1/" + ReportPortalConnectionConfig.PROJECT_NAME + "/log")
          .then().extract().jsonPath();

      assertThat(jsonPath.getString("[0].id"))
          .as("First log entry should contain an ID")
          .isNotBlank();
    } finally {
      finishTestLaunch(launchUuid);
    }
  }

  @TmsLink("RP_LOG_2")
  @Test(description = "Verify text file attachment can be sent to ReportPortal")
  public void shouldAttachTxtFileToReportPortal() {
    skipIfNotConfigured();
    String launchUuid = startTestLaunch();
    try {
      int statusCode = sendMultipartLog(launchUuid,
          "Integration test: file attachment",
          "Integration test file attachment content.\n".getBytes(StandardCharsets.UTF_8),
          "test-attachment.txt", "text/plain");

      assertThat(statusCode)
          .as("File attachment should return 2xx")
          .isBetween(200, 299);
    } finally {
      finishTestLaunch(launchUuid);
    }
  }

  @TmsLink("RP_LOG_3")
  @Test(description = "Verify screenshot (PNG) attachment can be sent to ReportPortal")
  public void shouldAttachPngScreenshotToReportPortal() {
    skipIfNotConfigured();
    String launchUuid = startTestLaunch();
    try {
      int statusCode = sendMultipartLog(launchUuid,
          "Integration test: screenshot attachment",
          MINIMAL_PNG, "screenshot.png", "image/png");

      assertThat(statusCode)
          .as("Screenshot attachment should return 2xx")
          .isBetween(200, 299);
    } finally {
      finishTestLaunch(launchUuid);
    }
  }

  @TmsLink("RP_NEG_1")
  @Test(description = "Verify ReportPortal rejects requests with an invalid API token")
  public void shouldRejectRequestWithInvalidToken() {
    if (isBlank(ReportPortalConnectionConfig.ENDPOINT)) {
      throw new SkipException("ReportPortal endpoint is not configured. Run infra reporting profile first.");
    }

    int statusCode = RestAssured.given()
        .baseUri(ReportPortalConnectionConfig.ENDPOINT)
        .contentType(ContentType.JSON)
        .header("Authorization", "Bearer INVALID_TOKEN_VALUE_XYZ")
        .queryParam("page.page", 1)
        .queryParam("page.size", 1)
        .when().get("/api/v1/project/list")
        .then().extract().statusCode();

    assertThat(statusCode)
        .as("Invalid token should be rejected with 401 or 403")
        .isIn(401, 403);
  }

  @TmsLink("RP_NEG_2")
  @Test(description = "Verify connection failure is raised for an unreachable ReportPortal endpoint")
  public void shouldRaiseErrorForUnreachableEndpoint() {
    assertThatThrownBy(() ->
        RestAssured.given()
            .baseUri("http://localhost:19999")
            .when().get("/api/v1/project/list"))
        .as("Expected an exception for an unreachable endpoint")
        .isInstanceOf(Exception.class);
  }

  private static void skipIfNotConfigured() {
    if (isBlank(ReportPortalConnectionConfig.ENDPOINT) || isBlank(ReportPortalConnectionConfig.API_KEY)) {
      throw new SkipException("ReportPortal env is not configured. Run infra reporting profile first.");
    }
  }

  private static RequestSpecification buildSpec() {
    return RestAssured.given()
        .baseUri(ReportPortalConnectionConfig.ENDPOINT)
        .contentType(ContentType.JSON)
        .header("Authorization", "Bearer " + ReportPortalConnectionConfig.API_KEY);
  }

  private static String startTestLaunch() {
    String body = new CustomPojo()
        .setField("name", "test-coverage-run")
        .setField("startTime", Instant.now().toString())
        .setField("mode", "DEBUG")
        .asJson();

    CustomPojo response = new CustomPojo(
        buildSpec()
            .body(body)
            .when().post("/api/v1/" + ReportPortalConnectionConfig.PROJECT_NAME + "/launch")
            .then().extract().asString());

    if (!response.json().has("id")) {
      throw new SkipException(
          "Cannot start a test launch on ReportPortal (project may not exist or mode is not allowed). "
              + "Response: " + response.asJson());
    }
    return response.json().get("id").asText();
  }

  private static void finishTestLaunch(String launchUuid) {
    try {
      buildSpec()
          .body(new CustomPojo()
              .setField("endTime", Instant.now().toString())
              .setField("status", "PASSED")
              .asJson())
          .when().put("/api/v1/" + ReportPortalConnectionConfig.PROJECT_NAME + "/launch/" + launchUuid + "/finish");
    } catch (Exception e) {
      LOG.debug("Failed to finish test launch {}: {}", launchUuid, e.getMessage());
    }
  }

  private static int sendMultipartLog(String launchUuid, String message,
      byte[] fileBytes, String fileName, String fileContentType) {
    String jsonPart = new CustomPojo()
        .setField("launchUuid", launchUuid)
        .setField("time", Instant.now().toString())
        .setField("level", "INFO")
        .setField("message", message)
        .asJsonArray();

    return RestAssured.given()
        .baseUri(ReportPortalConnectionConfig.ENDPOINT)
        .header("Authorization", "Bearer " + ReportPortalConnectionConfig.API_KEY)
        .multiPart("json_request_part", jsonPart, "application/json")
        .multiPart("file", fileName, fileBytes, fileContentType)
        .when().post("/api/v1/" + ReportPortalConnectionConfig.PROJECT_NAME + "/log")
        .then().extract().statusCode();
  }

  private static boolean isBlank(String value) {
    return Objects.isNull(value) || value.trim().isEmpty();
  }
}
