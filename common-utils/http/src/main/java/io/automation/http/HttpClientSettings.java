package io.automation.http;

import java.net.URI;
import java.net.http.HttpClient;
import java.time.Duration;
import java.util.Map;

/**
 * Settings for HTTP client instance.
 */
public record HttpClientSettings(
    URI baseUri,
    String basePath,
    Duration connectTimeout,
    Duration requestTimeout,
    Map<String, String> defaultHeaders,
    BasicAuth basicAuth,
    HttpClient.Redirect followRedirects,
    HttpClient.Version httpVersion
) {
}
