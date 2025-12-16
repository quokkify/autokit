package io.automation.elements.single.dropdown;

import io.automation.html.model.HtmlTag;
import org.openqa.selenium.By;

/**
 * React Dropdown menu looking like a button.
 */
public class ReactDropdown extends SimpleDropdown {

  @Override
  protected By getOptionsContainerSelector() {
    return By.xpath("./ancestor::%s".formatted(HtmlTag.BODY));
  }
}
