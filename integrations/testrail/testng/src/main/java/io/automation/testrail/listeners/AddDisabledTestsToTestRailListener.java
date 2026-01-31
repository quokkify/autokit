package io.automation.testrail.listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import io.automation.config.ConfigRegistry;
import io.automation.constant.StringConstant;
import io.automation.jira.configs.JiraConfig;
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
  private static final boolean IS_TESTRAIL_ENABLED =
      !CONFIG.isTestrailDisabled() && StringUtils.isNotEmpty(CONFIG.testRailId());
  private static final boolean IS_JIRA_ENABLED = JiraConfig.isEnabled();

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
    if (!IS_JIRA_ENABLED) {
      return;
    }
    JiraService jiraService = new JiraService(JiraConfig.jiraUrl(), JiraConfig.jiraToken());
    List<Issue> ticketsWithBug = jiraService.getIssues(JiraConfig.jiraBugQuery());
    Map<String, List<String>> testCasesWithBugs =
        jiraService.getTestCasesWithBugs(ticketsWithBug, JiraConfig.jiraBugMarker());
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
    return "[%2$s](%1$s%2$s)".formatted(JiraConfig.jiraIssueUrl(), issueId);
  }
}
