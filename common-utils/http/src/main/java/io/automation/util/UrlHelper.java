package io.automation.util;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.automation.constant.StringConstant;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.net.URIBuilder;
import org.apache.hc.core5.net.URLEncodedUtils;

/**
 * Utils for creating and editing URL entities.
 */
public final class UrlHelper {

  private UrlHelper() {
  }

  /**
   * Add query parameters to URL path.
   *
   * <p>ex: 'https://example.com' -> 'https://example.com?id=555'</p>
   *
   * <p>ex: 'https://example.com' -> 'https://example.com?id=555&status[]=yes&status[]=no'</p>
   *
   * @param path        URL path to modify
   * @param queryParams parameters to add to URL
   * @return modified URL with query parameters
   * @throws URISyntaxException if the URL is invalid
   */
  public static String addQueryParameters(String path, Map<String, Object> queryParams) throws URISyntaxException {
    List<NameValuePair> parameters = new ArrayList<>();
    queryParams.forEach((key, value) -> {
      Objects.requireNonNull(value, "Query param value must not be null for key: " + key);
      if (value instanceof List<?> list) {
        for (Object element : list) {
          parameters.add(new BasicNameValuePair(key, String.valueOf(element)));
        }
      } else {
        parameters.add(new BasicNameValuePair(key, String.valueOf(value)));
      }
    });
    return new URIBuilder(path).addParameters(parameters).build().toString();
  }

  /**
   * Convert URL to RFC3986 standard by replacing '+' with '%20' in query part.
   *
   * <p>ex: 'https://example.com?comment=test+comment' -> 'https://example.com?comment=test%20comment'</p>
   *
   * @param url URL string
   * @return converted URL string
   */
  public static String convertToRfc3986Standard(String url) {
    final String spaceInRfc3986Standard = "%20";
    return url.replace(StringConstant.PLUS, spaceInRfc3986Standard);
  }

  /**
   * Convert URL {@link String} to {@link URL} using UTF-8 decoding.
   *
   * @param url URL string to convert
   * @return {@link URL}
   * @throws MalformedURLException if malformed
   */
  public static URL convertToUrlWithDecodeUtf8(String url) throws MalformedURLException {
    return convertToUrl(url, StandardCharsets.UTF_8);
  }

  /**
   * Convert URL {@link String} to {@link URL} using provided charset for decoding.
   *
   * @param url           URL string to convert
   * @param decodeCharset charset for decoding
   * @return {@link URL}
   * @throws MalformedURLException if malformed
   */
  public static URL convertToUrl(String url, Charset decodeCharset) throws MalformedURLException {
    return convertToUri(url, decodeCharset).toURL();
  }

  /**
   * Convert URL {@link String} to {@link URL}.
   *
   * @param url URL string to convert
   * @return {@link URL}
   * @throws MalformedURLException if malformed
   */
  public static URL convertToUrl(String url) throws MalformedURLException {
    return convertToUri(url).toURL();
  }

  /**
   * Convert URL {@link String} to {@link URI} using UTF-8 decoding.
   *
   * @param url URL string to convert
   * @return {@link URI}
   */
  public static URI convertToUriWithDecodeUtf8(String url) {
    return convertToUri(url, StandardCharsets.UTF_8);
  }

  /**
   * Convert URL {@link String} to {@link URI} using provided charset for decoding.
   *
   * @param url           URL string to convert
   * @param decodeCharset charset for decoding
   * @return {@link URI}
   */
  public static URI convertToUri(String url, Charset decodeCharset) {
    return convertToUri(URLDecoder.decode(url, decodeCharset));
  }

  /**
   * Convert URL {@link String} to {@link URI}.
   *
   * @param url URL string to convert
   * @return {@link URI}
   */
  public static URI convertToUri(String url) {
    return URI.create(url);
  }

  /**
   * Build page URL with embedded user credentials.
   *
   * @param fullPageUrl base page URL
   * @param login       username
   * @param password    password
   * @return URL with user info
   * @throws URISyntaxException if the URL is invalid
   */
  public static String getPageUrlWithCredentials(String fullPageUrl, String login, String password)
      throws URISyntaxException {
    return new URIBuilder(fullPageUrl)
        .setUserInfo(login, password)
        .toString();
  }

  /**
   * Parse query params from {@link URI}.
   *
   * @param uri URI to parse
   * @return list of name-value pairs
   */
  public static List<NameValuePair> parseParams(URI uri) {
    return URLEncodedUtils.parse(uri, StandardCharsets.UTF_8);
  }

  /**
   * Split URL path by '/'.
   *
   * @param uri URI to split
   * @return path segments
   */
  public static List<String> splitPath(URI uri) {
    return Arrays.asList(uri.getPath().split(StringConstant.SLASH));
  }

  /**
   * Get value from url pairs by name of parameter.
   *
   * @param params    list of url params as {@link List}&lt;{@link NameValuePair}&gt;
   * @param paramName required param key name string
   * @return param value as {@link String}
   */
  public static String getValue(List<NameValuePair> params, String paramName) {
    String found = null;
    int count = 0;
    for (NameValuePair p : params) {
      if (p.getName().equals(paramName)) {
        found = p.getValue();
        count++;
        if (count > 1) break;
      }
    }
    if (count == 1) return found;
    if (count == 0) {
      throw new IllegalArgumentException("Parameter '" + paramName + "' not found");
    }
    throw new IllegalArgumentException("Multiple parameters named '" + paramName + "' found");
  }
}
