package io.automation.http;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Basic auth credentials holder.
 */
public record BasicAuth(String username, String password) {

  /**
   * Create value for Authorization header.
   *
   * @return header value like "Basic ..."
   */
  public String asAuthorizationHeader() {
    String token = username + ":" + password;
    String encoded = Base64.getEncoder().encodeToString(token.getBytes(StandardCharsets.UTF_8));
    return "Basic " + encoded;
  }
}
