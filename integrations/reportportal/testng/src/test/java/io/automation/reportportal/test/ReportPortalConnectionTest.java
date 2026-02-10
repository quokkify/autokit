package io.automation.reportportal.test;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import feign.Feign;
import feign.Headers;
import feign.Request;
import feign.RequestLine;
import feign.Response;
import feign.Util;
import io.automation.reportportal.config.ReportPortalConnectionConfig;
import io.qameta.allure.TmsLink;
import org.testng.SkipException;
import org.testng.annotations.Test;

public class ReportPortalConnectionTest {

  @TmsLink("RP_CONN_1")
  @Test(description = "Verify ReportPortal endpoint and token can access project list")
  public void shouldConnectToReportPortalApi() throws IOException {
    String endpoint = ReportPortalConnectionConfig.ENDPOINT;
    String token = ReportPortalConnectionConfig.API_KEY;

    if (isBlank(endpoint) || isBlank(token)) {
      throw new SkipException("ReportPortal env is not configured. Run infra reporting profile first.");
    }

    ReportPortalApi api = Feign.builder()
        .options(new Request.Options(15_000, 30_000))
        .requestInterceptor(template -> template.header("Authorization", "Bearer " + token))
        .target(ReportPortalApi.class, endpoint);

    Response response = api.getProjectList();
    String responseBody = readBody(response);
    if (response.status() != 200) {
      throw new AssertionError("Unexpected status code: " + response.status() + ", body: " + responseBody);
    }
    if (isBlank(responseBody) || !responseBody.contains("content")) {
      throw new AssertionError("Unexpected API response body: " + responseBody);
    }
  }

  private static boolean isBlank(String value) {
    return value == null || value.trim().isEmpty();
  }

  private static String readBody(Response response) throws IOException {
    if (response.body() == null) {
      return "";
    }
    try (Reader reader = response.body().asReader(StandardCharsets.UTF_8)) {
      return Util.toString(reader);
    }
  }

  interface ReportPortalApi {
    @RequestLine("GET /api/v1/project/list?page.page=1&page.size=1")
    @Headers("Accept: application/json")
    Response getProjectList();
  }
}
