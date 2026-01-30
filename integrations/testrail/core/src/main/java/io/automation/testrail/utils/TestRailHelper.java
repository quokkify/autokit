package io.automation.testrail.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.automation.constant.StringConstant;
import io.automation.config.ConfigRegistry;
import io.automation.testrail.configs.TestRailConfiguration;
import io.automation.testrail.models.CaseFields;
import io.automation.testrail.models.CustomAutomationType;
import io.automation.testrail.models.CustomAutomationTypes;
import io.automation.testrail.models.CustomTag;
import io.automation.testrail.models.Section;
import io.automation.testrail.models.TestCase;
import io.automation.testrail.models.TestData;
import io.automation.testrail.models.TestPlan;
import io.automation.testrail.models.TestRun;
import io.automation.testrail.models.TestRunFromSuite;
import io.automation.testrail.models.TestSuite;
import io.automation.testrail.services.TestRailApiService;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Utility class for interfacing with the TestRail API. Provides methods to manage test runs, test cases,
 * results, custom fields, and other related artifacts in TestRail.
 */
public final class TestRailHelper {

  private static final Logger LOG = LogManager.getLogger(TestRailHelper.class);
  private static final TestRailConfiguration CONFIG = ConfigRegistry.get(TestRailConfiguration.class);
  private static final TestRailApiService TEST_RAIL_API_SERVICE = new TestRailApiService();
  private static final Integer TAG_FIELD_CONFIG_INDEX = 0;
  private static final String TAGS_SYSTEM_NAME = "custom_tags";
  private static final Integer AUTOMATION_TYPE_FIELD_CONFIG_INDEX = 0;
  private static final String AUTOMATION_TYPE_SYSTEM_NAME = "custom_automation_type";
  private static final String BREAD_CRUMBS_DELIMITER = ">";

  private TestRailHelper() {
  }

  public static void addTestResultForPassedTest(String testCaseId) {
    TestRailDataGenerator.allTestsForExecute.entrySet().stream()
        .filter(
            testRun -> testRun.getValue().stream()
                .anyMatch(testCase -> testCase.getCaseId().toString().equals(testCaseId)))
        .forEach(testRun -> {
          LOG.debug("Add result for passed test '{}', in run '{}'", testCaseId, testRun.getKey());
          TEST_RAIL_API_SERVICE.addPassedTestResult(testRun.getKey(), testCaseId);
        });
  }

  public static void addTestResultForFailedTest(String testCaseId, String errorMessage) {
    TestRailDataGenerator.allTestsForExecute.entrySet().stream()
        .filter(
            testRun -> testRun.getValue().stream()
                .anyMatch(testCase -> testCase.getCaseId().toString().equals(testCaseId)))
        .forEach(testRun -> {
          LOG.info("Add result for failed test '{}', in run '{}'", testCaseId, testRun.getKey());
          TEST_RAIL_API_SERVICE.addFailedTestResult(testRun.getKey(), testCaseId, errorMessage);
        });
  }

  public static void addTestResultForSkippedTest(String testCaseId, String errorMessage) {
    TestRailDataGenerator.allTestsForExecute.entrySet().stream()
        .filter(
            testRun -> testRun.getValue().stream()
                .anyMatch(testCase -> testCase.getCaseId().toString().equals(testCaseId)))
        .forEach(testRun -> {
          LOG.info("Add result for skipped test '{}', in run '{}'", testCaseId, testRun.getKey());
          TEST_RAIL_API_SERVICE.addSkippedTestResult(testRun.getKey(), testCaseId, errorMessage);
        });
  }

  public static void addTestResultForDisabledTest(String testCaseId, int assignedUserId, String commentMessage) {
    TestRailDataGenerator.allTestsForExecute.entrySet().stream()
        .filter(testRun -> testRun.getValue().stream()
            .anyMatch(testCase -> testCase.getCaseId().equals(Integer.parseInt(testCaseId))))
        .forEach(testRun -> {
          LOG.info("Add result for disabled test '{}', in run '{}'", testCaseId, testRun.getKey());
          TEST_RAIL_API_SERVICE.addFailedTestResult(testRun.getKey(), testCaseId, assignedUserId, commentMessage);
        });
  }

