package dev.quokkify.elements.table.horizontal;

import com.codeborne.selenide.SelenideElement;
import dev.quokkify.elements.table.classic.base.BaseCell;

/**
 * Horizontal table cell UI element and methods of working with it.
 */
public class HorizontalCell extends BaseCell {

  public HorizontalCell(SelenideElement element) {
    super(element);
  }

  @Override
  public <T extends Enum<T>> HorizontalRow<T> getRow() {
    return new HorizontalRow<>(getSelf().parent());
  }
}
