package io.automation.impl;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.util.List;

import io.automation.elements.base.Component;

/**
 * Interface for UI elements collection source classes.
 */
public interface ElementsSource<T extends Component> {

  @CheckReturnValue
  @Nonnull
  List<T> getElements();
}
