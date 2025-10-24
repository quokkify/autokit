package io.automation.pages.google;

import com.codeborne.selenide.ElementsCollection;
import io.automation.annotation.PageUrl;
import io.automation.elements.single.Block;
import io.automation.elements.single.Link;
import io.automation.impl.Page;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

@PageUrl("/google")
public class SearchResultPage implements Page {

  // TODO: need to wrap ElementsCollection
//  @FindBy(how = How.CSS, using = "#rso > div")
//  private ElementsCollection<SearchResultBlock> searchResults;

//  public int getSearchTitlesCount() {
//    return searchResults.size();
//  }

  public void clickOnSearchResultLinkByLinkText(String linkText) {
//    searchResults.get(resultBlock -> resultBlock.getTitleLinkText().startsWith(linkText),
//            "Find by '%s' link text".formatted(linkText))
//        .clickOnTitleLink();
  }

  public static class SearchResultBlock extends Block {

    @FindBy(how = How.TAG_NAME, using = "h3")
    private Link titleLink;

    public void clickOnTitleLink() {
      titleLink.click();
    }

    public String getTitleLinkText() {
      return titleLink.getText();
    }
  }
}
