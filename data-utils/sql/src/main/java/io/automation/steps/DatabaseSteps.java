package io.automation.steps;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.automation.step.AbstractSteps;
import io.automation.verification.DatabaseVerification;

@SuppressFBWarnings("UWF_UNWRITTEN_PUBLIC_OR_PROTECTED_FIELD")
public abstract class DatabaseSteps<V extends DatabaseVerification> extends AbstractSteps<V> {

  protected V verification;

  @Override
  public V verify() {
    return verification;
  }
}
