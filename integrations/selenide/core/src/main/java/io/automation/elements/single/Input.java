package io.automation.elements.single;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.SetValueOptions;
import io.automation.constant.DateType;
import io.automation.elements.base.Component;
import io.automation.formatter.LocalDateFormatter;
import io.automation.formatter.LocalDateTimeFormatter;
import io.automation.html.model.HtmlAttribute;
import io.automation.html.model.HtmlTag;
import org.openqa.selenium.By;

/**
 * Input UI element and methods of working with it.
 */
public class Input extends Component {

  public Input pressEnter() {
    getSelf().pressEnter();
    return this;
  }

  /**
   * Clear input value.
   *
   * @return this
   */
  public Input clear() {
    getSelf().clear();
    return this;
  }

  /**
   * Send keys with a long value.
   *
   * @param value expected input value
   * @return this
   */
  public Input sendKeys(Long value) {
    return sendKeys(String.valueOf(value));
  }

  /**
   * Send keys with an integer value.
   *
   * @param value expected input value
   * @return this
   */
  public Input sendKeys(Integer value) {
    return sendKeys(String.valueOf(value));
  }

  /**
   * Send keys with a double value.
   *
   * @param value expected input value
   * @return this
   */
  public Input sendKeys(Double value) {
    return sendKeys(String.valueOf(value));
  }

  /**
   * Send keys with a LocalDate value.
   *
   * @param localDate expected input value
   * @param dateType  format of date
   * @return this
   */
  public Input sendKeys(LocalDate localDate, DateType dateType) {
    return sendKeys(LocalDateFormatter.format(localDate, dateType));
  }

  /**
   * Send keys with a LocalDateTime value.
   *
   * @param localDateTime expected input value
   * @param dateType      format of date and time
   * @return this
   */
  public Input sendKeys(LocalDateTime localDateTime, DateType dateType) {
    return sendKeys(LocalDateTimeFormatter.format(localDateTime, dateType));
  }

  /**
   * Send keys with text.
   *
   * @param text expected input value
   * @return this
   */
  public Input sendKeys(String text) {
    getSelf().sendKeys(text);
    return this;
  }

  /**
   * Set value to given input element.
   *
   * @param value text to enter into the text field
   * @return {@link SelenideElement}
   */
  public SelenideElement setValue(String value) {
    return getSelf().setValue(value);
  }

  /**
   * Set value to given input element.
   *
   * @param setValueOptions value option to enter into the text field
   * @return {@link SelenideElement}
   */
  public SelenideElement setValue(SetValueOptions setValueOptions) {
    return getSelf().setValue(setValueOptions);
  }

  /**
   * Get placeholder text from input.
   *
   * @return placeholder text
   */
  public String getPlaceholder() {
    return getSelf().getAttribute(HtmlAttribute.PLACEHOLDER);
  }

  /**
   * Close specific input.
   * ex: input with a datepicker
   *
   * @return this
   */
  public Input close() {
    getSelf().find(By.xpath("./ancestor::%s".formatted(HtmlTag.BODY))).click();
    return this;
  }
}
