package io.automation.converter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * Utilities for XML serialization and deserialization using Jackson XmlMapper.
 *
 * <p>Notes:
 * <ul>
 *   <li>No checked exceptions are thrown; they are wrapped into {@link RuntimeException}.</li>
 *   <li>Configured with {@link JavaTimeModule}, XML declaration disabled by default.</li>
 * </ul>
 * </p>
 */
public final class XmlConverter {

  private static final XmlMapper XML = XmlMapper.builder()
      .addModule(new JavaTimeModule())
      .disable(ToXmlGenerator.Feature.WRITE_XML_DECLARATION)
      .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
      .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
      .build();

  private static final XmlMapper XML_NON_NULL = XmlMapper.builder()
      .addModule(new JavaTimeModule())
      .disable(ToXmlGenerator.Feature.WRITE_XML_DECLARATION)
      .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
      .serializationInclusion(JsonInclude.Include.NON_NULL)
      .build();

  private XmlConverter() {
    // prevent instantiation
  }

  /* =========================
     Read
     ========================= */

  public static <T> T fromObject(Object obj, Class<T> clazz) {
    try {
      return XML.readValue(toXml(obj), clazz);
    } catch (Exception e) {
      throw new RuntimeException("Failed to deserialize from XML (Object->" + clazz.getSimpleName() + ")", e);
    }
  }

  public static <T> T fromObject(Object obj, TypeReference<T> typeRef) {
    try {
      return XML.readValue(toXml(obj), typeRef);
    } catch (Exception e) {
      throw new RuntimeException("Failed to deserialize from XML (Object->TypeReference)", e);
    }
  }

  public static <T> T fromString(String xml, Class<T> clazz) {
    try {
      return XML.readValue(xml, clazz);
    } catch (Exception e) {
      throw new RuntimeException("Failed to deserialize from XML (String->" + clazz.getSimpleName() + ")", e);
    }
  }

  public static <T> T fromString(String xml, TypeReference<T> typeRef) {
    try {
      return XML.readValue(xml, typeRef);
    } catch (Exception e) {
      throw new RuntimeException("Failed to deserialize from XML (String->TypeReference)", e);
    }
  }

  /* =========================
     Write
     ========================= */

  public static String toXml(Object obj) {
    try {
      return XML.writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException("Failed to serialize to XML", e);
    }
  }

  public static String toXmlIgnoreNulls(Object obj) {
    try {
      return XML_NON_NULL.writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException("Failed to serialize to XML (ignore nulls)", e);
    }
  }
}