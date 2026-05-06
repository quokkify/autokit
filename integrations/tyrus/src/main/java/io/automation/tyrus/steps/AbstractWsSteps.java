package io.automation.tyrus.steps;

import io.automation.step.AbstractSteps;

public abstract class AbstractWsSteps<V extends WsVerification> extends AbstractSteps<V> {

  protected V verification;

  @Override
  public V verify() {
    return verification;
  }
}
