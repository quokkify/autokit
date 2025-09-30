package io.automation.constant;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.base.Joiner;
import io.automation.model.ConstantFormat;

public enum HtmlAttributeConstant implements ConstantFormat {
  CLASS, CLASS_NAME, CONTENT, DATA, DISABLED, FORM, HREF, ID, METHOD, SPAN, SRC, STYLE, TITLE, TYPE, VALUE, BODY,
  ARIA_EXPANDED, DATA_VALUE;

  /**
   * HTML have global attribute data-*.
   * This attribute used to store custom data private to the page or application.
   * data-* can use all html attributes and custom attributes.
   *
   * <p>For example this code returns a data-* attribute with attribute:
   * <pre>
   *   "data-id" = getDataGlobalAttribute(ID);
   * </pre>
   *
   * @param attribute html attribute
   * @return data-*
   */
  public static String getDataGlobalAttribute(HtmlAttributeConstant attribute) {
    return getDataGlobalAttribute(List.of(attribute));
  }

  /**
   * HTML have global attribute data-*.
   * This attribute used to store custom data private to the page or application.
   * data-* can use all html attributes and custom attributes.
   *
   * <p>For example this code returns a data-* attribute with attribute:
   * <pre>
   *   "data-method-id" = getDataGlobalAttribute(List.of(METHOD, ID);
   * </pre>
   *
   * @param attributes some html attributes
   * @return data-*
   */
  public static String getDataGlobalAttribute(List<HtmlAttributeConstant> attributes) {
    return Joiner.on(StringConstant.DASH)
        .join(DATA.lowerCase(), attributes.stream()
            .map(ConstantFormat::lowerCase)
            .collect(Collectors.joining(StringConstant.DASH))
        );
  }

  @Override
  public String formatValue() {
    return name();
  }
}
