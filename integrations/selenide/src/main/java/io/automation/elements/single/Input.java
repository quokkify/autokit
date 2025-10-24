package io.automation.elements.single;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.SetValueOptions;
import io.automation.elements.base.Component;
import io.automation.html.model.HtmlAttribute;
import io.automation.html.model.HtmlTag;
import org.openqa.selenium.By;

/**
 * Input UI element and methods of working with it.
 */
public class Input extends Component {

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
