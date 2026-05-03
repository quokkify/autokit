# Signature

Generates RSA and HMAC cryptographic signatures and provides AES encryption utilities for test data and config decryption.

## Dependency

```gradle
testImplementation project(":common-utils:signature")
```

## Usage

Generate an RSA signature from a private key and raw data:

```java
String base64Signature = SignatureGenerator.generateRsaSignature(privateKey, data, "SHA256withRSA");
```

Generate an HMAC signature (returned as lowercase hex):

```java
String hexSignature = SignatureGenerator.generateHmacSignature(data, secretKey);
```

Encode or decode values with the Base64 helpers:

```java
String encoded = EncryptionUtils.encodeBytes(rawBytes);
byte[] decoded = EncryptionUtils.decodeString(encoded);
```
