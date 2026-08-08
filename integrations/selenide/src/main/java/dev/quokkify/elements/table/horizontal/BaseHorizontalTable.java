package dev.quokkify.elements.table.horizontal;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import dev.quokkify.elements.base.BaseTable;
import dev.quokkify.html.model.HtmlTag;
import dev.quokkify.model.ConstantFormat;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

/**
 * Abstract class to work with Horizontal Table.
 *
 * @param <T> enum with columns enumerations
 */
public abstract class BaseHorizontalTable<T extends Enum<T> & ConstantFormat> extends BaseTable<T> {

  /**
   * Get row by column.
   *
   * @param columnHeader column enum
   * @return horizontal table {@link HorizontalRow} element
   */
  public HorizontalRow<T> getRow(T columnHeader) {
    return getAllRows().get(fetchColumnIndex().apply(columnHeader));
  }

  /**
   * Get existing row status by row header.
   *
   * @param columnHeader column enum
   * @return true if any row has provided header, otherwise false
   */
  public boolean isRowExist(T columnHeader) {
    return isRowExist(columnHeader.upperCaseWithSpace());
  }

  /**
   * Get existing row status by row header.
   *
   * @param columnHeaderTitle column header title
   * @return true if any row has provided header, otherwise false
   */
  public boolean isRowExist(String columnHeaderTitle) {
    return getAllColumns().asDynamicIterable().stream().anyMatch(row -> row.text().equals(columnHeaderTitle));
  }

  @Override
  public HorizontalRow<T> getFirstRow() {
    return getAllRows().stream()
        .findFirst()
        .orElseThrow(() -> new RuntimeException("No horizontal rows found"));
  }

  /**
   * Get columns and values as map.
   *
   * @return {@link Map}
   */
  public Map<String, String> columnsAndValuesAsMap() {
    List<String> keys = getAllColumnsNames();
    List<String> values = getAllRowsValues();
    return IntStream.range(0, keys.size()).boxed()
        .collect(Collectors.toMap(keys::get, values::get));
  }

  /**
   * Get all rows as values.
   *
   * @return list of rows values
   */
  private List<String> getAllRowsValues() {
    return getAllRows().stream()
        .map(row -> row.getSelf().getText())
        .collect(Collectors.toList());
  }

  @Override
  public List<HorizontalRow<T>> getAllRows() {
    return this.getSelf().findAll(By.tagName(HtmlTag.TD)).asFixedIterable().stream()
        .map((Function<SelenideElement, HorizontalRow<T>>) HorizontalRow::new).collect(Collectors.toList());
  }
}
