package io.automation.reportportal.services;

import java.util.Calendar;
import java.util.Objects;
import java.util.function.Supplier;

import com.epam.reportportal.listeners.ItemStatus;
import com.epam.reportportal.listeners.ListenerParameters;
import com.epam.reportportal.service.Launch;
import com.epam.reportportal.service.ReportPortal;
import com.epam.reportportal.testng.TestNGService;
import com.epam.reportportal.utils.properties.PropertiesLoader;
import com.epam.ta.reportportal.ws.model.FinishTestItemRQ;
import com.epam.ta.reportportal.ws.model.StartTestItemRQ;
import com.epam.ta.reportportal.ws.model.launch.Mode;
import com.epam.ta.reportportal.ws.model.launch.StartLaunchRQ;
import io.automation.config.ConfigRegistry;
import io.automation.constant.StringConstant;
import io.automation.reportportal.configs.ReportPortalConfig;
import io.automation.testrail.configs.TestRailConfiguration;
import io.automation.testrail.services.TestRailService;
import io.automation.util.TestUtils;
import io.qameta.allure.TmsLink;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;

public class ParamOverrideTestNgService extends TestNGService {

  protected static final Logger LOG = LogManager.getLogger(ParamOverrideTestNgService.class);
  protected static final String BRANCH =
      Objects.requireNonNullElse(System.getenv("CI_COMMIT_REF_NAME"), StringUtils.EMPTY);
  protected static final ThreadLocal<String> TEST_CASE_ID = new ThreadLocal<>();
  private static final TestRailConfiguration TESTRAIL_CONFIG = ConfigRegistry.get(TestRailConfiguration.class);

  public ParamOverrideTestNgService() {
    super(getLaunchOverriddenProperties());
  }

  @Override
  protected StartTestItemRQ buildStartStepRq(ITestResult testResult) {
    final StartTestItemRQ startTestItemRq = super.buildStartStepRq(testResult);
    TmsLink testCaseIdAnnotation = TestUtils.getTestAnnotation(testResult.getMethod(), TmsLink.class);
    if (Objects.nonNull(testCaseIdAnnotation) && Objects.nonNull(testCaseIdAnnotation.value())) {
      TEST_CASE_ID.set(testCaseIdAnnotation.value());
    }
    if (testResult.getMethod().isTest()) {
      startTestItemRq.setDescription(getTestCaseDescription(testResult));
    }
    return startTestItemRq;
  }

  @Override
  protected FinishTestItemRQ buildFinishTestMethodRq(ItemStatus status, ITestResult testResult) {
    FinishTestItemRQ finishTestItemRq = super.buildFinishTestMethodRq(status, testResult);
    if (testResult.getMethod().isTest()) {
      finishTestItemRq.setDescription(getTestCaseDescription(testResult));
      TEST_CASE_ID.remove();
    }
    return finishTestItemRq;
  }

  protected static String getTestCaseDescription(ITestResult testResult) {
    ITestNGMethod testMethod = testResult.getMethod();
    String testDescription = testMethod.getDescription();
    String testRailCaseUrl = StringUtils.isNotBlank(TESTRAIL_CONFIG.testRailCaseUrl())
        ? TESTRAIL_CONFIG.testRailCaseUrl().formatted(TEST_CASE_ID.get())
        : StringUtils.EMPTY;
    return """
        **Test Case ID:** [%s](%s)%n\
        **Description:** %s%n\
        """.formatted(
        TEST_CASE_ID.get(), testRailCaseUrl,
        StringUtils.isEmpty(testDescription)
            ? StringConstant.NOT_AVAILABLE
            : testDescription);
  }

  private static Supplier<Launch> getLaunchOverriddenProperties() {
    ListenerParameters parameters = new ListenerParameters(PropertiesLoader.load());
    setEnabledStatus(parameters);
    setProjectName(parameters);
    setLaunchName(parameters);
    setLaunchDescription(parameters);
    setLaunchMode(parameters);
    ReportPortal reportPortal = ReportPortal.builder().withParameters(parameters).build();
    StartLaunchRQ startLaunchRequest = buildStartLaunch(parameters);
    return () -> reportPortal.newLaunch(startLaunchRequest);
  }

  private static void setEnabledStatus(ListenerParameters parameters) {
    parameters.setEnable(ReportPortalConfig.RUN_REPORT_PORTAL);
  }

