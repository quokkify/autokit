package io.automation.elements.table.classic;

import java.util.function.Function;

import com.codeborne.selenide.Condition;
import io.automation.elements.table.classic.base.BaseClassicTable;
import io.automation.html.model.HtmlTag;
import io.automation.model.ConstantFormat;
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
