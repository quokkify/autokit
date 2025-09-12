package io.automation.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Objects;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Utils for working with reflection.
 */
public final class ReflectionUtils {

  private ReflectionUtils() {
  }

  /**
   * Get generic class type by index.
   * throw an exception if class not parameterized.
   */
  public static Class<?> getGenericClassType(Class<?> clazz, int genericIndex) {
    Type genericSuperclass = clazz.getGenericSuperclass();
    if (!(genericSuperclass instanceof ParameterizedType parameterizedType)) {
      throw new IllegalStateException("Superclass of %s is not parameterized."
          .formatted(clazz.getCanonicalName()));
    }

    Type[] args = parameterizedType.getActualTypeArguments();
    if (genericIndex < 0 || genericIndex >= args.length) {
      throw new IllegalStateException("Generic index %d out of bounds [0..%d] for %s"
          .formatted(genericIndex, args.length - 1, clazz.getCanonicalName()));
    }
    Type type = args[genericIndex];
    if (type instanceof Class<?> c) {
      return c;
    }
    if (type instanceof ParameterizedType pt) {
      Type raw = pt.getRawType();
      if (raw instanceof Class<?> c) return c;
    }
    if (type instanceof TypeVariable<?> tv) {
      Type[] bounds = tv.getBounds();
      if (bounds.length > 0) {
        Type b = bounds[0];
        if (b instanceof Class<?> c) return c;
        if (b instanceof ParameterizedType bpt && bpt.getRawType() instanceof Class<?> c2) return c2;
      }
    }
    if (type instanceof WildcardType wt) {
      Type[] ub = wt.getUpperBounds();
      if (ub.length > 0) {
        Type b = ub[0];
        if (b instanceof Class<?> c) return c;
        if (b instanceof ParameterizedType bpt && bpt.getRawType() instanceof Class<?> c2) return c2;
      }
    }

    throw new IllegalStateException("Cannot resolve generic class for index %d in %s"
        .formatted(genericIndex, clazz.getCanonicalName()));
  }

  /**
   * Search class in superclass chain and return it if found (exact match).
   */
  public static Class<?> getClassByTypeFromHierarchy(Class<?> source, Class<?> target) {
    Class<?> current = source;
    while (current != null) {
      if (current.equals(target)) {
        return current;
      }
      current = current.getSuperclass();
    }
    throw new IllegalStateException("Cannot find class '%s' in hierarchy of '%s'"
        .formatted(target.getCanonicalName(), Objects.requireNonNull(source).getCanonicalName()));
  }

  /**
   * Get any method from {@link Class} with reflection.
   */
  public static Method getMethodWithAccessible(Class<?> sourceClass,
                                               String methodName,
                                               Class<?>... parameterTypes)
      throws NoSuchMethodException {
    Method method = sourceClass.getDeclaredMethod(methodName, parameterTypes);
    method.setAccessible(true);
    return method;
  }

  /**
   * Get field from {@link Class} with reflection.
   */
  @SuppressFBWarnings("REFLF_REFLECTION_MAY_INCREASE_ACCESSIBILITY_OF_FIELD")
  public static Field getFieldWithAccessible(Class<?> sourceClass, String fieldName)
      throws NoSuchFieldException {
    Field field = sourceClass.getDeclaredField(fieldName);
    field.setAccessible(true);
    return field;
  }
}