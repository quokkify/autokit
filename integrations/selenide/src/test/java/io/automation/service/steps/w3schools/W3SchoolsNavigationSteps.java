package io.automation.service.steps.w3schools;

import io.automation.model.Navigation;
import io.automation.page.w3school.HtmlHorizontalTablePage;
import io.automation.page.w3school.HtmlTablesPage;
import io.qameta.allure.Step;

public class W3SchoolsNavigationSteps extends Navigation {

  public W3SchoolsNavigationSteps(String baseUrl) {
    super(baseUrl);
  }

  @Step("Open 'HTML Tables' page")
  public HtmlTablesPageSteps openHtmlTablePage() {
    return new HtmlTablesPageSteps(openPage(HtmlTablesPage.class));
  }

  @Step("Open 'HTML Horizontal Tables' page")
  public HtmlHorizontalTablePageSteps openHtmlHorizontalTablePage() {
    return new HtmlHorizontalTablePageSteps(openPage(HtmlHorizontalTablePage.class));
  }
}
