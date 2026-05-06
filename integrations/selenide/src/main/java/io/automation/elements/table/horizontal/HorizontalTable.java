package io.automation.elements.table.horizontal;

import java.util.function.Function;

import io.automation.model.ConstantFormat;

/**
 * Horizontal Table UI element and methods of working with it.
 *
 * @param <T> enum with columns enumerations
 */
public class HorizontalTable<T extends Enum<T> & ConstantFormat> extends BaseHorizontalTable<T> {

  /**
   * Fetch column index by column index in the UI table.
   */
  @Override
  protected Function<T, Integer> fetchColumnIndex() {
    return Enum::ordinal;
  }
}