  /**
   * Set 'Retest' status to all tests for execute with status 'Automated'.
   */
  public static void resetTestResultsToRetestForAllTestsForExecute() {
    TestRailDataGenerator.allTestsForExecute.forEach((id, testDataList) -> testDataList.stream()
        .filter(testData -> testData.getCustomAutomationType()
            .equals(getAutomationTypeId(CustomAutomationTypes.AUTOMATED)))
        .forEach(testData -> addRetestTestResult(String.valueOf(testData.getCaseId()), "Reset result to 'Retest'")));
  }

  public static void addRetestTestResult(String testCaseId, String commentMessage) {
    TestRailDataGenerator.allTestsForExecute.entrySet().stream()
        .filter(
            testRun -> testRun.getValue().stream()
                .anyMatch(testCase -> testCase.getCaseId().toString().equals(testCaseId)))
        .forEach(testRun -> {
          LOG.info("Add retest result for test '{}', in run '{}'", testCaseId, testRun.getKey());
          TEST_RAIL_API_SERVICE.addRetestTestResult(testRun.getKey(), testCaseId, commentMessage);
        });
  }

  public static List<TestData> getTests(int testRunId) {
    return TEST_RAIL_API_SERVICE.getTestsAsModel(testRunId);
  }

  public static TestData getActualTest(int testRunId, int testCaseId) {
    return TestRailDataGenerator.allTestsForExecute.get(testRunId)
        .stream()
        .filter(testData -> testData.getCaseId().equals(testCaseId))
        .findFirst()
        .orElseThrow(() ->
            new RuntimeException("Test case with '%d' id was not found in test run with '%d' id"
                .formatted(testCaseId, testRunId)));
  }

  public static int getTestRunId(int testCaseId) {
    return TestRailDataGenerator.allTestsForExecute.entrySet().stream()
        .filter(testRunAsEntry -> testRunAsEntry.getValue().stream()
            .anyMatch(testData -> testData.getCaseId() == testCaseId))
        .map(Map.Entry::getKey)
        .findFirst()
        .orElseThrow(() ->
            new RuntimeException("Test case with '%d' id was not found in any test run".formatted(testCaseId)));
  }

