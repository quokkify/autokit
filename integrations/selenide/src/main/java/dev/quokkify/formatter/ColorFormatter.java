package dev.quokkify.formatter;

import dev.quokkify.elements.base.Component;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.support.Color;

/**
 * Util class for color formatter.
 */
public class ColorFormatter {

  private ColorFormatter() {
  }

  /**
   * Convert RGB border color to hex.
   *
   * @param element ui element
   * @param <T>     like {@link Component} or any of parents
   * @return hex as {@link String}
   */
  public static <T extends Component> String convertRgbBorderColorToHex(T element) {
    return convertElementRgbColorToHex(element, CssValue.BORDER_COLOR);
  }

  /**
   * Convert RGB background color to hex.
   *
   * @param element ui element
   * @param <T>     like {@link Component} or any of parents
   * @return hex as {@link String}
   */
  public static <T extends Component> String convertRgbBackgroundColorToHex(T element) {
    return convertElementRgbColorToHex(element, CssValue.BACKGROUND_COLOR);
  }

  /**
   * Convert RGB background color to hex.
   *
   * @param element ui element
   * @param <T>     like {@link SelenideElement}
   * @return hex as {@link String}
   */
  public static <T extends SelenideElement> String convertRgbBackgroundColorToHex(T element) {
    return convertElementRgbColorToHex(element, CssValue.BACKGROUND_COLOR);
  }

  /**
   * Convert element RGB color to hex.
   *
   * @param element  ui element
   * @param cssValue type of html attribute
   * @param <T>      like {@link Component} or any of parents
   * @return hex as {@link String}
   */
  private static <T extends Component> String convertElementRgbColorToHex(T element, CssValue cssValue) {
    return Color.fromString(element.getSelf().getCssValue(cssValue.value)).asHex();
  }

  /**
   * Convert element RGB color to hex.
   *
   * @param element  ui element
   * @param cssValue type of html attribute
   * @param <T>      like {@link Component} or any of parents
   * @return hex as {@link String}
   */
  private static <T extends SelenideElement> String convertElementRgbColorToHex(T element, CssValue cssValue) {
    return Color.fromString(element.getCssValue(cssValue.value)).asHex();
  }

  /**
   * Type of html attribute.
   */
  private enum CssValue {
    BORDER_COLOR("border-color"),
    BACKGROUND_COLOR("background-color");

    public final String value;

    CssValue(String value) {
      this.value = value;
    }
  }
}
