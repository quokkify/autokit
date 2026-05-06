package io.automation.service;

import java.util.Objects;

import com.codeborne.selenide.Configuration;
import io.automation.config.BrowserConfiguration;
import io.automation.config.ConfigRegistry;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.MutableCapabilities;

/**
 * Class to work with browser on Selenium Grid (remote WebDriver).
 */
public class GridBrowser {

  private static final BrowserConfiguration CONFIG = ConfigRegistry.get(BrowserConfiguration.class);

  private GridBrowser() {
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
   * Check that it is remote connection.
   *
   * @return true or false
   */
  public static boolean isRemote() {
    return Objects.nonNull(Configuration.remote);
  }
}
