package dev.quokkify.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import com.codeborne.selenide.SelenideElement;
import dev.quokkify.elements.base.Component;
import dev.quokkify.elements.base.ComponentsCollection;

/**
 * Class for wrapping {@link SelenideElement} to {@link ComponentsCollection} collection element.
 */
public class CollectionWrapper {

  private static final String FIELD_SELF = "self";

  private CollectionWrapper() {
  }

  /**
   * Wrap {@link SelenideElement} to UI element {@link T} according to provided class type.
   *
   * @param selenideElement element to wrap
   * @param classType       wrapped element class type
   * @return wrapped UI element as {@link T}
   */
  public static <T extends Component> T wrap(SelenideElement selenideElement, Class<T> classType) {
    Constructor<?> constructor;
    try {
      constructor = classType.getDeclaredConstructor();
    } catch (NoSuchMethodException exception) {
      throw new RuntimeException("Class '%s' has no default constructor".formatted(classType), exception);
    }
    T element;
    try {
      element = (T) constructor.newInstance();
    } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
    initElementSelfField(element, selenideElement);
    return element;
  }

  /**
   * Init UI element {@link T} self field.
   *
   * @param element element to init field
   * @param value   value of self field
   */
  private static <T extends Component> void initElementSelfField(T element, SelenideElement value) {
    Class<?> classType = element.getClass();
    while (classType != Object.class) {
      if (classType == Component.class) {
        Field self;
        try {
          self = classType.getDeclaredField(FIELD_SELF);
        } catch (NoSuchFieldException e) {
          throw new RuntimeException(e);
        }
        self.setAccessible(true);
        try {
          self.set(element, value);
        } catch (IllegalAccessException e) {
          throw new RuntimeException(e);
        }
      }
      classType = classType.getSuperclass();
    }
  }
}
