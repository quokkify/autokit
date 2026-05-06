package io.automation.elements.single;

import com.codeborne.selenide.DragAndDropOptions;
import com.codeborne.selenide.SelenideElement;
import io.automation.elements.base.Component;

/**
 * Label UI element and methods of working with it.
 */
public class Label extends Component {

  /**
   * Drag and drop this element to the target.
   *
   * @return this element.
   */
  public SelenideElement dragAndDrop(DragAndDropOptions dragAndDropOptions) {
    return getSelf().dragAndDrop(dragAndDropOptions);
  }
}
