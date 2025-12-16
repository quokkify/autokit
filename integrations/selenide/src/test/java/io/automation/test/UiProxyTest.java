package io.automation.test;

import com.codeborne.selenide.Configuration;
import de.sstoehr.harreader.model.Har;
import io.automation.config.ConfigRegistry;
import io.automation.parser.HarParser;
import io.qameta.allure.TmsLink;
import org.apache.http.HttpStatus;
import org.assertj.core.api.Assertions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * NOTE: Other tests could be affected by the test. Should be executed in single thread
 */
public class UiProxyTest extends BaseTest {

  @BeforeClass
  public void enableProxy() {
    Configuration.proxyEnabled = true;
  }

  @AfterClass
  public void disableProxy() {
    Configuration.proxyEnabled = false;
  }

  @TmsLink("UI_PROXY_ID_1")
  @Test(description = "Verify proxy 'Har' recording")
  public void testProxyHar() {
    String searchLinkText = "GitHub";
    String requestUrl = "https://github.githubassets.com/favicons/favicon-dark.svg";

    Har har = googleNavigationSteps.openSearchResultPage()
        .startProxyHarRecording()
        .clickOnSearchResultLink(searchLinkText)
        .stopProxyHarRecording();
    Assertions.assertThat(HarParser.getLastHarEntryResponseStatusCode(har, requestUrl))
        .as("Response status code is incorrect")
        .isEqualTo(HttpStatus.SC_OK);
  }
}
