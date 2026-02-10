package io.automation.testrail.services;

import java.util.List;
import java.util.Optional;

import io.automation.testrail.models.CaseFields;
import io.automation.testrail.models.CustomAutomationTypes;
import io.automation.testrail.models.Section;
import io.automation.testrail.models.TestCase;
import io.automation.testrail.models.TestCaseType;
import io.automation.testrail.models.TestData;
import io.automation.testrail.models.TestPlan;
import io.automation.testrail.models.TestRun;
import io.automation.testrail.models.TestRunFromSuite;
import io.automation.testrail.models.TestSuite;

public interface TestRailClient {

  Integer getUserId(String email);

  List<TestCaseType> getTestCaseTypesIds();

  List<TestCase> getSuiteCases(int suiteId);

  List<TestCase> getAllCases();

  TestCase getCaseAsModel(int caseId);

  List<TestRun> getTestRuns();

  List<Section> getSectionsAsModel(int projectId, int suiteId);

  Optional<TestRun> findTestRun(int testRunId);

  TestRun addRun(TestRun run);

  TestRun createRunFromSuite(TestRunFromSuite run);

  TestRun closeTestRun(int testRunId);

  Optional<TestPlan> findTestPlan(int testPlanId);

  void addPassedTestResult(int testRunId, String testCaseId);

  void addFailedTestResult(int testRunId, String testCaseId, String errorMessage);

  void addFailedTestResult(int testRunId, String testCaseId, Integer assignedUserId, String commentMessage);

  void addSkippedTestResult(int testRunId, String testCaseId, String errorMessage);

  void addRetestTestResult(int testRunId, String testCaseId, String commentMessage);

  List<TestData> getTestsAsModel(int testRunId);

  List<TestSuite> getAllSuitesAsModel();

  Optional<TestSuite> findTestSuite(int testSuiteId);

  List<CaseFields> getCaseFields();

  void updateCaseAutomationTypeToManual(String testCaseId);

  void updateCaseAutomationType(String testCaseId, CustomAutomationTypes automationType);

  boolean deleteTestRun(int testRunId);

  boolean existsTestRun(int runId);

  boolean existsTestPlan(int planId);

  boolean existsTestSuite(int suiteId);
}
