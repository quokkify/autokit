package io.automation.websockets.pojos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.automation.model.Pojo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class for WebSockets message model.
 */
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ParamsPojo implements Pojo {

  private String user;
  private String channel;
  private String timestamp;
  private String info;
  private String token;
  private Boolean watch;
  private Pojo data;
}
