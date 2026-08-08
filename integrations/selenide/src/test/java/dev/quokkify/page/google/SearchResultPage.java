package dev.quokkify.page.google;

import dev.quokkify.annotation.PageUrl;
import dev.quokkify.elements.base.ComponentsCollection;
import dev.quokkify.elements.single.Block;
import dev.quokkify.elements.single.Link;
import dev.quokkify.impl.Page;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

@PageUrl("/google")
public class SearchResultPage implements Page {

  @FindBy(how = How.CSS, using = "#rso > div")
  private ComponentsCollection<SearchResultBlock> searchResults;

  public int getSearchTitlesCount() {
    return searchResults.size();
  }

  public void clickOnSearchResultLinkByLinkText(String linkText) {
    searchResults.get(resultBlock -> resultBlock.getTitleLinkText().startsWith(linkText),
            "Find by '%s' link text".formatted(linkText))
        .clickOnTitleLink();
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
