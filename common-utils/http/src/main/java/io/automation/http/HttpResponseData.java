package io.automation.http;

import java.time.Duration;
import java.util.List;
import java.util.Map;

/**
 * Simple HTTP response wrapper.
 */
public record HttpResponseData(
    int statusCode,
    String body,
    Map<String, List<String>> headers,
    Duration duration
) {
}
