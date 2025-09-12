package io.automation.config;

import org.aeonbits.owner.Config;

/**
 * Interface for config with locale settings.
 */
public interface LocaleConfig extends Config {

  @Key("LOCALE")
  @DefaultValue("en")
  String locale();
}
