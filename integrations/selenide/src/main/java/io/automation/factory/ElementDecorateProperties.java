package io.automation.factory;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Objects;

import com.codeborne.selenide.impl.WebElementSource;
import io.automation.elements.base.Component;
import org.openqa.selenium.By;

/**
 * Class for decorating UI elements list source.
 */
public record ElementDecorateProperties<T extends Component>(
    WebElementSource searchContext,
    Class<T> classType,
    By selector,
    Type[] genericTypes
) {

  public ElementDecorateProperties {
    Objects.requireNonNull(searchContext, "searchContext must not be null");
    Objects.requireNonNull(classType, "classType must not be null");
    Objects.requireNonNull(selector, "selector must not be null");
    genericTypes = (genericTypes == null) ? new Type[0] : Arrays.copyOf(genericTypes, genericTypes.length);
  }

  @Override
  public Type[] genericTypes() {
    return Arrays.copyOf(genericTypes, genericTypes.length);
  }

  public ElementDecorateProperties<T> withSearchContext(WebElementSource newSearchContext) {
    Objects.requireNonNull(newSearchContext, "newSearchContext must not be null");
    return new ElementDecorateProperties<>(newSearchContext, classType, selector, genericTypes);
  }

  public ElementDecorateProperties<T> withClassType(Class<T> newClassType) {
    Objects.requireNonNull(newClassType, "newClassType must not be null");
    return new ElementDecorateProperties<>(searchContext, newClassType, selector, genericTypes);
  }

  public ElementDecorateProperties<T> withSelector(By newSelector) {
    Objects.requireNonNull(newSelector, "newSelector must not be null");
    return new ElementDecorateProperties<>(searchContext, classType, newSelector, genericTypes);
  }

  public ElementDecorateProperties<T> withGenericTypes(Type... newGenericTypes) {
    Type[] copy = (newGenericTypes == null) ? new Type[0] : Arrays.copyOf(newGenericTypes, newGenericTypes.length);
    return new ElementDecorateProperties<>(searchContext, classType, selector, copy);
  }
}
