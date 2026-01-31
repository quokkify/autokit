package io.automation.testrail.listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import io.automation.config.ConfigRegistry;
import io.automation.constant.StringConstant;
import io.automation.jira.configs.JiraConfiguration;
import io.automation.jira.services.JiraService;
import io.automation.testrail.configs.TestRailConfiguration;
import io.automation.testrail.utils.TestRailHelper;
import io.automation.testrail.utils.TestRailTestFilterRules;
import io.automation.util.TestUtils;

import com.atlassian.jira.rest.client.api.domain.Issue;
import io.qameta.allure.TmsLink;
import org.apache.commons.lang3.StringUtils;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestNGMethod;

/**
 * Listener adds info (description, issue link) about disabled tests to TestRail.
 * Add only to tests filtered by rules from {@link TestRailTestFilterRules}
 */
public class AddDisabledTestsToTestRailListener implements ISuiteListener {

  private static final TestRailConfiguration CONFIG = ConfigRegistry.get(TestRailConfiguration.class);
  private static final JiraConfiguration JIRA_CONFIG = ConfigRegistry.get(JiraConfiguration.class);
  private static final boolean IS_TESTRAIL_ENABLED =
      !CONFIG.isTestrailDisabled() && StringUtils.isNotEmpty(CONFIG.testRailId());

  @Override
  public void onFinish(ISuite suite) {
    if (!IS_TESTRAIL_ENABLED) return;
    setDisabledTestsAsFailedToTestRail(new ArrayList<>(suite.getExcludedMethods()));
    addJiraBugsToTestRail(new ArrayList<>(suite.getAllMethods()));
    if (CONFIG.closeTestRun()) {
      TestRailHelper.closeActualTestRuns();
    }
    if (CONFIG.deleteTestRun()) {
      TestRailHelper.deleteActualTestRuns();
    }
  }

  private void setDisabledTestsAsFailedToTestRail(List<ITestNGMethod> allTests) {
    allTests.stream()
        .filter(test -> Objects.nonNull(TestUtils.getTestAnnotation(test, TmsLink.class)))
        .filter(test -> !test.getEnabled())
        .forEach(test -> {
          AtomicReference<String> commentMessage =
              new AtomicReference<>("TC-%s is disabled via 'enabled' attribute.".formatted(getTestCaseId(test)));
          String testCaseId = getTestCaseId(test);
          if (TestRailTestFilterRules.needRunTest(testCaseId)) {
            TestRailHelper.addTestResultForDisabledTest(
                testCaseId,
                CONFIG.userIdAssignedDisabledTests(),
                commentMessage.get());
          }
        });
  }

  private void addJiraBugsToTestRail(List<ITestNGMethod> allTests) {
    if (!isJiraEnabled()) {
      return;
    }
    JiraService jiraService = new JiraService(JIRA_CONFIG.jiraUrl(), JIRA_CONFIG.jiraToken());
    List<Issue> ticketsWithBug = jiraService.getIssues(JIRA_CONFIG.jiraBugQuery());
    Map<String, List<String>> testCasesWithBugs =
        jiraService.getTestCasesWithBugs(ticketsWithBug, JIRA_CONFIG.jiraBugMarker());
    allTests.stream()
        .filter(test -> Objects.nonNull(TestUtils.getTestAnnotation(test, TmsLink.class)))
        .filter(test -> hasBugs(testCasesWithBugs, getTestCaseId(test)))
        .forEach(test -> {
          String testCaseId = getTestCaseId(test);
          List<String> jiraBugs = testCasesWithBugs.get(testCaseId);
          AtomicReference<String> commentMessage = new AtomicReference<>(buildBugComment(testCaseId, jiraBugs));
          if (TestRailTestFilterRules.needRunTest(testCaseId)) {
            TestRailHelper.addTestResultForDisabledTest(
                testCaseId,
                CONFIG.userIdAssignedDisabledTests(),
                commentMessage.get());
          }
        });
  }

  private static boolean hasBugs(Map<String, List<String>> testCasesWithBugs, String testCaseId) {
    List<String> jiraBugs = testCasesWithBugs.get(testCaseId);
    return jiraBugs != null && !jiraBugs.isEmpty();
  }

  private static String getTestCaseId(ITestNGMethod test) {
    return TestUtils.getTestAnnotation(test, TmsLink.class).value();
  }

  private static String buildBugComment(String testCaseId, List<String> jiraBugs) {
    String jiraBugsString = jiraBugs.stream()
        .map(AddDisabledTestsToTestRailListener::buildJiraLink)
        .collect(Collectors.joining(StringConstant.COMMA_SPACE));
    return "TC-%s is disabled via **Jira Issue(s):** %s".formatted(testCaseId, jiraBugsString);
  }

  private static String buildJiraLink(String issueId) {
    return "[%2$s](%1$s%2$s)".formatted(jiraIssueUrl(), issueId);
  }

  private static String jiraIssueUrl() {
    return StringUtils.appendIfMissing(JIRA_CONFIG.jiraUrl(), "/") + "browse/";
  }

  private static boolean isJiraEnabled() {
    return StringUtils.isNotBlank(JIRA_CONFIG.jiraUrl())
        && StringUtils.isNotBlank(JIRA_CONFIG.jiraToken())
        && StringUtils.isNotBlank(JIRA_CONFIG.jiraBugQuery())
        && StringUtils.isNotBlank(JIRA_CONFIG.jiraBugMarker());
  }
}
