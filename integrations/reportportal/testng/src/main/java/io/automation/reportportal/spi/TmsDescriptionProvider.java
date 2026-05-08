package io.automation.reportportal.spi;

public interface TmsDescriptionProvider {

  boolean isEnabled();

  String testCaseUrl(String tmsId);

  String enrichLaunchDescription();
}
