package dev.quokkify.elements.base;

import java.util.List;
import java.util.Random;
import java.util.function.Function;

import com.codeborne.selenide.ElementsCollection;
import dev.quokkify.elements.table.classic.base.BaseColumn;
import dev.quokkify.elements.table.classic.base.BaseRow;
import dev.quokkify.html.model.HtmlTag;
import org.openqa.selenium.By;

/**
 * Abstract class to work with table.
 *
 * @param <T> enum with columns enumerations
 */
public abstract class BaseTable<T extends Enum<T>> extends Component {

  private final Random random = new Random();
  protected static final int HEADERS_ROW_INDEX = 0;
  protected static final int HTML_START_INDEX = 1;

  /**
   * Function to get table column index.
   */
  protected abstract Function<T, Integer> fetchColumnIndex();

  /**
   * Get first row in table.
   *
   * @return first {@link BaseRow} element
   */
  public abstract BaseRow getFirstRow();

  /**
   * Get all table rows.
   *
   * @return {@link BaseRow} of all table rows
   */
  public abstract List<? extends BaseRow> getAllRows();

  /**
   * Get any random table row.
   *
   * @return any {@link BaseRow} element
   */
  public BaseRow getAnyRow() {
    List<? extends BaseRow> rows = getAllRows();
    return rows.get(random.nextInt(rows.size()));
  }

  /**
   * Checks is table empty.
   *
   * @return true if table has no rows
   */
  public boolean isTableEmpty() {
    return getAllRows().isEmpty();
  }

  /**
   * Get table column.
   *
   * @param columnHeader column enum
   * @return table {@link BaseColumn} element
   */
  public BaseColumn<T> getColumn(T columnHeader) {
    return new BaseColumn<>(getAllColumns().get(fetchColumnIndex().apply(columnHeader)));
  }

  /**
   * Get all table columns names.
   *
   * @return list of table columns names
   */
  public List<String> getAllColumnsNames() {
    return getAllColumns().texts();
  }

  /**
   * Get all table columns.
   *
   * @return {@link ElementsCollection} of all table columns
   */
  protected ElementsCollection getAllColumns() {
    return this.getSelf().findAll(By.tagName(HtmlTag.TH));
  }
}
