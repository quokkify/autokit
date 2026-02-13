package io.automation.websockets.pojos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.automation.model.Pojo;

/**
 * Class for Centrifugo message model.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CentrifugoMessagePublishPojo implements Pojo {

  private String channel;
  private Pojo data;

  public CentrifugoMessagePublishPojo() {
  }

  public CentrifugoMessagePublishPojo(String channel, Pojo data) {
    this.channel = channel;
    this.data = data;
  }

  public static Builder builder() {
    return new Builder();
  }

  public String getChannel() {
    return channel;
  }

  public void setChannel(String channel) {
    this.channel = channel;
  }

  public Pojo getData() {
    return data;
  }

  public void setData(Pojo data) {
    this.data = data;
  }

  public static final class Builder {
    private String channel;
    private Pojo data;

    private Builder() {
    }

    public Builder channel(String channel) {
      this.channel = channel;
      return this;
    }

    public Builder data(Pojo data) {
      this.data = data;
      return this;
    }

    public CentrifugoMessagePublishPojo build() {
      return new CentrifugoMessagePublishPojo(channel, data);
    }
  }
}
