package io.automation.service.steps.w3schools;

import io.automation.model.PageSteps;
import io.automation.page.w3school.HtmlHorizontalTablePage;
import io.automation.service.verifications.w3schools.HtmlHorizontalTablePageVerification;
import io.qameta.allure.Step;

public class HtmlHorizontalTablePageSteps
    extends PageSteps<HtmlHorizontalTablePageSteps, HtmlHorizontalTablePageVerification, HtmlHorizontalTablePage> {

  public HtmlHorizontalTablePageSteps(HtmlHorizontalTablePage page) {
    super.verification = new HtmlHorizontalTablePageVerification(this, page);
    super.page = page;
  }

  @Step("Accept Terms")
  public HtmlHorizontalTablePageSteps acceptTerms() {
    page.clickAcceptTermsButtonIfDisplayed();
    return this;
  }
}
