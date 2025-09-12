package io.automation.constant;

import java.time.Duration;

public enum Timeout {
  SECONDS_60(60),
  SECONDS_30(30),
  SECONDS_10(10),
  SECONDS_5(5),
  SECONDS_3(3);

  private final int seconds;

  Timeout(int seconds) {
    this.seconds = seconds;
  }

  public int seconds() {
    return seconds;
  }

  public int milliseconds() {
    return seconds * 1000;
  }

  public Duration duration() {
    return Duration.ofSeconds(seconds);
  }
}