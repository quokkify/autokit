package io.automation.service.verifications.google;

import io.automation.model.Verification;
import io.automation.page.google.SearchResultPage;
import io.automation.service.Browser;
import io.automation.service.steps.google.SearchResultPageSteps;
import io.automation.util.Waiter;
import io.qameta.allure.Step;
import org.assertj.core.api.Assertions;

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
    Assertions.assertThat(Browser.getUrl()).as("Page url is incorrect").startsWith(expectedUrl);
    return this;
  }
}
