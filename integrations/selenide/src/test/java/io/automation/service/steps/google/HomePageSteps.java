package io.automation.service.steps.google;

import io.automation.model.PageSteps;
import io.automation.model.Verification;
import io.automation.page.google.HomePage;
import io.qameta.allure.Step;

public class HomePageSteps extends PageSteps<HomePageSteps, Verification<HomePageSteps, HomePage>, HomePage> {

  public HomePageSteps(HomePage page) {
    super.verification = null;
    super.page = page;
  }

  @Step("Click accept Cookies if displayed")
  public HomePageSteps acceptCookies() {
    page.clickAcceptCookiesButtonIfDisplayed();
    return this;
  }

  @Step("Search '{searchText}' text")
  public SearchResultPageSteps searchText(String searchText) {
    return new SearchResultPageSteps(page.searchText(searchText));
  }
}
