package io.automation.test;

import java.util.Locale;

import io.automation.annotation.TestGroup;
import io.automation.formatter.JwtKeyPairFormatter;
import io.automation.formatter.JwtTokenFormatter;
import io.automation.generator.JwtHeaderGenerator;
import io.automation.generator.JwtKeyPairGenerator;
import io.automation.generator.JwtPayloadGenerator;
import io.automation.generator.JwtTokenGenerator;
import io.automation.model.Header;
import io.automation.model.JwtKeyPair;
import io.automation.model.JwtToken;
import io.automation.model.Payload;
import io.automation.util.JsonConverter;
import io.qameta.allure.TmsLink;
import net.datafaker.Faker;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class JwtTest {

  private static final Faker FAKER = new Faker(Locale.ENGLISH);
  private Header header;
  private Payload payload;
  private JwtKeyPair generatedJwtKeyPair;

  @BeforeClass
  public void prepareTestData() {
    long userId = FAKER.number().randomNumber(15);
    String ip = FAKER.internet().ipV4Address();
    header = JwtHeaderGenerator.generateRs512(FAKER.number().digit());
    payload = JwtPayloadGenerator.generate(userId, ip);
    generatedJwtKeyPair = JwtKeyPairGenerator.generateRs512();
  }

  @TmsLink("JWT_ID_1")
  @TestGroup("Jwt")
  @Test(description = "Verify JWT functionality using JwtKeyPair")
  public void testJwtFunctionalityUsingJwtKeyPair() {
    JwtToken jwtToken = JwtTokenGenerator.generate(generatedJwtKeyPair, header, payload);

    SoftAssertions.assertSoftly(softly -> {
      softly.assertThat(jwtToken.header()).as("Header is incorrect").isEqualTo(header);
      softly.assertThat(jwtToken.payload()).as("Payload is incorrect").isEqualTo(payload);
    });
  }

  @TmsLink("JWT_ID_2")
  @TestGroup("Jwt")
  @Test(description = "Verify JWT functionality using private and public keys")
  public void testJwtFunctionalityPrivatePublicKeys() {
    String publicKey = JwtKeyPairFormatter.formatPublicKey(generatedJwtKeyPair);
    String privateKey = JwtKeyPairFormatter.formatPrivateKey(generatedJwtKeyPair);
    JwtKeyPair jwtKeyPair = JwtKeyPairGenerator.generateRs512(privateKey, publicKey);
    JwtToken jwtToken = JwtTokenGenerator.generate(jwtKeyPair, header, payload);

    SoftAssertions.assertSoftly(softly -> {
      softly.assertThat(jwtToken.header()).as("Header is incorrect").isEqualTo(header);
      softly.assertThat(jwtToken.payload()).as("Payload is incorrect").isEqualTo(payload);
    });
  }

  @TmsLink("JWT_ID_3")
  @TestGroup("Jwt")
  @Test(description = "Decrypt payload JSON from compact JWT and map to Payload")
  public void testDecryptPayloadFromJwt() {
    String compactJwt = JwtTokenGenerator.generateAsString(generatedJwtKeyPair, header, payload);
    String payloadJson = JwtTokenFormatter.decryptPayloadAsString(generatedJwtKeyPair, compactJwt);
    Payload actual = JsonConverter.fromString(payloadJson, Payload.class);
    SoftAssertions.assertSoftly(softly -> softly.assertThat(actual)
        .as("Decrypted payload is incorrect")
        .isEqualTo(payload));
  }
}
