package io.automation.config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({"system:properties", "system:env"})
public interface Configuration extends Config {

  @Key("BASE_URL")
  @DefaultValue("http://localhost:80")
  String baseUrl();
}
