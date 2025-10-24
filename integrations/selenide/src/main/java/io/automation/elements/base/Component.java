package io.automation.elements.base;

import com.codeborne.selenide.Container;
import com.codeborne.selenide.SelenideElement;

/**
 * Abstract class with common methods for Ui elements and blocks.
 */
public abstract class Component implements Container {

  @Self
  private SelenideElement self;

  public SelenideElement getSelf() {
    return self;
  }
}
