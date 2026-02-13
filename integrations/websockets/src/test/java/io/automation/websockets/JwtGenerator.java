package io.automation.websockets;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.core.type.TypeReference;
import io.automation.generator.JwtHeaderGenerator;
import io.automation.generator.JwtKeyPairGenerator;
import io.automation.generator.JwtTokenGenerator;
import io.automation.generator.LocalDateTimeGenerator;
import io.automation.model.Header;
import io.automation.model.JwtKeyPair;
import io.automation.util.JsonConverter;
import io.automation.websockets.models.Keys;
import org.apache.commons.lang3.StringUtils;

public final class JwtGenerator {

  private JwtGenerator() {
  }

  public static String getJwtToken(String keyPath, String channelName, long userId) {
    try {
      List<Keys> keys = JsonConverter.fromString(Files.readString(
          Paths.get(Objects.requireNonNull(CentrifugoTest.class.getClassLoader().getResource(keyPath))
              .toURI())), new TypeReference<>() { });
      Keys keyConfig = keys.getFirst();
      JwtKeyPair jwtKeyPair =
          JwtKeyPairGenerator.generateRs512(formatKey(keyConfig.getPrivateKey()), formatKey(keyConfig.getPublicKey()));
      Header header = JwtHeaderGenerator.generateRs512(StringUtils.EMPTY);
      LocalDateTime localDateTime = LocalDateTimeGenerator.generateNow();
      Map<String, Object> payload = new LinkedHashMap<>();
      payload.put("sub", "%s_%s".formatted(channelName, userId));
      payload.put("iat", localDateTime.toEpochSecond(ZoneOffset.UTC));
      payload.put("exp", localDateTime.plusDays(1).toEpochSecond(ZoneOffset.UTC));
      return JwtTokenGenerator.generateAsString(jwtKeyPair, header, JsonConverter.toJson(payload));
    } catch (URISyntaxException | IOException e) {
      throw new RuntimeException("Unable to build JWT token", e);
    }
  }

  private static String formatKey(String publicKey) {
    return publicKey.replace("\n", "")
        .replaceAll("-+.*?-+", "");
  }
}
