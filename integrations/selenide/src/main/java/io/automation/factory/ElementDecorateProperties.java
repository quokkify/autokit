package io.automation.factory;

import java.lang.reflect.Type;
import java.util.Arrays;

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
    genericTypes = (genericTypes == null) ? new Type[0] : Arrays.copyOf(genericTypes, genericTypes.length);
  }

  @Override
  public Type[] genericTypes() {
    return Arrays.copyOf(genericTypes, genericTypes.length);
  }
}
