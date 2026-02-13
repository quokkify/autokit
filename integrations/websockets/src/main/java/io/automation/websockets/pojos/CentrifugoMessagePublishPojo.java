package io.automation.websockets.pojos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.automation.model.Pojo;

/**
 * Class for Centrifugo message model.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public record CentrifugoMessagePublishPojo(String channel, Pojo data) implements Pojo {

  public String getChannel() {
    return channel;
  }

  public Pojo getData() {
    return data;
  }
}
