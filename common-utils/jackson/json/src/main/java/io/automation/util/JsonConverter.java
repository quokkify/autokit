package io.automation.util;

import java.io.File;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * Utilities for JSON serialization and deserialization using Jackson.
 *
 * <p>Notes:
 * <ul>
 *   <li>No checked exceptions are thrown; they are wrapped into {@link RuntimeException}.</li>
 *   <li>Configured with {@link JavaTimeModule} and sane date defaults.</li>
 * </ul>
 * </p>
 */
public final class JsonConverter {

  private static final ObjectMapper JSON = JsonMapper.builder()
      .addModule(new JavaTimeModule())
      .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
      .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
      .build();

  private static final ObjectMapper JSON_NON_NULL = JsonMapper.builder()
      .addModule(new JavaTimeModule())
      .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
      .defaultPropertyInclusion(JsonInclude.Value.construct(JsonInclude.Include.NON_NULL, JsonInclude.Include.ALWAYS))
      .build();

  private JsonConverter() {
  }

  /**
   * =========================
   * Read
   * =========================
   */

  public static <T> T fromObject(Object obj, Class<T> clazz) {
    try {
      return JSON.readValue(toJson(obj), clazz);
    } catch (Exception e) {
      throw new RuntimeException("Failed to deserialize from JSON (Object->" + clazz.getSimpleName() + ")", e);
    }
  }

  public static <T> T fromObject(Object obj, TypeReference<T> typeRef) {
    try {
      return JSON.readValue(toJson(obj), typeRef);
    } catch (Exception e) {
      throw new RuntimeException("Failed to deserialize from JSON (Object->TypeReference)", e);
    }
  }

  public static <T> T fromString(String json, Class<T> clazz) {
    try {
      return JSON.readValue(json, clazz);
    } catch (Exception e) {
      throw new RuntimeException("Failed to deserialize from JSON (String->" + clazz.getSimpleName() + ")", e);
    }
  }

  public static <T> T fromString(String json, TypeReference<T> typeRef) {
    try {
      return JSON.readValue(json, typeRef);
    } catch (Exception e) {
      throw new RuntimeException("Failed to deserialize from JSON (String->TypeReference)", e);
    }
  }

  public static <T> T fromFile(File jsonFile, Class<T> clazz) {
    try {
      return JSON.readValue(jsonFile, clazz);
    } catch (Exception e) {
      throw new RuntimeException("Failed to read JSON file: " + jsonFile, e);
    }
  }

  public static <T> T fromFile(File jsonFile, TypeReference<T> typeRef) {
    try {
      return JSON.readValue(jsonFile, typeRef);
    } catch (Exception e) {
      throw new RuntimeException("Failed to read JSON file (TypeReference): " + jsonFile, e);
    }
  }

  /**
   * Deserialize JSON with a parametric type, e.g., Response&lt;Item&gt;.
   */
  public static <T> T fromStringParametric(String json, Class<T> outerClass, Class<?> paramClass) {
    try {
      JavaType javaType = JSON.getTypeFactory().constructParametricType(outerClass, paramClass);
      return JSON.readValue(json, javaType);
    } catch (Exception e) {
      throw new RuntimeException("Failed to deserialize JSON with parametric type", e);
    }
  }

  /**
   * =========================
   * Write
   * =========================
   */

  public static String toJson(Object obj) {
    try {
      return JSON.writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException("Failed to serialize to JSON", e);
    }
  }

  public static String toJsonIgnoreNulls(Object obj) {
    try {
      return JSON_NON_NULL.writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException("Failed to serialize to JSON (ignore nulls)", e);
    }
  }
}
