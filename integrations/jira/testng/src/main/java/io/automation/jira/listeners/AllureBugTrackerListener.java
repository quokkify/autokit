package io.automation.jira.listeners;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.automation.constant.BugExecutionScope;
import io.automation.jira.configs.JiraConfig;
import io.automation.jira.services.JiraService;
import io.automation.util.TestUtils;

import com.atlassian.jira.rest.client.api.domain.Issue;
import io.qameta.allure.Allure;
import io.qameta.allure.TmsLink;
import io.qameta.allure.model.Link;
import io.qameta.allure.util.ResultsUtils;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestResult;

/**
 * Listener for adding links to Jira issues in Allure.
 */
public class AllureBugTrackerListener implements IInvokedMethodListener, ISuiteListener {

  private Map<String, List<String>> disabledTestCases = Collections.emptyMap();

  @Override
  public void onStart(ISuite suite) {
    if (!JiraConfig.isEnabled()) {
      return;
    }
    if (JiraConfig.bugExecutionScope().equals(BugExecutionScope.ALL_TESTS)) {
      JiraService jiraService = new JiraService(JiraConfig.jiraUrl(), JiraConfig.jiraToken());
      List<Issue> ticketsWithBug = jiraService.getIssues(JiraConfig.jiraBugQuery());
      disabledTestCases = jiraService.getBugsWithTestCases(ticketsWithBug, JiraConfig.jiraBugMarker());
    }
  }

  @Override
  public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
    if (!JiraConfig.isEnabled()) {
      return;
    }
    if (JiraConfig.bugExecutionScope().equals(BugExecutionScope.ALL_TESTS)
        && testResult.getStatus() == ITestResult.FAILURE) {
      TmsLink testCaseIdAnnotation = TestUtils.getTestAnnotation(method.getTestMethod(), TmsLink.class);
      if (Objects.nonNull(testCaseIdAnnotation) && Objects.nonNull(testCaseIdAnnotation.value())) {
        disabledTestCases.entrySet().stream()
            .filter(map -> map.getValue().stream().anyMatch(caseId -> caseId.contains(testCaseIdAnnotation.value())))
            .map(Map.Entry::getKey)
            .toList()
            .forEach(ticketId -> {
              Link issue = ResultsUtils.createIssueLink(ticketId);
              Allure.issue(issue.getName(), issue.getUrl());
            });
      }
    }
  }
}
