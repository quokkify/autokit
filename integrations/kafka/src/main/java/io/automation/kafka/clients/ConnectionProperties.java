package io.automation.kafka.clients;

public class ConnectionProperties {

  private final String bootstrapServers;
  private final String securityProtocol;
  private final String sslTruststoreLocation;
  private final String sslTruststorePassword;
  private final String sslKeystoreLocation;
  private final String sslKeystorePassword;

  private ConnectionProperties(Builder builder) {
    this.bootstrapServers = builder.bootstrapServers;
    this.securityProtocol = builder.securityProtocol;
    this.sslTruststoreLocation = builder.sslTruststoreLocation;
    this.sslTruststorePassword = builder.sslTruststorePassword;
    this.sslKeystoreLocation = builder.sslKeystoreLocation;
    this.sslKeystorePassword = builder.sslKeystorePassword;
  }

  public static Builder builder() {
    return new Builder();
  }

  public String getBootstrapServers() {
    return bootstrapServers;
  }

  public String getSecurityProtocol() {
    return securityProtocol;
  }

  public String getSslTruststoreLocation() {
    return sslTruststoreLocation;
  }

  public String getSslTruststorePassword() {
    return sslTruststorePassword;
  }

  public String getSslKeystoreLocation() {
    return sslKeystoreLocation;
  }

  public String getSslKeystorePassword() {
    return sslKeystorePassword;
  }

  public static final class Builder {
    private String bootstrapServers;
    private String securityProtocol;
    private String sslTruststoreLocation;
    private String sslTruststorePassword;
    private String sslKeystoreLocation;
    private String sslKeystorePassword;

    private Builder() {
    }

    public Builder bootstrapServers(String bootstrapServers) {
      this.bootstrapServers = bootstrapServers;
      return this;
    }

    public Builder securityProtocol(String securityProtocol) {
      this.securityProtocol = securityProtocol;
      return this;
    }

    public Builder sslTruststoreLocation(String sslTruststoreLocation) {
      this.sslTruststoreLocation = sslTruststoreLocation;
      return this;
    }

    public Builder sslTruststorePassword(String sslTruststorePassword) {
      this.sslTruststorePassword = sslTruststorePassword;
      return this;
    }

    public Builder sslKeystoreLocation(String sslKeystoreLocation) {
      this.sslKeystoreLocation = sslKeystoreLocation;
      return this;
    }

    public Builder sslKeystorePassword(String sslKeystorePassword) {
      this.sslKeystorePassword = sslKeystorePassword;
      return this;
    }

    public ConnectionProperties build() {
      return new ConnectionProperties(this);
    }
  }
}
