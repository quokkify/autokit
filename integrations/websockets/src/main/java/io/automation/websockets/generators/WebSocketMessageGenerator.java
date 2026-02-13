package io.automation.websockets.generators;

import java.time.ZoneOffset;
import java.util.UUID;

import io.automation.generator.LocalDateTimeGenerator;
import io.automation.generator.SignatureGenerator;
import io.automation.websockets.constants.WebSocketMethod;
import io.automation.websockets.pojos.ParamsPojo;
import io.automation.websockets.pojos.WebSocketMessageRequestPojo;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

/**
 * Class for generating various types of WebSocket messages.
 * Provides static methods for creating messages for authorization, subscription,
 * and unsubscription actions within a WebSocket connection.
 */
@UtilityClass
public class WebSocketMessageGenerator {

  /**
   * Generates an authorization message for a WebSocket connection.
   * The message includes a unique user token created with an HMAC signature,
   * a user ID, and a current timestamp.
   *
   * @param secret the secret key for HMAC signature generation
   * @param userId the unique identifier of the user
   * @return a {@link WebSocketMessageRequestPojo} object containing authorization parameters
   */
  public static WebSocketMessageRequestPojo generateAuthorizationMessage(String secret, String userId) {
    long timestamp = LocalDateTimeGenerator.generateNow().toEpochSecond(ZoneOffset.UTC);
    String token = SignatureGenerator.generateHmacSignature("%s%s".formatted(userId, timestamp), secret);
    return WebSocketMessageRequestPojo.builder()
        .uid(UUID.randomUUID().toString())
        .method(WebSocketMethod.CONNECT.lowerCase())
        .params(ParamsPojo.builder()
            .user(userId)
            .timestamp(String.valueOf(timestamp))
            .info(StringUtils.EMPTY)
            .token(token)
            .build())
        .build();
  }

  /**
   * Generates an authorization message for Admin role to WebSocket connection.
   *
   * @param token the token HMAC signature generation
   * @return a {@link WebSocketMessageRequestPojo} object containing authorization parameters
   */
  public static WebSocketMessageRequestPojo generateAdminAuthorizationMessage(String token) {
    return WebSocketMessageRequestPojo.builder()
        .method(WebSocketMethod.CONNECT.lowerCase())
        .params(ParamsPojo.builder()
            .token(token)
            .watch(true)
            .build())
        .build();
  }

  /**
   * Generates a subscription message for a specified WebSocket channel.
   * This message allows a user to subscribe to a channel, enabling real-time
   * data delivery for the specified channel.
   *
   * @param channelTitle the title of the WebSocket channel to subscribe to
   * @return a {@link WebSocketMessageRequestPojo} object for the subscription action
   */
  public static WebSocketMessageRequestPojo generateSubscriptionMessage(String channelTitle) {
    return WebSocketMessageRequestPojo.builder()
        .method(WebSocketMethod.SUBSCRIBE.lowerCase())
        .params(ParamsPojo.builder()
            .channel(channelTitle)
            .build())
        .build();
  }

  /**
   * Generates an unsubscription message for a specified WebSocket channel.
   * This message allows a user to stop receiving updates from a previously
   * subscribed channel.
   *
   * @param channelTitle the title of the WebSocket channel to unsubscribe from
   * @return a {@link WebSocketMessageRequestPojo} object for the unsubscription action
   */
  public static WebSocketMessageRequestPojo generateUnsubscriptionMessage(String channelTitle) {
    return WebSocketMessageRequestPojo.builder()
        .method(WebSocketMethod.UNSUBSCRIBE.lowerCase())
        .params(ParamsPojo.builder()
            .channel(channelTitle)
            .build())
        .build();
  }
}
