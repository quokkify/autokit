package dev.quokkify.elements.single;

import dev.quokkify.elements.base.Component;

import com.codeborne.selenide.DragAndDropOptions;
import com.codeborne.selenide.SelenideElement;

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
