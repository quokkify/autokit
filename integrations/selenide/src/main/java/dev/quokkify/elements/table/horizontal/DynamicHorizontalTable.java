package dev.quokkify.elements.table.horizontal;

import java.util.function.Function;

import com.codeborne.selenide.Condition;
import dev.quokkify.model.ConstantFormat;
import org.openqa.selenium.By;

/**
 * Dynamic Horizontal Table UI element and methods of working with it.
 * Table has a variable number of columns.
 *
 * @param <T> enum with columns enumerations
 */
public class DynamicHorizontalTable<T extends Enum<T> & ConstantFormat> extends BaseHorizontalTable<T> {

  /**
   * Fetch column index by column title text in the UI table.
   */
  @Override
  protected Function<T, Integer> fetchColumnIndex() {
    return column -> getSelf().shouldBe(Condition.visible)
        .findAll(By.xpath(".//tr/th"))
        .findBy(Condition.text(column.capitalize()))
        .findAll(By.xpath("./parent::tr/preceding-sibling::tr"))
        .size();
  }
}
