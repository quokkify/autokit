package io.automation.service;

import java.time.Duration;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.FileDownloadMode;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import io.automation.config.BrowserConfiguration;
import io.automation.config.ConfigRegistry;
import org.openqa.selenium.Cookie;

/**
 * Class to work with browser.
 */
public class Browser {

  private static final BrowserConfiguration CONFIG = ConfigRegistry.get(BrowserConfiguration.class);
  private static final Duration DURATION = Duration.ofSeconds(10);

  private Browser() {
  }

  /**
   * Set default configurations to browser.
   */
  public static void setDefaultConfigurations() {
    Configuration.browser = CONFIG.browser();
    Configuration.browserSize = CONFIG.browserSize();
    Configuration.reportsFolder = "build/reports/tests";
    Configuration.downloadsFolder = "build/downloads";
    Configuration.fileDownload = FileDownloadMode.FOLDER;
    Configuration.screenshots = false;
  }

  /**
   * Close web driver.
   */
  public static void closeWebDriver() {
    Selenide.closeWebDriver();
  }

  /**
   * Open browser.
   */
  public static void open() {
    Selenide.open();
  }

  /**
   * Get url from browser.
   */
  public static String getUrl() {
    return WebDriverRunner.url();
  }

  /**
   * Refresh browser active page.
   */
  public static void refresh() {
    Selenide.refresh();
  }

  /**
   * Click on browser back button.
   */
  public static void back() {
    Selenide.back();
  }

  /**
   * Get cookie from browser by name.
   *
   * @param cookieName cookie name as {@link String}
   * @return {@link Cookie}
   */
  public static Cookie getCookie(String cookieName) {
    return WebDriverRunner.getWebDriver().manage().getCookieNamed(cookieName);
  }

  /**
   * Add cookie to browser.
   *
   * @param cookie {@link Cookie}
   */
  public static void addCookie(Cookie cookie) {
    WebDriverRunner.getWebDriver().manage().addCookie(cookie);
  }

  /**
   * Clear all browser cookies.
   */
  public static void clearCookies() {
    Selenide.clearBrowserCookies();
  }

  /**
   * Confirm browser alert and add comment.
   *
   * @param comment comment as {@link String}
   */
  public static void confirmAlertWithComment(String comment) {
    confirmAlert();
    addCommentToAlert(comment);
  }

  /**
   * Confirm browser alert.
   */
  public static void confirmAlert() {
    Selenide.confirm();
  }

  /**
   * Confirm browser alert with dialog text.
   *
   * @param expectedDialogText dialog text as {@link String}
   */
  public static void confirmAlert(String expectedDialogText) {
    Selenide.confirm(expectedDialogText);
  }

  /**
   * Dismiss browser alert.
   */
  public static void dismissAlert() {
    Selenide.dismiss();
  }

  /**
   * Dismiss browser alert with dialog text.
   *
   * @param expectedDialogText dialog text as {@link String}
   */
  public static void dismissAlert(String expectedDialogText) {
    Selenide.dismiss(expectedDialogText);
  }

  /**
   * Get browser alert dialog text.
   *
   * @return dialog text as {@link String}
   */
  public static String getAlertText() {
    return Selenide.switchTo().alert().getText();
  }

  /**
   * Add comment to browser alert.
   *
   * @param comment comment as {@link String}
   */
  public static void addCommentToAlert(String comment) {
    Selenide.prompt(comment);
  }

  /**
   * Switch to first frame.
   */
  public static void switchToFirstFrame() {
    switchToFrame(0, DURATION);
  }

  /**
   * Switch to frame.
   *
   * @param frameIndex switched frame index. Note: Started from 0 index.
   * @param duration   timeout duration.
   */
  public static void switchToFrame(int frameIndex, Duration duration) {
    Selenide.switchTo().frame(frameIndex, duration);
  }

  /**
   * Switch to frame by SelenideElement.
   *
   * @param element switched frame element.
   */
  public static void switchToFrame(SelenideElement element) {
    Selenide.switchTo().frame(element.should(Condition.appear, DURATION));
  }

  /**
   * Switch to default page content from frame.
   */
  public static void switchToDefaultContent() {
    Selenide.switchTo().defaultContent();
  }

  /**
   * Check that web driver started and browser opened.
   *
   * @return true or false
   */
  public static boolean isOpened() {
    return WebDriverRunner.hasWebDriverStarted();
  }

  /**
   * Get HTML source.
   *
   * @return the source (HTML) of current page as {@link String}
   */
  public static String source() {
    return WebDriverRunner.source();
  }
}
