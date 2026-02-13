package io.automation.websockets.entities;

import lombok.Data;

/**
 * Class for represents a WebSocket channel associated with a specific user and a unique channel title.
 * This class provides a formatted title based on the channel name and user ID.
 */
@Data
public class WebSocketChannelEntity {

  private final long userId;
  private final String title;

  /**
   * Constructs a {@code WebSocketChannel} with the specified channel name and user ID.
   * The {@code title} field is initialized as a formatted string, combining the
   * {@code channelName} and {@code userId} in the format "{@code channelName#userId}".
   *
   * @param channelName the name of the WebSocket channel
   * @param userId      the unique ID of the user associated with this WebSocket channel
   */
  public WebSocketChannelEntity(String channelName, long userId) {
    this.userId = userId;
    this.title = "%s#%d".formatted(channelName, userId);
  }
}
