package io.automation.reportportal.config;

import io.automation.config.ConfigRegistry;

public class ReportPortalConnectionConfig {

  private static final ReportPortalConnectionConfiguration CONFIG =
      ConfigRegistry.get(ReportPortalConnectionConfiguration.class);

  public static final String ENDPOINT = CONFIG.endpoint();
  public static final String API_KEY = CONFIG.apiKey();

  private ReportPortalConnectionConfig() {
  }
}
