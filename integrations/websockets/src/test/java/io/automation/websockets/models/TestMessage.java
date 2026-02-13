package io.automation.websockets.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.automation.model.Pojo;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public record TestMessage(String test) implements Pojo {

  public String getTest() {
    return test;
  }
}
