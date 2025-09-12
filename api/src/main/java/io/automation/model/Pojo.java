package io.automation.model;

import io.automation.util.JsonConverter;

/**
 * Interface for all types of pojo.
 */
public interface Pojo {

  /**
   * Convert pojo to String.
   *
   * @return pojo as {@link String}
   */
  default String asJson() {
    return JsonConverter.toJson(this);
  }
}
