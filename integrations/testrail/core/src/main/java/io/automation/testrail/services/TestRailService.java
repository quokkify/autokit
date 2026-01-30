package io.automation.testrail.services;

import java.util.Objects;

import io.automation.testrail.models.TestPlan;
import io.automation.testrail.models.TestRun;
import io.automation.testrail.utils.TestRailDataGenerator;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public class TestRailService {

  private static volatile TestRailService instance;
  private TestPlan testPlan;
  private TestRun testRun;

  private TestRailService() {
  }

  @SuppressFBWarnings("MS_EXPOSE_REP")
  public static TestRailService getInstance() {
    TestRailService localInstance = instance;
    if (localInstance == null) {
      synchronized (TestRailService.class) {
        localInstance = instance;
        if (localInstance == null) {
          instance = new TestRailService();
          TestRailDataGenerator.getInstance();
        }
      }
    }
    return instance;
  }

  public boolean isTestRun() {
    return Objects.nonNull(testRun);
  }

  public boolean isTestPlan() {
    return Objects.nonNull(testPlan);
  }

  public TestPlan getTestPlan() {
    return testPlan;
  }

  public void setTestPlan(TestPlan testPlan) {
    this.testPlan = testPlan;
  }

  public TestRun getTestRun() {
    return testRun;
  }

  public void setTestRun(TestRun testRun) {
    this.testRun = testRun;
  }
}
