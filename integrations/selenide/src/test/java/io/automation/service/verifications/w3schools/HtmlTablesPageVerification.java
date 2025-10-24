package io.automation.service.verifications.w3schools;

import java.util.Map;

import io.automation.elements.table.model.CheckType;
import io.automation.elements.table.model.RowData;
import io.automation.model.Verification;
import io.automation.page.w3school.HtmlTablesPage;
import io.automation.service.steps.w3schools.HtmlTablesPageSteps;
import io.qameta.allure.Step;

public class HtmlTablesPageVerification extends Verification<HtmlTablesPageSteps, HtmlTablesPage> {

  public HtmlTablesPageVerification(HtmlTablesPageSteps steps, HtmlTablesPage page) {
    super(steps, page);
  }

  @Step("Verify table row")
  @Deprecated(since = "0.4.31")
  public HtmlTablesPageVerification verifyTableRow(String company,
                                                   Map<HtmlTablesPage.Header, String> expectedRowValues) {
    page.getTableRowWithValueInColumn(company).verifyRow(expectedRowValues);
    return this;
  }

  @Step("Verify table row")
  public HtmlTablesPageVerification verifyTableRow(String company, UiTableTest.Firm firm) {
    page.getTableRowWithValueInColumn(company)
        .verifyRow(RowData.<HtmlTablesPage.Header>builder()
            .add(CheckType.EQUALS, HtmlTablesPage.Header.CONTACT, firm.getContract())
            .add(CheckType.CONTAINS, HtmlTablesPage.Header.COUNTRY, firm.getCountry())
            .build());
    return this;
  }

  @Step("Verify table row found from several values")
  public HtmlTablesPageVerification verifyTableRow(Map<HtmlTablesPage.Header, String> actualRowValues,
                                                   UiTableTest.Firm firm) {
    page.getTableRowWithValuesInColumns(actualRowValues)
        .verifyCell(HtmlTablesPage.Header.CONTACT, firm.getContract());
    return this;
  }
}
