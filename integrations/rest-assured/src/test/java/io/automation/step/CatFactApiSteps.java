package io.automation.step;

import io.automation.helper.MockApiHelper;
import io.automation.model.CatFactPojo;
import io.qameta.allure.Step;

public class CatFactApiSteps extends ApiSteps<CatFactApiVerification> {

  public CatFactApiSteps() {
    this.verification = new CatFactApiVerification();
  }

  @Step("Get Cat Fact")
  public CatFactPojo getCatFact() {
    return MockApiHelper.getCatFact();
  }
}
