package io.automation.testrail.services;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import io.automation.config.ConfigRegistry;
import io.automation.http.HttpClientFactory;
import io.automation.http.HttpCoreClient;
import io.automation.http.HttpResponseData;
import io.automation.testrail.configs.TestRailConfiguration;
import io.automation.testrail.models.CaseFields;
import io.automation.testrail.models.CustomAutomationTypes;
import io.automation.testrail.models.Section;
import io.automation.testrail.models.Sections;
import io.automation.testrail.models.TestCase;
import io.automation.testrail.models.TestCaseType;
import io.automation.testrail.models.TestCases;
import io.automation.testrail.models.TestData;
import io.automation.testrail.models.TestDataList;
import io.automation.testrail.models.TestPlan;
import io.automation.testrail.models.TestRailUser;
import io.automation.testrail.models.TestRun;
import io.automation.testrail.models.TestRunFromSuite;
import io.automation.testrail.models.TestRuns;
import io.automation.testrail.models.TestSuite;
import io.automation.testrail.utils.TestRailDataGenerator;
import io.automation.testrail.utils.TestRailHelper;
import io.automation.util.JsonConverter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TestRailHttpClient implements TestRailClient {

  private static final TestRailConfiguration CONFIG = ConfigRegistry.get(TestRailConfiguration.class);

  private static final Logger LOG = LogManager.getLogger(TestRailHttpClient.class);
  private static final String BASE_PATH = "index.php";
  private static final String SUITE_ID_PARAM = "suite_id";
  private static final String EMAIL_PARAM = "email";
  //TestRail endpoints
  private static final String GET_CASES = "api/v2/get_cases/%s".formatted(CONFIG.projectId());
  private static final String GET_CASE = "api/v2/get_case/%s";
  private static final String GET_RUNS = "api/v2/get_runs/%s".formatted(CONFIG.projectId());
  private static final String GET_RUN = "api/v2/get_run/%s";
  private static final String GET_TEST_CASE_TYPES = "/api/v2/get_case_types";
  private static final String ADD_RUN = "api/v2/add_run/%s".formatted(CONFIG.projectId());
  private static final String GET_USER_BY_EMAIL = "api/v2/get_user_by_email";
  private static final String ADD_RESULT_FOR_CASE = "api/v2/add_result_for_case/%s/%s";
  private static final String GET_TESTS = "api/v2/get_tests/%s";
  private static final String GET_CASE_FIELDS = "api/v2/get_case_fields";
  private static final String CREATE_NEW_TEST_RUN = "api/v2/add_run/%s".formatted(CONFIG.projectId());
  private static final String GET_TEST_PLAN = "api/v2/get_plan/%s";
  private static final String GET_TEST_SUITE = "api/v2/get_suite/%s";
  private static final String GET_TEST_SUITES = "api/v2/get_suites/%s".formatted(CONFIG.projectId());
  private static final String CLOSE_TEST_RUN = "api/v2/close_run/%s";
  private static final String UPDATE_CASE = "api/v2/update_case/%s";
  private static final String DELETE_TEST_RUN = "api/v2/delete_run/%s";
  private static final String GET_SECTIONS = "api/v2/get_sections/%s&suite_id=%s";

  private final HttpCoreClient client;

  public TestRailHttpClient() {
    this.client = HttpClientFactory.builder()
        .baseUri(CONFIG.baseUrl())
        .basicAuth(CONFIG.user(), CONFIG.password())
        .defaultHeaders(Map.of(
            "Content-Type", "application/json",
            "Accept", "application/json"
        ))
        .build();
  }

  public Integer getUserId(String email) {
    String path = withParams(apiPath(GET_USER_BY_EMAIL), Map.of(EMAIL_PARAM, email));
    HttpResponseData response = client.get(path);
    return JsonConverter.fromString(response.body(), TestRailUser.class).getId();
  }

  public List<TestCaseType> getTestCaseTypesIds() {
    HttpResponseData response = client.get(apiPath(GET_TEST_CASE_TYPES));
    return JsonConverter.fromString(response.body(), new TypeReference<List<TestCaseType>>() {
    });
  }

  public List<TestCase> getSuiteCases(int suiteId) {
    String path = withParams(apiPath(GET_CASES), Map.of(SUITE_ID_PARAM, suiteId));
    HttpResponseData response = client.get(path);
    return JsonConverter.fromString(response.body(), TestCases.class).getCases();
  }

  public List<TestCase> getAllCases() {
    return getSuiteCases(CONFIG.baseSuiteId());
  }

  public TestCase getCaseAsModel(int caseId) {
    HttpResponseData response = client.get(apiPath(GET_CASE.formatted(caseId)));
    return JsonConverter.fromString(response.body(), TestCase.class);
  }

  public List<TestRun> getTestRuns() {
    HttpResponseData response = client.get(apiPath(GET_RUNS));
    return JsonConverter.fromString(response.body(), TestRuns.class).getRuns();
  }

  public List<Section> getSectionsAsModel(int projectId, int suiteId) {
    HttpResponseData response = client.get(apiPath(GET_SECTIONS.formatted(projectId, suiteId)));
    return JsonConverter.fromString(response.body(), Sections.class).getSections();
  }

  public TestRun getTestRunAsModel(Integer testRunId) {
    HttpResponseData response = getTestRun(testRunId);
    return JsonConverter.fromString(response.body(), TestRun.class);
  }

  public HttpResponseData getTestRun(Integer testRunId) {
    LOG.info("Get test run with id: {}", testRunId);
    return client.get(apiPath(GET_RUN.formatted(testRunId)));
  }

  public TestRun addRun(TestRun run) {
    HttpResponseData response = client.post(apiPath(ADD_RUN), JsonConverter.toJson(run));
    return JsonConverter.fromString(response.body(), TestRun.class);
  }

  public TestRun createRunFromSuite(TestRunFromSuite run) {
    HttpResponseData response = client.post(apiPath(CREATE_NEW_TEST_RUN), JsonConverter.toJson(run));
    return JsonConverter.fromString(response.body(), TestRun.class);
  }

  public TestRun closeTestRun(int testRunId) {
    HttpResponseData response = client.post(apiPath(CLOSE_TEST_RUN.formatted(testRunId)), StringUtils.EMPTY);
    return JsonConverter.fromString(response.body(), TestRun.class);
  }

  public TestPlan getTestPlanAsModel(int testPlanId) {
    HttpResponseData response = getTestPlan(testPlanId);
    return JsonConverter.fromString(response.body(), TestPlan.class);
  }

  public HttpResponseData getTestPlan(int testPlanId) {
    LOG.info("Get test plan with id: {}", testPlanId);
    return client.get(apiPath(GET_TEST_PLAN.formatted(testPlanId)));
  }

  public void addPassedTestResult(int testRunId, String testCaseId) {
    client.post(apiPath(ADD_RESULT_FOR_CASE.formatted(testRunId, testCaseId)),
        JsonConverter.toJson(TestRailDataGenerator.getInstance().generatePassedTestResult()));
  }

  public void addFailedTestResult(int testRunId, String testCaseId, String errorMessage) {
    client.post(apiPath(ADD_RESULT_FOR_CASE.formatted(testRunId, testCaseId)),
        JsonConverter.toJson(TestRailDataGenerator.getInstance().generateFailedTestResult(errorMessage)));
  }

  public void addFailedTestResult(int testRunId, String testCaseId, Integer assignedUserId, String commentMessage) {
    client.post(apiPath(ADD_RESULT_FOR_CASE.formatted(testRunId, testCaseId)),
        JsonConverter.toJson(
            TestRailDataGenerator.getInstance().generateFailedTestResult(assignedUserId, commentMessage)));
  }

  public void addSkippedTestResult(int testRunId, String testCaseId, String errorMessage) {
    client.post(apiPath(ADD_RESULT_FOR_CASE.formatted(testRunId, testCaseId)),
        JsonConverter.toJson(TestRailDataGenerator.getInstance().generateSkippedTestResult(errorMessage)));
  }

  public void addRetestTestResult(int testRunId, String testCaseId, String commentMessage) {
    client.post(apiPath(ADD_RESULT_FOR_CASE.formatted(testRunId, testCaseId)),
        JsonConverter.toJson(TestRailDataGenerator.getInstance().generateRetestTestResult(commentMessage)));
  }

  public List<TestData> getTestsAsModel(int testRunId) {
    HttpResponseData response = client.get(apiPath(GET_TESTS.formatted(testRunId)));
    return JsonConverter.fromString(response.body(), TestDataList.class).getTests();
  }

  public List<TestSuite> getAllSuitesAsModel() {
    HttpResponseData response = client.get(apiPath(GET_TEST_SUITES));
    return JsonConverter.fromString(response.body(), new TypeReference<List<TestSuite>>() {
    });
  }

  public TestSuite getTestSuiteAsModel(int testSuiteId) {
    HttpResponseData response = getTestSuite(testSuiteId);
    return JsonConverter.fromString(response.body(), TestSuite.class);
  }

  public HttpResponseData getTestSuite(int testSuiteId) {
    LOG.info("Get test suite with id: {}", testSuiteId);
    return client.get(apiPath(GET_TEST_SUITE.formatted(testSuiteId)));
  }

  public List<CaseFields> getCaseFields() {
    HttpResponseData response = client.get(apiPath(GET_CASE_FIELDS));
    return JsonConverter.fromString(response.body(), new TypeReference<>() {
    });
  }

  public void updateCaseAutomationTypeToManual(String testCaseId) {
    updateCaseAutomationType(testCaseId, CustomAutomationTypes.MANUAL);
  }

  public void updateCaseAutomationType(String testCaseId, CustomAutomationTypes automationType) {
    ObjectNode customAutomationTypeNode = JsonNodeFactory.instance.objectNode();
    customAutomationTypeNode.put("custom_automation_type", TestRailHelper.getAutomationTypeId(automationType));
    client.post(apiPath(UPDATE_CASE.formatted(testCaseId)), customAutomationTypeNode.toString());
  }

  public HttpResponseData deleteTestRun(int testRunId) {
    LOG.info("Delete test run: {}", testRunId);
    return client.post(apiPath(DELETE_TEST_RUN.formatted(testRunId)), StringUtils.EMPTY);
  }

  private String apiPath(String endpoint) {
    String clean = endpoint == null ? "" : endpoint.trim();
    if (clean.startsWith("/")) {
      clean = clean.substring(1);
    }
    return BASE_PATH + "?" + clean;
  }

  private String withParams(String path, Map<String, ?> params) {
    if (params == null || params.isEmpty()) {
      return path;
    }
    StringBuilder sb = new StringBuilder(path);
    for (Map.Entry<String, ?> entry : params.entrySet()) {
      if (entry.getValue() == null) {
        continue;
      }
      sb.append('&')
          .append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8))
          .append('=')
          .append(URLEncoder.encode(String.valueOf(entry.getValue()), StandardCharsets.UTF_8));
    }
    return sb.toString();
  }
}
