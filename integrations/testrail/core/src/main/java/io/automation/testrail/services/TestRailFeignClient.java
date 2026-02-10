package io.automation.testrail.services;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import feign.Feign;
import feign.FeignException;
import feign.RequestInterceptor;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import io.automation.config.ConfigRegistry;
import io.automation.testrail.configs.TestRailConfiguration;
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
import io.automation.testrail.utils.TestRailDataGenerator;
import io.automation.testrail.utils.TestRailHelper;

public class TestRailFeignClient implements TestRailClient {

  private static final TestRailConfiguration CONFIG = ConfigRegistry.get(TestRailConfiguration.class);
  private static final String SUITE_ID_PARAM = "suite_id";
  private static final String EMAIL_PARAM = "email";

  private final TestRailFeignApi api;

  public TestRailFeignClient() {
    this.api = Feign.builder()
        .client(new OkHttpClient())
        .encoder(new JacksonEncoder())
        .decoder(new JacksonDecoder())
        .requestInterceptor(defaultHeadersInterceptor())
        .target(TestRailFeignApi.class, CONFIG.baseUrl());
  }

  @Override
  public Integer getUserId(String email) {
    return api.getUserByEmail(Map.of(EMAIL_PARAM, email)).getId();
  }

  @Override
  public List<TestCaseType> getTestCaseTypesIds() {
    return api.getTestCaseTypes();
  }

  @Override
  public List<TestCase> getSuiteCases(int suiteId) {
    return api.getCases(CONFIG.projectId(), Map.of(SUITE_ID_PARAM, suiteId)).getCases();
  }

  @Override
  public List<TestCase> getAllCases() {
    return getSuiteCases(CONFIG.baseSuiteId());
  }

  @Override
  public TestCase getCaseAsModel(int caseId) {
    return api.getCase(caseId);
  }

  @Override
  public List<TestRun> getTestRuns() {
    return api.getRuns(CONFIG.projectId()).getRuns();
  }

  @Override
  public List<Section> getSectionsAsModel(int projectId, int suiteId) {
    return api.getSections(projectId, Map.of(SUITE_ID_PARAM, suiteId)).getSections();
  }

  @Override
  public Optional<TestRun> findTestRun(int testRunId) {
    return executeOptional(() -> api.getRun(testRunId));
  }

  @Override
  public TestRun addRun(TestRun run) {
    return api.addRun(CONFIG.projectId(), run);
  }

  @Override
  public TestRun createRunFromSuite(TestRunFromSuite run) {
    return api.addRun(CONFIG.projectId(), run);
  }

  @Override
  public TestRun closeTestRun(int testRunId) {
    return api.closeRun(testRunId, Collections.emptyMap());
  }

  @Override
  public Optional<TestPlan> findTestPlan(int testPlanId) {
    return executeOptional(() -> api.getPlan(testPlanId));
  }

  @Override
  public void addPassedTestResult(int testRunId, String testCaseId) {
    api.addResultForCase(testRunId, testCaseId, TestRailDataGenerator.getInstance().generatePassedTestResult());
  }

  @Override
  public void addFailedTestResult(int testRunId, String testCaseId, String errorMessage) {
    api.addResultForCase(
        testRunId,
        testCaseId,
        TestRailDataGenerator.getInstance().generateFailedTestResult(errorMessage)
    );
  }

  @Override
  public void addFailedTestResult(int testRunId, String testCaseId, Integer assignedUserId, String commentMessage) {
    api.addResultForCase(
        testRunId,
        testCaseId,
        TestRailDataGenerator.getInstance().generateFailedTestResult(assignedUserId, commentMessage)
    );
  }

  @Override
  public void addSkippedTestResult(int testRunId, String testCaseId, String errorMessage) {
    api.addResultForCase(
        testRunId,
        testCaseId,
        TestRailDataGenerator.getInstance().generateSkippedTestResult(errorMessage)
    );
  }

  @Override
  public void addRetestTestResult(int testRunId, String testCaseId, String commentMessage) {
    api.addResultForCase(
        testRunId,
        testCaseId,
        TestRailDataGenerator.getInstance().generateRetestTestResult(commentMessage)
    );
  }

  @Override
  public List<TestData> getTestsAsModel(int testRunId) {
    return api.getTests(testRunId).getTests();
  }

  @Override
  public List<TestSuite> getAllSuitesAsModel() {
    return api.getSuites(CONFIG.projectId());
  }

  @Override
  public Optional<TestSuite> findTestSuite(int testSuiteId) {
    return executeOptional(() -> api.getSuite(testSuiteId));
  }

  @Override
  public List<CaseFields> getCaseFields() {
    return api.getCaseFields();
  }

  @Override
  public void updateCaseAutomationTypeToManual(String testCaseId) {
    updateCaseAutomationType(testCaseId, CustomAutomationTypes.MANUAL);
  }

  @Override
  public void updateCaseAutomationType(String testCaseId, CustomAutomationTypes automationType) {
    ObjectNode customAutomationTypeNode = JsonNodeFactory.instance.objectNode();
    customAutomationTypeNode.put("custom_automation_type", TestRailHelper.getAutomationTypeId(automationType));
    api.updateCase(testCaseId, customAutomationTypeNode);
  }

  @Override
  public boolean deleteTestRun(int testRunId) {
    try {
      api.deleteRun(testRunId, Collections.emptyMap());
      return true;
    } catch (FeignException.NotFound ignored) {
      return false;
    } catch (FeignException e) {
      throw withContext("POST /index.php?api/v2/delete_run/" + testRunId, e);
    }
  }

  @Override
  public boolean existsTestRun(int runId) {
    return findTestRun(runId).isPresent();
  }

  @Override
  public boolean existsTestPlan(int planId) {
    return findTestPlan(planId).isPresent();
  }

  @Override
  public boolean existsTestSuite(int suiteId) {
    return findTestSuite(suiteId).isPresent();
  }

  private RequestInterceptor defaultHeadersInterceptor() {
    return template -> {
      template.header("Content-Type", "application/json");
      template.header("Accept", "application/json");
      template.header("Authorization", basicAuthHeaderValue());
    };
  }

  private String basicAuthHeaderValue() {
    String token = CONFIG.user() + ":" + CONFIG.password();
    String encoded = Base64.getEncoder().encodeToString(token.getBytes(StandardCharsets.UTF_8));
    return "Basic " + encoded;
  }

  private <T> Optional<T> executeOptional(FeignSupplier<T> supplier) {
    try {
      return Optional.ofNullable(supplier.get());
    } catch (FeignException.NotFound ignored) {
      return Optional.empty();
    } catch (FeignException e) {
      throw withContext(e.request().httpMethod().name() + " " + e.request().url(), e);
    }
  }

  private RuntimeException withContext(String endpoint, FeignException e) {
    return new RuntimeException("HTTP request failed: " + endpoint, e);
  }

  @FunctionalInterface
  private interface FeignSupplier<T> {
    T get();
  }
}
