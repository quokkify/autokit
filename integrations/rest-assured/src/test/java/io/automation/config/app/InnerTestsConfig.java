package io.automation.config.app;

import io.automation.config.ConfigRegistry;

public class InnerTestsConfig {

  private static final InnerTestsConfiguration CONFIG = ConfigRegistry.get(InnerTestsConfiguration.class);

  public static final String BASE_API_URL = CONFIG.baseApiUrl();

  private InnerTestsConfig() {
  }
}
