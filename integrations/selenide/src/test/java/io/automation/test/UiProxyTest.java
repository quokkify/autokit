package io.automation.test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.NoSuchElementException;

import com.codeborne.selenide.Configuration;
import de.sstoehr.harreader.model.Har;
import io.automation.config.ConfigRegistry;
import io.automation.config.TestNGExtension;
import io.automation.parser.HarParser;
import io.qameta.allure.TmsLink;
import org.apache.http.HttpStatus;
import org.assertj.core.api.Assertions;
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * NOTE: Other tests could be affected by the test. Should be executed in single thread
 */
public class UiProxyTest extends BaseTest {
  private static final TestNGExtension TESTNG_CONFIG = ConfigRegistry.get(TestNGExtension.class);

  @BeforeClass
  public void enableProxy() {
    if (TESTNG_CONFIG.mode() == TestNGExtension.ExecutionMode.DIND) {
      throw new SkipException("Proxy test is skipped for DIND runs");
    }
    Configuration.proxyEnabled = true;
  }

  @AfterClass
  public void disableProxy() {
    Configuration.proxyEnabled = false;
  }

  @TmsLink("UI_PROXY_ID_1")
  @Test(description = "Verify proxy 'Har' recording")
  public void testProxyHar() {
    String searchLinkText = "Speed Test";
    String requestRelativePath = "/external/speedtest/assets/speedtestpl-logo.webp";
    String requestUrl = APP_CONFIG.baseUrl() + requestRelativePath;

    Har har = googleNavigationSteps.openSearchResultPage()
        .startProxyHarRecording()
        .clickOnSearchResultLink(searchLinkText)
        .stopProxyHarRecording();

    Assertions.assertThat(getStatusCodeWithFallback(har, requestUrl))
        .as("Response status code is incorrect")
        .isEqualTo(HttpStatus.SC_OK);
  }

  private static int getStatusCodeWithFallback(Har har, String requestUrl) {
    try {
      return HarParser.getLastHarEntryResponseStatusCode(har, requestUrl);
    } catch (NoSuchElementException missing) {
      String noPort = dropPort(requestUrl);
      if (!noPort.equals(requestUrl)) {
        return HarParser.getLastHarEntryResponseStatusCode(har, noPort);
      }
      throw missing;
    }
  }

  private static String dropPort(String url) {
    try {
      URI uri = new URI(url);
      if (uri.getPort() == -1) {
        return url;
      }
      return new URI(
          uri.getScheme(),
          uri.getUserInfo(),
          uri.getHost(),
          -1,
          uri.getPath(),
          uri.getQuery(),
          uri.getFragment()).toString();
    } catch (URISyntaxException e) {
      return url;
    }
  }
}
