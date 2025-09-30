package io.automation.impl;

import java.util.Locale;

import io.automation.config.ConfigRegistry;
import io.automation.config.LocaleConfig;
import io.automation.spi.LocaleProvider;

public class LocaleProviderImpl implements LocaleProvider {

  @Override
  public Locale getLocale() {
    String tag = ConfigRegistry.get(LocaleConfig.class).locale();
    return Locale.forLanguageTag(tag);
  }
}
