package io.automation.config;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Mutable;
import org.aeonbits.owner.Reloadable;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({"system:env"})
public interface TestNGExtension extends Config, Reloadable, Mutable {

  @Key("RETRY_COUNT")
  @DefaultValue("2")
  Integer retryCount();

  @Key("TEST_GROUP")
  String testGroup();

  @Key("EXECUTION_MODE")
  @DefaultValue("LOCAL")
  ExecutionMode mode();

  enum ExecutionMode {
    LOCAL, CI
  }
}
