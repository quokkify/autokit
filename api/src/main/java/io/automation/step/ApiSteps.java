package io.automation.step;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.automation.step.AbstractSteps;
import io.automation.verification.ApiVerification;

/**
 * Abstract class for api steps class.
 *
 * @param <V> verification api steps class
 */
@SuppressFBWarnings("UWF_UNWRITTEN_PUBLIC_OR_PROTECTED_FIELD")
public abstract class ApiSteps<V extends ApiVerification> extends AbstractSteps<V> {

  protected V verification;

  @Override
  public V verify() {
    return verification;
  }
}
