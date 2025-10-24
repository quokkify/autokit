package io.automation.pages.w3school;

import com.codeborne.selenide.Selenide;
import io.automation.annotation.PageUrl;
import io.automation.elements.table.horizontal.HorizontalRow;
import io.automation.elements.table.horizontal.HorizontalTable;
import io.automation.model.ConstantFormat;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

@PageUrl("/horizontal_table")
public class HtmlHorizontalTablePage extends BaseTablePage {

  @FindBy(how = How.XPATH, using = "//table[2]")
  private HorizontalTable<Header> table;

  public HtmlHorizontalTablePage() {
    Selenide.switchTo().frame("iframeResult");
  }

  public HorizontalRow<Header> getTableRowByColumn(Header header) {
    return table.getRow(header);
  }

  public enum Header implements ConstantFormat {
    NAME, TELEPHONE_1, TELEPHONE_2;

    @Override
    public String formatValue() {
      return name();
    }
  }
}
