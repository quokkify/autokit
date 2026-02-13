package io.automation.websockets.entities;

import java.time.LocalDateTime;

public record WebSocketMessage(LocalDateTime timestamp, String message) {

  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  public String getMessage() {
    return message;
  }
}
