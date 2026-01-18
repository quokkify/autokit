package io.automation.service.verifications.google;

import io.automation.model.Verification;
import io.automation.page.google.SearchResultPage;
import io.automation.service.Browser;
import io.automation.service.steps.google.SearchResultPageSteps;
import io.automation.util.Waiter;
import io.qameta.allure.Step;
import org.assertj.core.api.Assertions;

import java.net.URI;
import java.net.URISyntaxException;

public class SearchResultPageVerification extends Verification<SearchResultPageSteps, SearchResultPage> {

  public SearchResultPageVerification(SearchResultPageSteps steps, SearchResultPage page) {
    super(steps, page);
  }

  @Step("Verify that search results exist")
  public SearchResultPageVerification verifySearchResultsExist() {
    Waiter.awaitAssertion(() ->
        Assertions.assertThat(page.getSearchTitlesCount()).as("The search results not exists").isPositive());
    return this;
  }

  @Step("Verify opened page url")
  public SearchResultPageVerification verifyOpenedPageUrl(String expectedUrl) {
    String actualUrl = Browser.getUrl();
    if (!actualUrl.startsWith(expectedUrl)) {
      String normalizedActual = dropPort(actualUrl);
      String normalizedExpected = dropPort(expectedUrl);
      Assertions.assertThat(normalizedActual).as("Page url is incorrect").startsWith(normalizedExpected);
      return this;
    }
    Assertions.assertThat(actualUrl).as("Page url is incorrect").startsWith(expectedUrl);
    return this;
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
