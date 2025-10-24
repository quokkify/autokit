package io.automation.elements.base;

import com.codeborne.selenide.Container;
import com.codeborne.selenide.ElementsCollection;

/**
 * Class for working with UI elements collection.
 */
public class ComponentCollection<T extends Component> {

  @Container.Self
  private ElementsCollection self;

  public ElementsCollection getSelf() {
    return self;
  }
}