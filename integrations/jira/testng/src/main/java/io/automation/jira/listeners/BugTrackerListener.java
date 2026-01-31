package io.automation.jira.listeners;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import io.automation.constant.BugExecutionScope;
import io.automation.constant.StringConstant;
import io.automation.jira.configs.JiraConfig;
import io.automation.jira.services.JiraService;
import io.automation.util.TestUtils;

import com.atlassian.jira.rest.client.api.domain.Issue;
import io.qameta.allure.TmsLink;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IExecutionListener;
import org.testng.IMethodInstance;
import org.testng.IMethodInterceptor;
import org.testng.ITestContext;

/**
 * Listener for skipping tests which are marked as bugs in Jira tickets.
 */
public class BugTrackerListener implements IMethodInterceptor, IExecutionListener {

  private static final Logger LOG = LogManager.getLogger(BugTrackerListener.class);
  private List<String> disabledTestCases = Collections.emptyList();

  @Override
  public void onExecutionStart() {
    if (!JiraConfig.isEnabled()) {
      return;
    }
    if (!JiraConfig.bugExecutionScope().equals(BugExecutionScope.ALL_TESTS)) {
      JiraService jiraService = new JiraService(JiraConfig.jiraUrl(), JiraConfig.jiraToken());
      List<Issue> ticketsWithBug = jiraService.getIssues(JiraConfig.jiraBugQuery());
      disabledTestCases =
          jiraService.getBugsWithTestCases(ticketsWithBug, JiraConfig.jiraBugMarker()).values().stream()
              .flatMap(Collection::stream).distinct().toList();
      LOG.info("Collected disabled test cases via Jira: '{}'",
          String.join(StringConstant.COMMA_SPACE, disabledTestCases));
    }
  }

  @Override
  public List<IMethodInstance> intercept(List<IMethodInstance> methods, ITestContext context) {
    List<IMethodInstance> methodInstancesList = methods.stream().filter(getBugExecutionScopePredicate()).toList();
    String resultMethods = methodInstancesList.stream()
        .filter(methodInstances ->
            Objects.nonNull(TestUtils.getTestAnnotation(methodInstances.getMethod(), TmsLink.class)))
        .map(methodInstances ->
            TestUtils.getTestAnnotation(methodInstances.getMethod(), TmsLink.class).value())
        .collect(Collectors.joining(StringConstant.COMMA_SPACE)).trim();
    if (!resultMethods.isEmpty()) {
      LOG.debug("Test cases for execute: {}", resultMethods);
    }
    return methodInstancesList;
  }

  private boolean isTestEnabled(TmsLink testCaseIdAnnotation) {
    return Objects.nonNull(testCaseIdAnnotation)
        && Objects.nonNull(testCaseIdAnnotation.value())
        && disabledTestCases.stream()
        .filter(Objects::nonNull)
        .map(String::trim)
        .filter(string -> !string.isBlank())
        .noneMatch(caseId -> caseId.equals(testCaseIdAnnotation.value()));
  }

  private Predicate<IMethodInstance> getBugExecutionScopePredicate() {
    if (!JiraConfig.isEnabled()) {
      return methodInstance -> true;
    }
    return switch (JiraConfig.bugExecutionScope()) {
      case EXCLUDE_TESTS_WITH_BUGS -> methodInstance -> {
        TmsLink testCaseIdAnnotation = TestUtils.getTestAnnotation(methodInstance.getMethod(), TmsLink.class);
        return Objects.isNull(testCaseIdAnnotation)  // for Test Data Setup, when test has no TmsLink annotation
            || isTestEnabled(testCaseIdAnnotation);
      };
      case TESTS_WITH_BUGS -> methodInstance ->
          !isTestEnabled(TestUtils.getTestAnnotation(methodInstance.getMethod(), TmsLink.class));
      case ALL_TESTS -> methodInstance -> true;
    };
  }
}
