package io.automation.config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({"system:properties", "system:env",})
interface Configuration extends Config {

  @Key("BASE_URL")
  String baseUrl();
}
