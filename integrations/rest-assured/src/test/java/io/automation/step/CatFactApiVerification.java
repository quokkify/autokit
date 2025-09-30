package io.automation.step;

import io.automation.model.CatFactPojo;
import io.automation.verification.ApiVerification;
import io.qameta.allure.Step;
import org.assertj.core.api.Assertions;

public class CatFactApiVerification implements ApiVerification {

  @Step("Verify 'Get Fact'")
  public void verifyGetFactText(CatFactPojo catFactPojo) {
    Assertions.assertThat(catFactPojo.fact()).as("Fact text is empty")
        .isNotBlank();
  }
}
