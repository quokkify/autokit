package io.automation.test;

import java.util.Objects;

import io.automation.config.BrowserConfiguration;
import io.automation.config.ConfigRegistry;
import io.automation.config.Configuration;
import io.automation.config.TestNGExtension;
import io.automation.service.Browser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.automation.service.steps.google.GoogleNavigationSteps;
import io.automation.service.steps.w3schools.W3SchoolsNavigationSteps;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeSuite;

public class BaseTest {

  protected static final Configuration APP_CONFIG = ConfigRegistry.get(Configuration.class);
  protected static final BrowserConfiguration BROWSER_CONFIGURATION = ConfigRegistry.get(BrowserConfiguration.class);
  protected static final TestNGExtension TESTNG_CONFIG = ConfigRegistry.get(TestNGExtension.class);
  private static final Logger LOG = LoggerFactory.getLogger(BaseTest.class);

  protected GoogleNavigationSteps googleNavigationSteps = new GoogleNavigationSteps(APP_CONFIG.baseUrl());
  protected W3SchoolsNavigationSteps w3SchoolsNavigationSteps = new W3SchoolsNavigationSteps(APP_CONFIG.baseUrl());

  @BeforeSuite(alwaysRun = true)
  protected void beforeSuite() {
    LOG.debug("BASE_URL={}", APP_CONFIG.baseUrl());
    LOG.debug("EXECUTION_MODE={}", TESTNG_CONFIG.mode());
    Browser.setDefaultConfigurations();
    com.codeborne.selenide.Configuration.headless = true;
    if (Objects.nonNull(BROWSER_CONFIGURATION.remoteUrl())) {
      Browser.setRemoteDefaultConfiguration();
    }
  }

  @AfterMethod(alwaysRun = true)
  protected void closeWebDriver() {
    Browser.closeWebDriver();
  }
}
