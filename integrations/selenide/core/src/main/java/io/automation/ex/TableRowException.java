package io.automation.ex;

import java.util.Map;

import com.codeborne.selenide.ex.UIAssertionError;
import org.apache.commons.lang3.StringUtils;

/**
 * Custom table row exception.
 */
public class TableRowException extends UIAssertionError {

  public TableRowException(Enum<?> columnName, String value) {
    super("No row with '%s' value in '%s' column".formatted(value,
        StringUtils.capitalize(columnName.name().toLowerCase())));
  }

  public <T extends Enum<?>> TableRowException(Map<T, String> expectedRowValues) {
    super("No row with expected values: %s".formatted(expectedRowValues));
  }
}
