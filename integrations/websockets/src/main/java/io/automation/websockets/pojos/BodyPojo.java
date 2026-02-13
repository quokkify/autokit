package io.automation.websockets.pojos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.automation.model.Pojo;
import lombok.Data;

/**
 * Class for WebSockets message response model.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BodyPojo<T> implements Pojo {

  private String channel;
  private Boolean status;
  private String last;
  private Object messages;
  private Boolean recovered;
  private String version;
  private Boolean expires;
  private Boolean expired;
  private Integer ttl;
  private String uid;
  private String client;
  private InfoPojo info;
  private T data;
}
