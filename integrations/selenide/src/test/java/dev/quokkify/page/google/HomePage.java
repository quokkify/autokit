package dev.quokkify.page.google;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import dev.quokkify.annotation.PageUrl;
import dev.quokkify.constant.StringConstant;
import dev.quokkify.elements.single.Block;
import dev.quokkify.elements.single.Button;
import dev.quokkify.elements.single.Input;
import dev.quokkify.impl.Page;
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
