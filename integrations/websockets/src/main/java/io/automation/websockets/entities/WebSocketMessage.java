package io.automation.websockets.entities;

import java.time.LocalDateTime;

public class WebSocketMessage {

  private LocalDateTime timestamp;
  private String message;

  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  public WebSocketMessage setTimestamp(LocalDateTime timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  public String getMessage() {
    return message;
  }

  public WebSocketMessage setMessage(String message) {
    this.message = message;
    return this;
  }
}
