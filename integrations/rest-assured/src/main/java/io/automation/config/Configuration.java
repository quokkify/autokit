package io.automation.config;

import org.aeonbits.owner.Config;

/**
 * Interface for config with api settings.
 */
@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({"system:env", "classpath:api-config.properties"})
public interface Configuration extends Config {

  @Key("MAX_RESPONSE_TIME_SECONDS")
  Long maxResponseTimeSeconds();
}