  public static Map<Integer, String> getBreadCrumbs() {
    Map<Integer, String> suiteIds = TEST_RAIL_API_SERVICE.getAllSuitesAsModel().stream()
        .collect(Collectors.toMap(TestSuite::getId, TestSuite::getName));
    return suiteIds.entrySet().stream().parallel().map(entry -> {
      List<Section> sections = TEST_RAIL_API_SERVICE.getSectionsAsModel(CONFIG.projectId(), entry.getKey());
      return sections.stream().parallel()
              .collect(Collectors.toMap(
                  Section::getId,
                  section -> entry.getValue()
                      .concat(BREAD_CRUMBS_DELIMITER).concat(getFullSectionPath(sections, section.getId()))
              ));
    }).flatMap(map -> map.entrySet().stream())
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            Map.Entry::getValue,
            (existing, replacement) -> replacement));
  }

  public static TestRun createTestRunFromSuite(TestRunFromSuite testRunFromSuite) {
    return TEST_RAIL_API_SERVICE.createRunFromSuite(testRunFromSuite);
  }

  public static void closeActualTestRuns() {
    TestRailDataGenerator.allTestsForExecute.forEach((key, value) -> closeTestRun(key));
  }

  public static void deleteActualTestRuns() {
    TestRailDataGenerator.allTestsForExecute.forEach((key, value) -> deleteTestRun(key));
  }

  public static TestRun closeTestRun(int testRunId) {
    LOG.info("Close test run {}", testRunId);
    return TEST_RAIL_API_SERVICE.closeTestRun(testRunId);
  }

  public static boolean deleteTestRun(int testRunId) {
    return TEST_RAIL_API_SERVICE.deleteTestRun(testRunId).statusCode() == 200;
  }

  public static TestSuite getTestSuite(int suiteId) {
    return TEST_RAIL_API_SERVICE.getTestSuiteAsModel(suiteId);
  }

  public static TestPlan getTestPlan(int planId) {
    return TEST_RAIL_API_SERVICE.getTestPlanAsModel(planId);
  }

  public static boolean isExistTestSuite(int suiteId) {
    return TEST_RAIL_API_SERVICE.getTestSuite(suiteId).statusCode() == 200;
  }

  public static boolean isExistTestPlan(int planId) {
    return TEST_RAIL_API_SERVICE.getTestPlan(planId).statusCode() == 200;
  }

  public static boolean isExistTestRun(int runId) {
    return TEST_RAIL_API_SERVICE.getTestRun(runId).statusCode() == 200;
  }

  public static Integer getUserId() {
    return TEST_RAIL_API_SERVICE.getUserId(CONFIG.user());
  }

  /**
   * Find test run.
   *
   * @return test run
   */
  public static TestRun getTestRun(Integer testRunId) {
    return TEST_RAIL_API_SERVICE.getTestRuns().stream()
        .filter(testRun -> testRun.getId().equals(testRunId))
        .findFirst()
        .orElse(null);
  }

  public static TestCase getTestCase(int testCaseId) {
    return TEST_RAIL_API_SERVICE.getCaseAsModel(testCaseId);
  }

  public static List<CustomTag> getAvailableCustomTags() {
    String tags = getCustomCaseFieldBySystemName(TAGS_SYSTEM_NAME)
        .getConfigs()
        .get(TAG_FIELD_CONFIG_INDEX)
        .getOptions()
        .getItems();
    return Arrays.stream(tags.split(StringUtils.LF))
        .map(idAndName -> {
          String[] separatedIdAndName = idAndName.split(StringConstant.COMMA);
          return new CustomTag(Integer.parseInt(separatedIdAndName[0].trim()), separatedIdAndName[1].trim());
        })
        .toList();
  }

  public static int getAutomationTypeId(CustomAutomationTypes customAutomationType) {
    return getCustomAutomationTypes().stream()
        .filter(automationType -> automationType.getAutomationType().equals(customAutomationType.capitalize()))
        .findAny()
        .orElseThrow(() -> new RuntimeException("Automation type '%s' not found".formatted(customAutomationType)))
        .getId();
  }

  private static List<CustomAutomationType> getCustomAutomationTypes() {
    String automationTypes = getCustomCaseFieldBySystemName(AUTOMATION_TYPE_SYSTEM_NAME)
        .getConfigs()
        .get(AUTOMATION_TYPE_FIELD_CONFIG_INDEX)
        .getOptions()
        .getItems();
    return Arrays.stream(automationTypes.split(StringUtils.LF))
        .map(idAndName -> {
          String[] separatedIdAndType = idAndName.split(StringConstant.COMMA);
          return new CustomAutomationType(Integer.parseInt(separatedIdAndType[0].trim()), separatedIdAndType[1].trim());
        })
        .toList();
  }

  private static CaseFields getCustomCaseFieldBySystemName(String customCaseField) {
    return TEST_RAIL_API_SERVICE.getCaseFields().stream()
        .filter(caseFields -> caseFields.getSystemName().equals(customCaseField))
        .findFirst()
        .orElseThrow(() -> new RuntimeException("Custom field '%s' not found".formatted(customCaseField)));
  }

  private static String getFullSectionPath(List<Section> sections, Integer sectionId) {
    Section section = sections.stream().parallel()
        .filter(eachSection -> eachSection.getId().equals(sectionId)).findFirst().orElse(null);
    if (section == null) {
      return StringUtils.EMPTY;
    }
    StringBuilder path = new StringBuilder();
    if (section.getParentId() != null) {
      path.append(getFullSectionPath(sections, section.getParentId()));
      path.append(BREAD_CRUMBS_DELIMITER);
    }
    path.append(section.getName());
    return path.toString();
  }
}
