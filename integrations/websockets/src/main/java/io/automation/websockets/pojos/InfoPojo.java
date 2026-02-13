package io.automation.websockets.pojos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.automation.model.Pojo;
import lombok.Data;

/**
 * Class for WebSockets message response model.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class InfoPojo implements Pojo {

  private String user;
  private String client;
}
