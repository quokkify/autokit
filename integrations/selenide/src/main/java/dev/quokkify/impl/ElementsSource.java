package dev.quokkify.impl;

import java.util.List;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

import dev.quokkify.elements.base.Component;

/**
 * Interface for UI elements collection source classes.
 */
public interface ElementsSource<T extends Component> {

  @CheckReturnValue
  @Nonnull
  List<T> getElements();
}
