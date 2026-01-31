package io.automation.jira.configs;

import io.automation.config.ConfigRegistry;
import io.automation.constant.BugExecutionScope;

import org.apache.commons.lang3.StringUtils;

public final class JiraConfig {

  private static final JiraConfiguration CONFIG = ConfigRegistry.get(JiraConfiguration.class);

  private JiraConfig() {
  }

  public static String jiraUrl() {
    return CONFIG.jiraUrl();
  }

  public static String jiraToken() {
    return CONFIG.jiraToken();
  }

  public static String jiraBugMarker() {
    return CONFIG.jiraBugMarker();
  }

  public static String jiraBugQuery() {
    return CONFIG.jiraBugQuery();
  }

  public static BugExecutionScope bugExecutionScope() {
    return BugExecutionScope.valueOf(CONFIG.bugExecutionScope());
  }

  public static String jiraIssueUrl() {
    return StringUtils.appendIfMissing(jiraUrl(), "/") + "browse/";
  }

  public static boolean isEnabled() {
    return StringUtils.isNotBlank(jiraUrl())
        && StringUtils.isNotBlank(jiraToken())
        && StringUtils.isNotBlank(jiraBugQuery())
        && StringUtils.isNotBlank(jiraBugMarker());
  }
}
