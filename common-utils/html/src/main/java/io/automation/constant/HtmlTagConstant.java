package io.automation.constant;

import io.automation.model.ConstantFormat;

public enum HtmlTagConstant implements ConstantFormat {
  A, DIV, I, LI, LINK, OPTION, P, SELECT, SPAN, TABLE, TD, TH, TR;

  @Override
  public String formatValue() {
    return name();
  }
}
