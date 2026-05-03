Generates RS512-signed JWT tokens for use in test authentication flows.

## Dependency

```gradle
testImplementation project(":common-utils:jwt")
```

## Environment variables

| Variable          | Description                          |
|-------------------|--------------------------------------|
| `JWT_PRIVATE_KEY` | Base64-encoded RSA private key (PEM) |
| `JWT_PUBLIC_KEY`  | Base64-encoded RSA public key (PEM)  |

## Initialization in BaseTest

```java
private static JwtKeyPair keyPair;
private static Header header;

@BeforeClass
public static void initJwt() {
    String privateKey = System.getenv("JWT_PRIVATE_KEY");
    String publicKey  = System.getenv("JWT_PUBLIC_KEY");
    keyPair = JwtKeyPairGenerator.generateRs512(privateKey, publicKey);
    header  = JwtHeaderGenerator.generateRs512("key-id-1");
}
```

## Usage in tests

```java
@Test
public void loginWithGeneratedToken() {
    Payload payload = JwtPayloadGenerator.generate("session-abc", "user-42", "10.0.0.1");
    String token = JwtTokenGenerator.generateAsString(keyPair, header, payload);

    given().header("Authorization", "Bearer " + token)
           .get("/api/profile")
           .then().statusCode(200);
}
```

For tests that don't need a real key pair:

```java
JwtKeyPair randomPair = JwtKeyPairGenerator.generateRs512();
JwtToken   jwtToken   = JwtTokenGenerator.generate(randomPair, header, payload);
String     raw        = jwtToken.token();
```

## Key API

| Method                                                       | Returns    | Notes                              |
|--------------------------------------------------------------|------------|------------------------------------|
| `JwtKeyPairGenerator.generateRs512(privateKey, publicKey)`   | `JwtKeyPair` | Keys are Base64-encoded strings  |
| `JwtKeyPairGenerator.generateRs512()`                        | `JwtKeyPair` | Random pair, for isolated tests  |
| `JwtHeaderGenerator.generateRs512(keyId)`                    | `Header`     | Reuse across tests                |
| `JwtPayloadGenerator.generate(session, userId, ip)`          | `Payload`    | Sets `iat=now`, `exp=tomorrow`   |
| `JwtPayloadGenerator.generate(userId, ip)`                   | `Payload`    | Minimal payload, no session      |
| `JwtTokenGenerator.generate(keyPair, header, payload)`       | `JwtToken`   | Use `.token()` for raw string    |
| `JwtTokenGenerator.generateAsString(keyPair, header, payload)` | `String`   | Convenience shorthand            |
