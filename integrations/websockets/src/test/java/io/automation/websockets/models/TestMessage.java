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

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private String test;

    private Builder() {
    }

    public Builder test(String test) {
      this.test = test;
      return this;
    }

    public TestMessage build() {
      return new TestMessage(test);
    }
  }
}
