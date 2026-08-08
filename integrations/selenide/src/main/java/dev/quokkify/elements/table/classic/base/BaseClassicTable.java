package dev.quokkify.elements.table.classic.base;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import dev.quokkify.elements.base.BaseTable;
import dev.quokkify.elements.table.classic.Row;
import dev.quokkify.ex.TableRowException;
import dev.quokkify.html.model.HtmlTag;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;

/**
 * Abstract class to work with Classic Table.
 *
 * @param <T> enum with columns enumerations
 */
public abstract class BaseClassicTable<T extends Enum<T>> extends BaseTable<T> {

  /**
   * Get row by values in given columns.
   *
   * @param expectedRowValues map: key - column, value - cell value
   * @return table {@link Row} element
   */
  public Row<T> getRow(Map<T, String> expectedRowValues) {
    return this.getFilteredRow(this.columnsTextsPredicate(expectedRowValues),
        () -> new TableRowException(expectedRowValues));
  }

  /**
   * Get row by value in given column.
   *
   * @param columnHeader column enum
   * @param cellValue    expected cell value
   * @return table {@link Row} element
   */
  public Row<T> getRow(T columnHeader, String cellValue) {
    return this.getFilteredRow(this.columnTextPredicate(columnHeader, cellValue),
        () -> new TableRowException(columnHeader, cellValue));
  }

  /**
   * Get row by pattern in given column.
   *
   * @param columnHeader column enum
   * @param pattern      expected cell pattern
   * @return table {@link Row} element
   */
  public Row<T> getRowByPattern(T columnHeader, String pattern) {
    return this.getFilteredRow(this.columnTextPredicateByPattern(columnHeader, pattern),
        () -> new TableRowException(columnHeader, pattern));
  }

  /**
   * Get existing row status by values in columns.
   *
   * @param expectedRowValues map: key - column, value - cell value
   * @return true if row contains all values, otherwise false
   */
  public boolean isRowExist(Map<T, String> expectedRowValues) {
    return isRowExist(columnsTextsPredicate(expectedRowValues));
  }

  /**
   * Get existing row status by value in column.
   *
   * @param columnHeader column enum
   * @param cellValue    expected cell value
   * @return true if row value equals expected value, otherwise false
   */
  public boolean isRowExist(T columnHeader, String cellValue) {
    return isRowExist(columnTextPredicate(columnHeader, cellValue));
  }

  /**
   * Get existing row status.
   *
   * @param condition how to filter all rows in the table
   * @return boolean row existing status
   */
  private boolean isRowExist(Predicate<Row<T>> condition) {
    return getAllRows().stream().anyMatch(condition);
  }

  /**
   * Get a filtered row by the given condition.
   *
   * @param condition         condition how to filter all rows in the table
   * @param tableRowException error if no suitable row is found in the table
   * @return filtered row as {@link SelenideElement}
   */
  protected Row<T> getFilteredRow(Predicate<Row<T>> condition,
                                  Supplier<TableRowException> tableRowException) {
    return this.getAllRows().stream().filter(condition).findFirst()
        .orElseThrow(tableRowException);
  }

  /**
   * Get row predicate by expected row values.
   *
   * @param expectedRowValues {@link Map} expected row values
   * @return table {@link Row} element
   */
  private Predicate<Row<T>> columnsTextsPredicate(Map<T, String> expectedRowValues) {
    return row -> expectedRowValues.entrySet().stream()
        .allMatch(entry ->
            row.getCell(entry.getKey()).getSelf().has(Condition.exactTextCaseSensitive(entry.getValue())));
  }

  /**
   * Get row predicate by cell value.
   *
   * @param columnHeader column enum
   * @param cellValue    expected cell value
   * @return table {@link Row} element
   */
  private Predicate<Row<T>> columnTextPredicate(T columnHeader, String cellValue) {
    return row -> row.getCell(columnHeader).getSelf().has(Condition.exactTextCaseSensitive(cellValue));
  }

  /**
   * Get row predicate by cell pattern.
   *
   * @param columnHeader column enum
   * @param pattern      expected cell pattern
   * @return table {@link Row} element
   */
  private Predicate<Row<T>> columnTextPredicateByPattern(T columnHeader, String pattern) {
    return row -> row.getCell(columnHeader).getSelf().getText().matches(pattern);
  }

  /**
   * Get all column values.
   *
   * @param columnHeader column enum
   * @param tag          to specify the html-tag from a cell to get only one value from all cells,
   * @return list of column values
   */
  public List<String> getAllColumnValuesByTag(T columnHeader, String tag) {
    return getAllColumnValuesByXpath(columnHeader, "//%s".formatted(tag));
  }

  /**
   * Get all column values.
   *
   * @param columnHeader column enum
   * @return list of column values
   */
  public List<String> getAllColumnValuesByXpath(T columnHeader) {
    return getAllColumnValuesByXpath(columnHeader, StringUtils.EMPTY);
  }

  /**
   * Get all column values.
   *
   * @param columnHeader           column enum
   * @param xpathAdditionalLocator additional xPath locator to get a specific element in a cell
   * @return list of column values
   */
  public List<String> getAllColumnValuesByXpath(T columnHeader, String xpathAdditionalLocator) {
    return getSelf().findAll(By.xpath(".//%s[%d]%s".formatted(
        HtmlTag.TD,
            fetchColumnIndex().apply(columnHeader) + HTML_START_INDEX,
            xpathAdditionalLocator)
        ))
        .texts();
  }

  /**
   * Get first row from table.
   *
   * @return table {@link Row} element
   */
  @Override
  public Row<T> getFirstRow() {
    return getAllRows().stream()
        .findFirst()
        .orElseThrow(() -> new RuntimeException("No rows found"));
  }

  /**
   * Get all table rows.
   *
   * @return all table {@link Row} element
   */
  @Override
  public List<Row<T>> getAllRows() {
    return getAllRowsElements().asFixedIterable().stream()
        .map(this::mapToRow).collect(Collectors.toList());
  }

  /**
   * Map element to row object.
   *
   * @param element {@link SelenideElement}
   * @return table {@link Row} element
   */
  protected Row<T> mapToRow(SelenideElement element) {
    return new Row<>(element, fetchColumnIndex());
  }

  /**
   * Get all rows as elements collection.
   *
   * @return {@link ElementsCollection}
   */
  protected ElementsCollection getAllRowsElements() {
    ElementsCollection rowsWithHeader = this.getSelf().shouldBe(Condition.visible).findAll(By.tagName(HtmlTag.TR));
    return rowsWithHeader.last(rowsWithHeader.size() - HTML_START_INDEX);
  }
}
