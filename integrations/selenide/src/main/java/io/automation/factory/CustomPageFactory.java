package io.automation.factory;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import javax.annotation.CheckReturnValue;
import javax.annotation.ParametersAreNonnullByDefault;

import com.codeborne.selenide.Driver;
import com.codeborne.selenide.impl.SelenidePageFactory;
import com.codeborne.selenide.impl.WebElementSource;
import io.automation.elements.base.Component;
import io.automation.elements.base.ComponentsCollection;
import org.jspecify.annotations.Nullable;
import org.openqa.selenium.By;

/**
 * Custom factory class for initializing Page Objects fields.
 */
@ParametersAreNonnullByDefault
public class CustomPageFactory<T extends Component> extends SelenidePageFactory {

  @Override
  @CheckReturnValue
  @Nullable
  public Object decorate(ClassLoader loader,
                         Driver driver,
                         @Nullable WebElementSource searchContext,
                         Field field,
                         By selector,
                         Type[] genericTypes) {
    if (ComponentsCollection.class.isAssignableFrom(field.getType())) {
      return init(searchContext, field, selector, genericTypes);
    }
    return super.decorate(loader, driver, searchContext, field, selector, genericTypes);
  }

  /**
   * Initialize UI elements list {@link ComponentsCollection}&lt;{@link T}&gt;.
   *
   * @param searchContext collection search context
   * @param field         UI element field
   * @param selector      collection selector
   * @param genericTypes  class generic types
   * @return UI elements list as {@link ComponentsCollection}&lt;{@link T}&gt;
   */
  @SuppressWarnings("unchecked")
  private ComponentsCollection<T> init(@Nullable WebElementSource searchContext,
                                       Field field,
                                       By selector,
                                       Type[] genericTypes) {
    Class<T> listType = (Class<T>) getListGenericType(field, genericTypes);
    var elementDecorateProperties = new ElementDecorateProperties<>(searchContext, listType, selector, genericTypes);
    return new ComponentsCollection<>(this, elementDecorateProperties);
  }
}
