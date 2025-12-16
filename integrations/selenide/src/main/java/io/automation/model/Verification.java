package io.automation.model;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.automation.impl.Page;

/**
 * Abstract class for verification steps.
 *
 * @param <S> steps class
 * @param <P> page class
 */
@SuppressFBWarnings("URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD")
public abstract class Verification<S extends PageSteps<S, ?, ?>, P extends Page> {

  private final S steps;
  protected P page;

  public Verification(S steps, P page) {
    this.steps = steps;
    this.page = page;
  }

  /**
   * Return to steps chain.
   *
   * @return steps class
   */
  public S backToSteps() {
    return steps;
  }
}
