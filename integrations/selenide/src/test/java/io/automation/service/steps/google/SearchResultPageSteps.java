package io.automation.service.steps.google;

import io.automation.model.PageSteps;
import io.automation.page.google.SearchResultPage;
import io.automation.service.verifications.google.SearchResultPageVerification;
import io.qameta.allure.Step;

public class SearchResultPageSteps
    extends PageSteps<SearchResultPageSteps, SearchResultPageVerification, SearchResultPage> {

  public SearchResultPageSteps(SearchResultPage page) {
    super.verification = new SearchResultPageVerification(this, page);
    super.page = page;
  }

  @Step("Click on search result link with '{searchResultLinkText}' link text")
  public SearchResultPageSteps clickOnSearchResultLink(String searchResultLinkText) {
    page.clickOnSearchResultLinkByLinkText(searchResultLinkText);
    return this;
  }
}
