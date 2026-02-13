package io.automation.websockets.models;

public record Keys(String algorithm, String publicKey, String privateKey) {

  public String getAlgorithm() {
    return algorithm;
  }

  public String getPublicKey() {
    return publicKey;
  }

  public String getPrivateKey() {
    return privateKey;
  }
}
