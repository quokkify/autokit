package io.automation.testrail.listeners;

import java.util.Objects;

import io.automation.config.ConfigRegistry;
import io.automation.testrail.configs.TestRailConfiguration;
import io.automation.testrail.utils.TestRailHelper;
import io.automation.util.TestUtils;
import io.qameta.allure.TmsLink;
import org.apache.commons.lang3.StringUtils;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * Listener updates test statuses in TestRail and adds error message if test is not passed.
 */
public class TestStatusTestRailListener implements ITestListener {

  private static final TestRailConfiguration CONFIG = ConfigRegistry.get(TestRailConfiguration.class);
  private static final boolean IS_TESTRAIL_ENABLED =
      !CONFIG.isTestrailDisabled() && StringUtils.isNotEmpty(CONFIG.testRailId());

  @Override
  public void onTestSuccess(ITestResult result) {
    if (!IS_TESTRAIL_ENABLED) return;
    TestRailHelper.addTestResultForPassedTest(getTestCaseId(result));
  }

  @Override
  public void onTestFailure(ITestResult result) {
    if (!IS_TESTRAIL_ENABLED) return;
    TestRailHelper.addTestResultForFailedTest(getTestCaseId(result), getErrorMessage(result));
  }

  @Override
  public void onTestSkipped(ITestResult result) {
    if (!IS_TESTRAIL_ENABLED) return;
    TestRailHelper.addTestResultForSkippedTest(getTestCaseId(result), getErrorMessage(result));
  }

  private String getErrorMessage(ITestResult result) {
    Throwable throwable = result.getThrowable();
    return Objects.nonNull(throwable) ? throwable.getMessage() : "Error message not found";
  }

  private static String getTestCaseId(ITestResult testResult) {
    TmsLink testCaseIdAnnotation = TestUtils.getTestAnnotation(testResult.getMethod(), TmsLink.class);
    if (Objects.nonNull(testCaseIdAnnotation) && Objects.nonNull(testCaseIdAnnotation.value())) {
      return TestUtils.getTestAnnotation(testResult.getMethod(), TmsLink.class).value();
    } else {
      throw new RuntimeException(
          "Test has no test case id (TmsLink), test name: %s".formatted(testResult.getMethod().getMethodName()));
    }
  }
}
