package io.automation.constant;

import java.util.Locale;

import io.automation.config.ConfigRegistry;
import io.automation.config.LocaleConfig;

/**
 * Supported date formats with default locale resolution.
 */
public enum DateFormat implements DateType {
  YYYY_MM_DD("yyyy-MM-dd"),
  YYYY_MM_DD_HH_MM_SS_ISO("yyyy-MM-dd'T'HH:mm:ss.SSSVV"),
  YYYY_MM_DD_HH_MM_SS_MS_ISO("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSSX");

  private final String pattern;

  private static final LocaleConfig CONFIG = ConfigRegistry.get(LocaleConfig.class);

  DateFormat(String pattern) {
    this.pattern = pattern;
  }

  @Override
  public Locale getDefaultLocale() {
    return Locale.forLanguageTag(CONFIG.locale());
  }

  @Override
  public String getPattern() {
    return pattern;
  }
}