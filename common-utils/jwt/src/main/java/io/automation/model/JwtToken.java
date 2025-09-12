package io.automation.model;

/**
 * JWT token object.
 */
public record JwtToken(
    Header header,
    Payload payload,
    String token
) { }