package io.automation.reportportal.test;

import java.io.IOException;
import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import feign.Feign;
import feign.FeignException;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import io.automation.reportportal.config.ReportPortalConnectionConfig;
import io.qameta.allure.TmsLink;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.testng.SkipException;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class ReportPortalConnectionTest {

  private static final byte[] MINIMAL_PNG = Base64.getDecoder().decode(
      "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNkYAAAAAYAAjCB0C8AAAAASUVORK5CYII=");

  @TmsLink("RP_CONN_1")
  @Test(description = "Verify ReportPortal endpoint and token can access project list")
  public void shouldConnectToReportPortalApi() {
    skipIfNotConfigured();
    String endpoint = ReportPortalConnectionConfig.ENDPOINT;
    String token = ReportPortalConnectionConfig.API_KEY;

    Map<String, Object> result = buildApi(endpoint, token).getProjectList();

    assertThat(result)
        .as("Project list response should contain 'content' key")
        .containsKey("content");
  }

  @TmsLink("RP_LOG_1")
  @Test(description = "Verify text log can be sent to ReportPortal and returns log entry ID")
  public void shouldSendTextLogToReportPortal() throws Exception {
    skipIfNotConfigured();
    String endpoint = ReportPortalConnectionConfig.ENDPOINT;
    String token = ReportPortalConnectionConfig.API_KEY;
    String project = ReportPortalConnectionConfig.PROJECT_NAME;

    ReportPortalApi api = buildApi(endpoint, token);
    String launchUuid = startTestLaunch(api, project);
    try {
      List<Map<String, Object>> response = api.createTextLog(project,
          List.of(Map.of(
              "launchUuid", launchUuid,
              "time", Instant.now().toString(),
              "level", "INFO",
              "message", "Integration test: text log verification")));

      assertThat(response)
          .as("Log creation response should not be empty")
          .isNotEmpty();
      assertThat(response.get(0))
          .as("First log entry should contain an ID")
          .containsKey("id");
    } finally {
      finishTestLaunch(api, project, launchUuid);
    }
  }

  @TmsLink("RP_LOG_2")
  @Test(description = "Verify text file attachment can be sent to ReportPortal")
  public void shouldAttachTxtFileToReportPortal() throws Exception {
    skipIfNotConfigured();
    String endpoint = ReportPortalConnectionConfig.ENDPOINT;
    String token = ReportPortalConnectionConfig.API_KEY;
    String project = ReportPortalConnectionConfig.PROJECT_NAME;

    ReportPortalApi api = buildApi(endpoint, token);
    String launchUuid = startTestLaunch(api, project);
    try {
      byte[] fileContent = "Integration test file attachment content.\n".getBytes();
      int statusCode = sendMultipartLog(endpoint, token, project, launchUuid,
          "Integration test: file attachment", fileContent,
          "test-attachment.txt", "text/plain");

      assertThat(statusCode)
          .as("File attachment should return 2xx")
          .isBetween(200, 299);
    } finally {
      finishTestLaunch(api, project, launchUuid);
    }
  }

  @TmsLink("RP_LOG_3")
  @Test(description = "Verify screenshot (PNG) attachment can be sent to ReportPortal")
  public void shouldAttachPngScreenshotToReportPortal() throws Exception {
    skipIfNotConfigured();
    String endpoint = ReportPortalConnectionConfig.ENDPOINT;
    String token = ReportPortalConnectionConfig.API_KEY;
    String project = ReportPortalConnectionConfig.PROJECT_NAME;

    ReportPortalApi api = buildApi(endpoint, token);
    String launchUuid = startTestLaunch(api, project);
    try {
      int statusCode = sendMultipartLog(endpoint, token, project, launchUuid,
          "Integration test: screenshot attachment", MINIMAL_PNG,
          "screenshot.png", "image/png");

      assertThat(statusCode)
          .as("Screenshot attachment should return 2xx")
          .isBetween(200, 299);
    } finally {
      finishTestLaunch(api, project, launchUuid);
    }
  }

  @TmsLink("RP_NEG_1")
  @Test(description = "Verify ReportPortal rejects requests with an invalid API token")
  public void shouldRejectRequestWithInvalidToken() {
    String endpoint = ReportPortalConnectionConfig.ENDPOINT;
    if (isBlank(endpoint)) {
      throw new SkipException("ReportPortal endpoint is not configured. Run infra reporting profile first.");
    }

    ReportPortalApi api = buildApi(endpoint, "INVALID_TOKEN_VALUE_XYZ");
    try {
      api.getProjectList();
      fail("Expected FeignException for invalid token");
    } catch (FeignException e) {
      assertThat(e.status())
          .as("Invalid token should be rejected with 401 or 403")
          .isIn(401, 403);
    }
  }

  @TmsLink("RP_NEG_2")
  @Test(description = "Verify connection failure is raised for an unreachable ReportPortal endpoint")
  public void shouldRaiseErrorForUnreachableEndpoint() {
    ReportPortalApi api = buildApi("http://localhost:19999", "test-token");
    try {
      api.getProjectList();
      fail("Expected an exception for an unreachable endpoint");
    } catch (FeignException e) {
      assertThat(e).isNotNull();
    }
  }

  private static void skipIfNotConfigured() {
    String endpoint = ReportPortalConnectionConfig.ENDPOINT;
    String token = ReportPortalConnectionConfig.API_KEY;
    if (isBlank(endpoint) || isBlank(token)) {
      throw new SkipException("ReportPortal env is not configured. Run infra reporting profile first.");
    }
  }

  private static ReportPortalApi buildApi(String endpoint, String token) {
    return Feign.builder()
        .client(new OkHttpClient())
        .encoder(new JacksonEncoder())
        .decoder(new JacksonDecoder())
        .requestInterceptor(t -> t.header("Authorization", "Bearer " + token))
        .target(ReportPortalApi.class, endpoint);
  }

  private static String startTestLaunch(ReportPortalApi api, String project) {
    Map<String, Object> body = Map.of(
        "name", "test-coverage-run",
        "startTime", Instant.now().toString(),
        "mode", "DEBUG");
    Map<String, Object> response = api.startLaunch(project, body);
    assertThat(response)
        .as("Start launch response should contain 'id'")
        .containsKey("id");
    return (String) response.get("id");
  }

  private static void finishTestLaunch(ReportPortalApi api, String project, String launchUuid) {
    try {
      api.finishLaunch(project, launchUuid, Map.of(
          "endTime", Instant.now().toString(),
          "status", "PASSED"));
    } catch (Exception ignored) {
    }
  }

  private static int sendMultipartLog(String endpoint, String token, String project,
      String launchUuid, String message, byte[] fileBytes, String fileName, String fileContentType)
      throws IOException {
    String jsonPart = "[{\"launchUuid\":\"%s\",\"time\":\"%s\",\"level\":\"INFO\",\"message\":\"%s\"}]"
        .formatted(launchUuid, Instant.now(), message);

    RequestBody multipart = new MultipartBody.Builder()
        .setType(MultipartBody.FORM)
        .addFormDataPart("json_request_part", null,
            RequestBody.create(jsonPart.getBytes(), MediaType.parse("application/json")))
        .addFormDataPart("file", fileName,
            RequestBody.create(fileBytes, MediaType.parse(fileContentType)))
        .build();

    okhttp3.OkHttpClient rawClient = new okhttp3.OkHttpClient();
    okhttp3.Request request = new Request.Builder()
        .url(endpoint + "/api/v1/" + project + "/log")
        .header("Authorization", "Bearer " + token)
        .post(multipart)
        .build();

    try (okhttp3.Response rawResponse = rawClient.newCall(request).execute()) {
      return rawResponse.code();
    }
  }

  private static boolean isBlank(String value) {
    return value == null || value.trim().isEmpty();
  }

  interface ReportPortalApi {

    @RequestLine("GET /api/v1/project/list?page.page=1&page.size=1")
    @Headers("Accept: application/json")
    Map<String, Object> getProjectList();

    @RequestLine("POST /api/v1/{project}/launch")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Map<String, Object> startLaunch(@Param("project") String project, Map<String, Object> body);

    @RequestLine("PUT /api/v1/{project}/launch/{launchUuid}/finish")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    void finishLaunch(@Param("project") String project,
        @Param("launchUuid") String launchUuid,
        Map<String, Object> body);

    @RequestLine("POST /api/v1/{project}/log")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    List<Map<String, Object>> createTextLog(@Param("project") String project,
        List<Map<String, Object>> body);
  }
}
