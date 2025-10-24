package io.automation.pages.google;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import io.automation.annotation.PageUrl;
import io.automation.constant.StringConstant;
import io.automation.elements.single.Block;
import io.automation.elements.single.Button;
import io.automation.elements.single.Input;
import io.automation.impl.Page;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

@PageUrl(StringConstant.SLASH)
public class HomePage implements Page {

  @FindBy(how = How.XPATH, using = "//button/following-sibling::button")
  private Button acceptCookiesButton;
  @FindBy(how = How.CSS, using = "form")
  private SearchBlock searchBlock;

  public void clickAcceptCookiesButtonIfDisplayed() {
    if (acceptCookiesButton.is(Condition.appear)) {
      acceptCookiesButton.click();
    }
  }

  private static class SearchBlock extends Block {

    @FindBy(how = How.NAME, using = "q")
    private Input searchInput;
  }

  public SearchResultPage searchText(String searchText) {
    searchBlock.searchInput.sendKeys(searchText);
    searchBlock.searchInput.pressEnter();
    return Selenide.page(SearchResultPage.class);
  }
}
