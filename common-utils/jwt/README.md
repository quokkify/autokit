# JWT

Generates signed JWT tokens (RS256 and others) for use in test authentication flows.

## Dependency

```gradle
testImplementation project(":common-utils:jwt")
```

## Usage

Generate a key pair, build header and payload, then produce a token.

```java
KeyPair keyPair = JwtKeyPairGenerator.generate();

JwtHeader header = JwtHeaderGenerator.rs256();
JwtPayload payload = JwtPayloadGenerator.builder()
        .subject("user-123")
        .expiresInSeconds(3600)
        .build();

JwtToken token = JwtTokenGenerator.generate(keyPair, header, payload);
String raw = token.getRaw();
```

To get only the raw string directly:

```java
String jwt = JwtTokenGenerator.generateAsString(keyPair, header, payload);
```
