package io.automation.elements.base;

import com.codeborne.selenide.Container;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebElementCondition;

/**
 * Abstract class with common methods for Ui elements and blocks.
 */
public abstract class Component implements Container {

  @Self
  private SelenideElement self;

  protected SelenideElement getSelf() {
    return self;
  }

  public void click() {
    getSelf().click();
  }

  public String getText() {
    return getSelf().getText();
  }

  public boolean is(WebElementCondition condition) {
    return getSelf().is(condition);
  }
}
