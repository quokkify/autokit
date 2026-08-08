package dev.quokkify.test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.NoSuchElementException;

import com.codeborne.selenide.Configuration;
import de.sstoehr.harreader.model.Har;
import dev.quokkify.annotation.SingleThread;
import dev.quokkify.config.ConfigRegistry;
import dev.quokkify.config.TestNGExtension;
import dev.quokkify.parser.HarParser;
import dev.quokkify.service.ProxyBrowser;
import io.qameta.allure.TmsLink;
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
    if (TESTNG_CONFIG.mode() != TestNGExtension.ExecutionMode.LOCAL) {
      throw new SkipException("Proxy test is skipped for non-local runs");
    }
    Configuration.proxyEnabled = true;
  }

  @AfterClass
  public void disableProxy() {
    Configuration.proxyEnabled = false;
  }

  @TmsLink("UI_PROXY_ID_1")
  @SingleThread
  @Test(description = "Verify proxy 'Har' recording")
  public void testProxyHar() {
    String searchLinkText = "Speed Test";
    String requestRelativePath = "/external/speedtest/assets/speedtestpl-logo.webp";
    String requestUrl = APP_CONFIG.baseUrl() + requestRelativePath;

    ProxyBrowser.newProxyHar();
    googleNavigationSteps.openSearchResultPage()
        .clickOnSearchResultLink(searchLinkText);
    Har har = ProxyBrowser.endProxyHar();

    Assertions.assertThat(getStatusCodeWithFallback(har, requestUrl))
        .as("Response status code is incorrect")
        .isEqualTo(200);
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
