package io.automation.constant;

import io.automation.model.ConstantFormat;

public enum HtmlClassNameConstant implements ConstantFormat {
  OPTION, ITEM;

  @Override
  public String formatValue() {
    return name();
  }
}
