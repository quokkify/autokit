package io.automation.page.w3school;

import com.codeborne.selenide.Condition;
import io.automation.elements.single.Button;
import io.automation.impl.Page;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class BaseTablePage implements Page {

  @FindBy(how = How.ID, using = "accept-choices")
  private Button acceptTermsButton;

  public void clickAcceptTermsButtonIfDisplayed() {
    if (acceptTermsButton.is(Condition.appear)) {
      acceptTermsButton.click();
    }
  }
}
