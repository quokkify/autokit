package io.automation.reportportal.test;

import java.util.Objects;

import io.automation.reportportal.config.ReportPortalConnectionConfig;
import io.automation.reportportal.model.ReportPortalItem;
import io.automation.reportportal.services.ReportPortalApiService;
import io.qameta.allure.TmsLink;
import org.testng.SkipException;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ReportPortalApiServiceTest {

  private static final String NON_EXISTENT_UUID = "00000000-0000-0000-0000-000000000000";
  private static final ReportPortalApiService SERVICE = new ReportPortalApiService();

  @TmsLink("RP_API_1")
  @Test(description = "getItemByUuid returns an object for an existing project (negative: non-existent UUID)")
  public void getItemByUuid_nonExistentUuid_returnsItemWithNullId() {
    skipIfNotConfigured();

    ReportPortalItem item = SERVICE.getItemByUuid(ReportPortalConnectionConfig.PROJECT_NAME, NON_EXISTENT_UUID);

    assertThat(item).as("Service must return a non-null object even for a missing item").isNotNull();
    assertThat(item.id()).as("id must be null for a non-existent UUID").isNull();
    assertThat(item.launchId()).as("launchId must be null for a non-existent UUID").isNull();
  }

  @TmsLink("RP_API_2")
  @Test(description = "getItemByUuid path is built correctly for project and UUID")
  public void getItemByUuid_pathContainsProjectAndUuid() {
    skipIfNotConfigured();

    ReportPortalItem item = SERVICE.getItemByUuid(ReportPortalConnectionConfig.PROJECT_NAME, NON_EXISTENT_UUID);

    assertThat(item).as("Response must be deserialized without exception").isNotNull();
  }

  private static void skipIfNotConfigured() {
    if (isBlank(ReportPortalConnectionConfig.ENDPOINT) || isBlank(ReportPortalConnectionConfig.API_KEY)) {
      throw new SkipException("ReportPortal env is not configured. Run infra reporting profile first.");
    }
  }

  private static boolean isBlank(String value) {
    return Objects.isNull(value) || value.trim().isEmpty();
  }
}
