package io.automation.reportportal.configs;

import io.automation.config.ConfigRegistry;

public class ReportPortalConfig {

  private static final ReportPortalConfiguration CONFIG = ConfigRegistry.get(ReportPortalConfiguration.class);

  public static final boolean RUN_REPORT_PORTAL = CONFIG.runReportPortal();
  public static final String RP_PROJECT_NAME = CONFIG.rpProjectName();
  public static final String RP_LAUNCH_NAME = CONFIG.rpLaunchName();
  public static final String RP_LAUNCH_MODE = CONFIG.rpLaunchMode();

  private ReportPortalConfig() {
  }
}
