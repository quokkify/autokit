package dev.quokkify.elements.table.classic;

import java.util.function.Function;

import dev.quokkify.elements.table.classic.base.BaseClassicTable;
import dev.quokkify.html.model.HtmlTag;
import dev.quokkify.model.ConstantFormat;

import com.codeborne.selenide.Condition;
import org.openqa.selenium.By;

/**
 * Dynamic Table UI element and methods of working with it.
 * Table has a variable number of columns.
 *
 * @param <T> enum with columns enumerations
 */
public class DynamicTable<T extends Enum<T> & ConstantFormat> extends BaseClassicTable<T> {

  /**
   * Fetch column index by column title text in the UI table.
   */
  @Override
  protected Function<T, Integer> fetchColumnIndex() {
    return column -> getSelf().shouldBe(Condition.visible)
        .findAll(By.xpath(".//%s//%s".formatted(HtmlTag.THEAD, HtmlTag.TH)))
        .findBy(Condition.text(column.capitalize()))
        .findAll(By.xpath("./preceding-sibling::%s".formatted(HtmlTag.TH)))
        .size();
  }
}
