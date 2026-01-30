package io.automation.testrail.listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import io.automation.config.ConfigRegistry;
import io.automation.testrail.configs.TestRailConfiguration;
import io.automation.testrail.utils.TestRailHelper;
import io.automation.testrail.utils.TestRailTestFilterRules;
import io.automation.util.TestUtils;

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

  @Override
  public void onFinish(ISuite suite) {
    if (!IS_TESTRAIL_ENABLED) return;
    setDisabledTestsAsFailedToTestRail(new ArrayList<>(suite.getExcludedMethods()));
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

  private static String getTestCaseId(ITestNGMethod test) {
    return TestUtils.getTestAnnotation(test, TmsLink.class).value();
  }

}
