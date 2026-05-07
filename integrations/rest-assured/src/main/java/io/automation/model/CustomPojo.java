package io.automation.model;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.automation.util.JsonConverter;
import io.restassured.response.ValidatableResponse;

public record CustomPojo(ObjectNode json) implements Pojo {

  public CustomPojo() {
    this(JsonNodeFactory.instance.objectNode());
  }

  public CustomPojo(ValidatableResponse response) {
    this(response.extract().asString());
  }

  public CustomPojo(String json) {
    this(JsonConverter.fromString(json, ObjectNode.class));
  }

  @Override
  public String asJson() {
    return JsonConverter.toJson(json);
  }

  @JsonValue
  public ObjectNode json() {
    return json;
  }

  public String asJsonArray() {
    return asJsonArray(this);
  }

  public CustomPojo setField(String field, Object value) {
    json.set(field, JsonConverter.fromObject(value, JsonNode.class));
    return this;
  }

  public static String asJsonArray(CustomPojo... items) {
    ArrayNode array = JsonNodeFactory.instance.arrayNode();
    for (CustomPojo item : items) {
      array.add(item.json);
    }
    return JsonConverter.toJson(array);
  }
}
