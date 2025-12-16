package io.automation.service;

import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import com.browserup.bup.BrowserUpProxy;
import com.browserup.bup.proxy.CaptureType;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.FileDownloadMode;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import de.sstoehr.harreader.model.Har;
import io.automation.config.BrowserConfiguration;
import io.automation.config.ConfigRegistry;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.MutableCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class to work with browser.
 */
public class Browser {

  private static final BrowserConfiguration CONFIG = ConfigRegistry.get(BrowserConfiguration.class);
  private static final Duration DURATION = Duration.ofSeconds(10);

  private static final Logger LOG = LoggerFactory.getLogger(Browser.class);

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
   * Set remote configurations to browser.
   */
  public static void setRemoteDefaultConfiguration() {
    Configuration.remote = CONFIG.remoteUrl();
    Configuration.browserCapabilities.setCapability("se:downloadsEnabled", true);
  }

  /**
   * Merge two {@link Capabilities} together and return the union of the two as a new {@link Capabilities} instance.
   * Capabilities from {@code other} will override those in {@code this}.
   *
   * @param capabilities {@link Capabilities} from selenium lib
   * @return {@link MutableCapabilities} mutable obj
   */
  public static MutableCapabilities mergeCapabilities(Capabilities capabilities) {
    return Configuration.browserCapabilities.merge(capabilities);
  }

  /**
   * Close web driver.
   */
  public static void closeWebDriver() {
    Selenide.closeWebDriver();
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
   * Switch to first frame
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
   * Check that it is remote connection.
   *
   * @return true or false
   */
  public static boolean isRemote() {
    return Objects.nonNull(Configuration.remote);
  }

  /**
   * Get HTML source.
   *
   * @return the source (HTML) of current page as {@link String}
   */
  public static String source() {
    return WebDriverRunner.source();
  }

  /**
   * Open browser and add proxy request headers.
   *
   * @param requestHeaders proxy filters request headers
   */
  public static void openBrowserAndAddProxyRequestHeaders(Map<String, String> requestHeaders) {
    open();
    addProxyRequestHeaders(requestHeaders);
  }

  /**
   * Open browser.
   */
  public static void open() {
    Selenide.open();
  }

  /**
   * Add proxy request filter headers.
   *
   * @param headers request filter headers as {@link Map}&lt;{@link String}, {@link String}&gt;
   */
  public static void addProxyRequestHeaders(Map<String, String> headers) {
    getProxy().addHeaders(headers);
  }

  /**
   * Enable har recording.
   */
  public static void newProxyHar() {
    BrowserUpProxy browserUpProxy = getProxy();
    browserUpProxy.setHarCaptureTypes(CaptureType.getAllContentCaptureTypes());
    browserUpProxy.enableHarCaptureTypes(CaptureType.REQUEST_CONTENT, CaptureType.RESPONSE_CONTENT);
    browserUpProxy.newHar(UUID.randomUUID().toString());
  }

  /**
   * Get browser proxy recorded har.
   *
   * @return browser har as {@link Har}
   */
  public static Har getProxyHar() {
    return getProxy().getHar();
  }

  /**
   * Disable har recording.
   *
   * @return previous recorded har as {@link Har}
   */
  public static Har endProxyHar() {
    return getProxy().endHar();
  }

  /**
   * Get browser proxy.
   *
   * @return browser proxy as {@link BrowserUpProxy}
   */
  public static BrowserUpProxy getProxy() {
    return WebDriverRunner.getSelenideProxy().getProxy();
  }
}
