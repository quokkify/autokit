package io.automation.test;

import io.qameta.allure.TmsLink;
import org.testng.annotations.Test;

public class ElementsCollectionTest extends BaseTest {

  @TmsLink("UI_ID_6")
  @Test(description = "Verify 'CustomElementsCollection'")
  public void testVerifyCustomElementsCollection() {
    String searchLinkText = "QUOKKIFY git";
    String searchResultLinkUrl = "https://github.com/ylazakovich/quokkify";

    googleNavigationSteps.openSearchResultPage()
        .clickOnSearchResultLink(searchLinkText)
        .verify()
        .verifyOpenedPageUrl(searchResultLinkUrl);
  }
}
