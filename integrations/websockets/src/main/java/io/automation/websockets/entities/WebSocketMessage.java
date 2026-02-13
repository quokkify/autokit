package io.automation.websockets.entities;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class WebSocketMessage {

  private LocalDateTime timestamp;
  private String message;
}
