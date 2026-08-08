package dev.quokkify.impl;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;

import dev.quokkify.elements.base.Component;

/**
 * UI elements collection source with filtered elements.
 */
public class FilteredElementsSource<T extends Component> implements ElementsSource<T> {

  private final ElementsSource<T> elementsSource;
  private final Predicate<T> predicate;

  public FilteredElementsSource(ElementsSource<T> elementsSource, Predicate<T> predicate) {
    this.elementsSource = elementsSource;
    this.predicate = predicate;
  }

  @Nonnull
  @Override
  public List<T> getElements() {
    return this.elementsSource.getElements().stream().filter(predicate).collect(Collectors.toList());
  }
}
