package dev.quokkify.elements.table.classic;

import java.util.List;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

/**
 * Flex Table UI element and methods of working with it.
 * Table consist of react flex table rows.
 *
 * @param <T> enum with columns enumerations
 */
public class FlexTable<T extends Enum<T>> extends Table<T> {

  @Override
  public List<String> getAllColumnValuesByXpath(T columnHeader, String xpathAdditionalLocator) {
    return this.getSelf().findAll(By.xpath(".//div[%d]%s".formatted(this
        .fetchColumnIndex().apply(columnHeader) + HTML_START_INDEX, xpathAdditionalLocator))).texts();
  }

  @Override
  protected ElementsCollection getAllRowsElements() {
    ElementsCollection rowsWithHeader = this.getSelf()
        .shouldBe(Condition.visible).findAll(By.cssSelector(".flex-table-row"));
    return rowsWithHeader.last(rowsWithHeader.size() - HTML_START_INDEX);
  }

  /**
   * Create row by selenide element.
   *
   * @param element selenide element for  table row
   * @return table {@link Row} element
   */
  @Override
  protected Row<T> mapToRow(SelenideElement element) {
    return new Row<>(element, fetchColumnIndex()) {

      @Override
      public Cell<T> getCell(T columnHeader) {
        int cellIndex = fetchColumnIndex().apply(columnHeader) + HTML_START_INDEX;
        return new Cell<>(this.getSelf().find(By.xpath("./div[%d]".formatted(cellIndex))), this);
      }
    };
  }
}
