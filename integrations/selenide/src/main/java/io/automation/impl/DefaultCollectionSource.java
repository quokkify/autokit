package io.automation.impl;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.impl.ElementFinder;
import io.automation.elements.base.Component;
import io.automation.factory.ElementDecorateProperties;

/**
 * Default UI elements collection source.
 */
public class DefaultCollectionSource<T extends Component> implements ElementsSource<T> {

  private final ElementDecorateProperties<T> elementDecorateProperties;

  public DefaultCollectionSource(ElementDecorateProperties<T> elementDecorateProperties) {
    this.elementDecorateProperties = elementDecorateProperties;
  }

  /**
   * Get UI elements list. Wrapped from {@link ElementsCollection}
   *
   * @return list of UI elements as {@link List}&lt;{@link T}&gt;
   */
  @Nonnull
  @Override
  public List<T> getElements() {
    return findElements().asFixedIterable().stream()
        .map(element -> CollectionWrapper.wrap(element, elementDecorateProperties.classType()))
        .collect(Collectors.toList());
  }

  /**
   * Find elements collection according to element decorate properties.
   *
   * @return list of elements as {@link ElementsCollection}
   */
  private ElementsCollection findElements() {
    return Objects.nonNull(elementDecorateProperties.searchContext())
        ? ElementFinder.wrap(SelenideElement.class, elementDecorateProperties.searchContext())
        .findAll(elementDecorateProperties.selector())
        : Selenide.elements(elementDecorateProperties.selector());
  }
}
