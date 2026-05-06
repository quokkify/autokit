package io.automation.service.steps.google;

import io.automation.model.Navigation;
import io.automation.page.google.HomePage;
import io.automation.page.google.SearchResultPage;
import io.qameta.allure.Step;

public class GoogleNavigationSteps extends Navigation {

  public GoogleNavigationSteps(String baseUrl) {
    super(baseUrl);
  }

  @Step("Open 'Google' home page")
  public HomePageSteps openHomePage() {
    return new HomePageSteps(openPage(HomePage.class));
  }

  @Step("Open 'Google' search result page")
  public SearchResultPageSteps openSearchResultPage() {
    return new SearchResultPageSteps(openPage(SearchResultPage.class));
  }
}
