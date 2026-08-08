package dev.quokkify.constant;

import java.time.Duration;

/**
 * Polling intervals in milliseconds.
 */
public enum PollingInterval {
  MILLIS_5000(5000),
  MILLIS_3000(3000),
  MILLIS_1000(1000),
  MILLIS_500(500),
  MILLIS_100(100);

  private final int millis;

  PollingInterval(int millis) {
    this.millis = millis;
  }

  public int getMillis() {
    return millis;
  }

  public Duration duration() {
    return Duration.ofMillis(millis);
  }
}
