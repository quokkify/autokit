package io.automation.http;

import java.net.URI;
import java.net.http.HttpClient;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Factory for creating {@link HttpCoreClient} instances.
 */
public final class HttpClientFactory {

  private HttpClientFactory() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static HttpCoreClient create(HttpClientSettings settings) {
    return new HttpCoreClient(settings, buildHttpClient(settings));
  }

  private static HttpClient buildHttpClient(HttpClientSettings settings) {
    HttpClient.Builder builder = HttpClient.newBuilder();
    if (settings.connectTimeout() != null) {
      builder.connectTimeout(settings.connectTimeout());
    }
    if (settings.followRedirects() != null) {
      builder.followRedirects(settings.followRedirects());
    }
    if (settings.httpVersion() != null) {
      builder.version(settings.httpVersion());
    }
    return builder.build();
  }

  public static final class Builder {
    private URI baseUri;
    private String basePath = "";
    private Duration connectTimeout;
    private Duration requestTimeout;
    private Map<String, String> defaultHeaders = new LinkedHashMap<>();
    private BasicAuth basicAuth;
    private HttpClient.Redirect followRedirects = HttpClient.Redirect.NORMAL;
    private HttpClient.Version httpVersion = HttpClient.Version.HTTP_1_1;

    private Builder() {
    }

    public Builder baseUri(String baseUri) {
      this.baseUri = URI.create(baseUri);
      return this;
    }

    public Builder baseUri(URI baseUri) {
      this.baseUri = baseUri;
      return this;
    }

    public Builder basePath(String basePath) {
      this.basePath = basePath == null ? "" : basePath;
      return this;
    }

    public Builder connectTimeout(Duration connectTimeout) {
      this.connectTimeout = connectTimeout;
      return this;
    }

    public Builder requestTimeout(Duration requestTimeout) {
      this.requestTimeout = requestTimeout;
      return this;
    }

    public Builder defaultHeaders(Map<String, String> defaultHeaders) {
      this.defaultHeaders = defaultHeaders == null ? new LinkedHashMap<>() : new LinkedHashMap<>(defaultHeaders);
      return this;
    }

    public Builder basicAuth(String username, String password) {
      this.basicAuth = new BasicAuth(username, password);
      return this;
    }

    public Builder basicAuth(BasicAuth basicAuth) {
      this.basicAuth = basicAuth;
      return this;
    }

    public Builder followRedirects(HttpClient.Redirect followRedirects) {
      this.followRedirects = followRedirects;
      return this;
    }

    public Builder httpVersion(HttpClient.Version httpVersion) {
      this.httpVersion = httpVersion;
      return this;
    }

    public HttpCoreClient build() {
      if (baseUri == null) {
        throw new IllegalStateException("baseUri must be set");
      }
      HttpClientSettings settings = new HttpClientSettings(
          baseUri,
          basePath,
          connectTimeout,
          requestTimeout,
          Map.copyOf(defaultHeaders),
          basicAuth,
          followRedirects,
          httpVersion
      );
      return create(settings);
    }
  }
}
