package io.automation.http;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import io.automation.util.UrlHelper;

/**
 * Lightweight HTTP client wrapper based on {@link HttpClient}.
 */
public final class HttpCoreClient {

  private final HttpClientSettings settings;
  private final HttpClient client;

  HttpCoreClient(HttpClientSettings settings, HttpClient client) {
    this.settings = Objects.requireNonNull(settings, "settings");
    this.client = Objects.requireNonNull(client, "client");
  }

  public HttpResponseData get(String path) {
    return request(HttpMethod.GET, path, null, null, null);
  }

  public HttpResponseData get(String path, Map<String, ?> queryParams) {
    return request(HttpMethod.GET, path, queryParams, null, null);
  }

  public HttpResponseData post(String path, String body) {
    return request(HttpMethod.POST, path, null, null, body);
  }

  public HttpResponseData post(String path, Map<String, ?> queryParams, String body) {
    return request(HttpMethod.POST, path, queryParams, null, body);
  }

  public HttpResponseData request(HttpMethod method, String path, Map<String, ?> queryParams,
                                  Map<String, String> headers, String body) {
    URI uri = buildUri(path, queryParams);
    HttpRequest.Builder builder = HttpRequest.newBuilder(uri);
    if (settings.requestTimeout() != null) {
      builder.timeout(settings.requestTimeout());
    }

    Map<String, String> finalHeaders = new LinkedHashMap<>(settings.defaultHeaders());
    if (headers != null && !headers.isEmpty()) {
      finalHeaders.putAll(headers);
    }
    if (settings.basicAuth() != null) {
      finalHeaders.putIfAbsent("Authorization", settings.basicAuth().asAuthorizationHeader());
    }
    for (Map.Entry<String, String> header : finalHeaders.entrySet()) {
      builder.header(header.getKey(), header.getValue());
    }

    HttpRequest.BodyPublisher publisher = body == null
        ? HttpRequest.BodyPublishers.noBody()
        : HttpRequest.BodyPublishers.ofString(body);

    builder.method(method.name(), publisher);

    long start = System.nanoTime();
    try {
      HttpResponse<String> response = client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
      Duration duration = Duration.ofNanos(System.nanoTime() - start);
      return new HttpResponseData(response.statusCode(), response.body(), response.headers().map(), duration);
    } catch (Exception e) {
      throw new RuntimeException("HTTP request failed: " + method + " " + uri, e);
    }
  }

  private URI buildUri(String path, Map<String, ?> queryParams) {
    if (path != null && (path.startsWith("http://") || path.startsWith("https://"))) {
      return buildUriWithQuery(URI.create(path), queryParams);
    }

    String combinedPath = combinePaths(settings.basePath(), path);
    URI base = settings.baseUri();
    URI resolved = combinedPath.isEmpty() ? base : base.resolve(combinedPath);
    return buildUriWithQuery(resolved, queryParams);
  }

  private URI buildUriWithQuery(URI uri, Map<String, ?> queryParams) {
    if (queryParams == null || queryParams.isEmpty()) {
      return uri;
    }
    try {
      Map<String, Object> params = new LinkedHashMap<>();
      for (Map.Entry<String, ?> entry : queryParams.entrySet()) {
        params.put(entry.getKey(), entry.getValue());
      }
      return URI.create(UrlHelper.addQueryParameters(uri.toString(), params));
    } catch (URISyntaxException e) {
      throw new RuntimeException("Failed to build URI with query params: " + uri, e);
    }
  }

  private String combinePaths(String basePath, String path) {
    String left = normalizePath(basePath);
    String right = normalizePath(path);
    if (left.isEmpty()) {
      return right;
    }
    if (right.isEmpty()) {
      return left;
    }
    return left + "/" + right;
  }

  private String normalizePath(String value) {
    if (value == null || value.isBlank()) {
      return "";
    }
    String trimmed = value.trim();
    while (trimmed.startsWith("/")) {
      trimmed = trimmed.substring(1);
    }
    while (trimmed.endsWith("/")) {
      trimmed = trimmed.substring(0, trimmed.length() - 1);
    }
    return trimmed;
  }
}
