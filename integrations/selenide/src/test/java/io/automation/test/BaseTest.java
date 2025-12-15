package io.automation.test;

import java.util.Objects;

import io.automation.config.BrowserConfiguration;
import io.automation.config.ConfigRegistry;
import io.automation.config.Configuration;
import io.automation.service.Browser;
import io.automation.service.steps.google.GoogleNavigationSteps;
import io.automation.service.steps.w3schools.W3SchoolsNavigationSteps;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeSuite;

public class BaseTest {

  private static final Configuration APP_CONFIG = ConfigRegistry.get(Configuration.class);
  private static final BrowserConfiguration BROWSER_CONFIGURATION = ConfigRegistry.get(BrowserConfiguration.class);

  protected GoogleNavigationSteps googleNavigationSteps = new GoogleNavigationSteps(APP_CONFIG.baseUrl());
  protected W3SchoolsNavigationSteps w3SchoolsNavigationSteps = new W3SchoolsNavigationSteps(APP_CONFIG.baseUrl());

  @BeforeSuite(alwaysRun = true)
  protected void beforeSuite() {
    Browser.setDefaultConfigurations();
    com.codeborne.selenide.Configuration.headless = false;
    if (Objects.nonNull(BROWSER_CONFIGURATION.remoteUrl())) {
      Browser.setRemoteDefaultConfiguration();
    }
  }

  @AfterMethod(alwaysRun = true)
  protected void closeWebDriver() {
    Browser.closeWebDriver();
  }
}