  private static void setProjectName(ListenerParameters parameters) {
    if (Objects.nonNull(ReportPortalConfig.RP_PROJECT_NAME)) {
      parameters.setProjectName(ReportPortalConfig.RP_PROJECT_NAME);
    } else if (Objects.isNull(parameters.getProjectName()) && ReportPortalConfig.RUN_REPORT_PORTAL) {
      LOG.warn("ReportPortal project name is not set");
    }
  }

  private static void setLaunchName(ListenerParameters parameters) {
    String launchName = System.getProperty("rpLaunchName", ReportPortalConfig.RP_LAUNCH_NAME);
    if (Objects.nonNull(launchName)) {
      parameters.setLaunchName(launchName);
    } else if (ReportPortalConfig.RUN_REPORT_PORTAL) {
      LOG.warn("ReportPortal launch name is not set");
    }
  }

  private static void setLaunchDescription(ListenerParameters parameters) {
    String description = StringUtils.EMPTY;
    if (StringUtils.isNotEmpty(BRANCH)) {
      String branchUrl = "%s/-/tree/%s".formatted(System.getenv("CI_PROJECT_URL"), BRANCH);
      String userName = System.getenv("GITLAB_USER_NAME");
      String jobUrl = System.getenv("CI_JOB_URL");
      String jobId = System.getenv("CI_JOB_ID");
      String pipelineUrl = System.getenv("CI_PIPELINE_URL");
      String pipelineId = System.getenv("CI_PIPELINE_ID");
      description += """
          **Branch:** [%s](%s)%n\
          **Run by user:** %s%n\
          **CI job:** [ID_%s](%s)%n\
          **CI pipeline:** [ID_%s](%s)%n\
          """.formatted(
          BRANCH, branchUrl,
          userName,
          jobId, jobUrl,
          pipelineId, pipelineUrl);
    }
    if (parameters.getEnable() && isTestRailEnabled()) {
      description = setTestrailDataForLaunchDescription(description);
    }
    String rcVersion = System.getenv("RC_VERSION");
    if (StringUtils.isNotBlank(rcVersion)) {
      description += "**Release candidate version:** [%s]".formatted(rcVersion);
    }
    String rpDefaultDescription = StringUtils.defaultIfBlank(parameters.getDescription(), StringUtils.EMPTY);
    parameters.setDescription("%s%s".formatted(description, rpDefaultDescription));
  }

  private static boolean isTestRailEnabled() {
    return !TESTRAIL_CONFIG.isTestrailDisabled() && StringUtils.isNotEmpty(TESTRAIL_CONFIG.testRailId());
  }

  private static String setTestrailDataForLaunchDescription(String description) {
    TestRailService testRailService = TestRailService.getInstance();
    if (testRailService.isTestPlan() && StringUtils.isNotBlank(TESTRAIL_CONFIG.testRailTestPlanUrl())) {
      String testRailPlanId = testRailService.getTestPlan().getId().toString();
      return "%s **TestRail plan id:** [%s](%s)%n"
          .formatted(description, testRailPlanId, TESTRAIL_CONFIG.testRailTestPlanUrl().formatted(testRailPlanId));
    } else if (testRailService.isTestRun() && StringUtils.isNotBlank(TESTRAIL_CONFIG.testRailTestRunUrl())) {
      String testRailRunId = testRailService.getTestRun().getId().toString();
      return "%s **TestRail run id:** [%s](%s)%n"
          .formatted(description, testRailRunId, TESTRAIL_CONFIG.testRailTestRunUrl().formatted(testRailRunId));
    }
    return description;
  }

  private static void setLaunchMode(ListenerParameters parameters) {
    final String rpLaunchMode = Objects.isNull(ReportPortalConfig.RP_LAUNCH_MODE)
        ? Mode.DEBUG.name()
        : ReportPortalConfig.RP_LAUNCH_MODE;
    parameters.setLaunchRunningMode(Mode.valueOf(rpLaunchMode));
  }

  private static StartLaunchRQ buildStartLaunch(ListenerParameters parameters) {
    StartLaunchRQ startLaunchRequest = new StartLaunchRQ();
    startLaunchRequest.setName(parameters.getLaunchName());
    startLaunchRequest.setStartTime(Calendar.getInstance().getTime());
    startLaunchRequest.setAttributes(parameters.getAttributes());
    startLaunchRequest.setMode(parameters.getLaunchRunningMode());
    startLaunchRequest.setDescription(parameters.getDescription());
    return startLaunchRequest;
  }
}
