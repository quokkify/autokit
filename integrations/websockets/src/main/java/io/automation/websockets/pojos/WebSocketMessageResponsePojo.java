package io.automation.websockets.pojos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.automation.model.Pojo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class for WebSockets message response model.
 */
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WebSocketMessageResponsePojo<T> implements Pojo {

  private String uid;
  private String method;
  private String error;
  private String advice;
  private BodyPojo<T> body;
}
