package io.automation.testrail.models;

import io.automation.model.ConstantFormat;

public enum CustomAutomationTypes implements ConstantFormat {
  TO_AUTOMATE, AUTOMATED, MANUAL;

  @Override
  public String formatValue() {
    return name();
  }
}
