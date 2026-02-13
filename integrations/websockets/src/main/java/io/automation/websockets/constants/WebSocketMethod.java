package io.automation.websockets.constants;

import io.automation.model.ConstantFormat;
import lombok.AllArgsConstructor;

/**
 * Enum representing the available WebSocket methods for use in the generating type of message.
 * Provides methods to retrieve the constant values in a formatted way.
 *
 * <p>This enum implements the {@link ConstantFormat} interface, allowing it to
 * expose the formatted constant values through the {@link #formatValue()} method.</p>
 *
 * <ul>
 * <li>{@code CONNECT} - Used to initiate a WebSocket connection.</li>
 * <li>{@code SUBSCRIBE} - Used to subscribe to a topic in a WebSocket session.</li>
 * <li>{@code UNSUBSCRIBE} - Used to unsubscribe from a topic in a WebSocket session.</li>
 * </ul>
 */
@AllArgsConstructor
public enum WebSocketMethod implements ConstantFormat {
  CONNECT, SUBSCRIBE, UNSUBSCRIBE, PUBLISH;

  @Override
  public String formatValue() {
    return name();
  }
}
