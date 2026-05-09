package io.automation.redis.steps;

import io.automation.redis.verification.RedisVerifier;
import io.automation.step.AbstractSteps;
import org.redisson.api.RedissonClient;

public class RedisSteps extends AbstractSteps<RedisVerifier> {

  private final RedissonClient client;

  public RedisSteps(RedissonClient client) {
    this.client = client;
  }

  @Override
  public RedisVerifier verify() {
    return new RedisVerifier(client);
  }
}
