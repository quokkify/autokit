package io.automation.websockets.configs;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
    "system:properties",
    "system:env",
    "classpath:local_resources/websockets.properties",
    "classpath:websockets.properties"
})
public interface WebSocketsConfiguration extends Config {

  @Key("WEBSOCKETS_HOST")
  @DefaultValue("localhost")
  String webSocketsHost();

  @Key("WEBSOCKETS_PORT")
  @DefaultValue("8001")
  int webSocketsPort();

  @Key("WEBSOCKETS_SERVER_SECRET")
  @DefaultValue("6eca2493-8f27-4940-aa9e-df7a87b053c9")
  String webSocketsServerSecret();

  @Key("WEBSOCKETS_CHANNEL_NAME")
  @DefaultValue("test_channel")
  String webSocketsChannelName();

  @Key("WEBSOCKETS_USER_ID")
  @DefaultValue("1234")
  long webSocketsUserId();

  @Key("WEBSOCKETS_MESSAGE")
  @DefaultValue("success")
  String webSocketsMessage();

  @Key("CENTRIFUGO_HOST")
  @DefaultValue("localhost")
  String centrifugoHost();

  @Key("CENTRIFUGO_PORT")
  @DefaultValue("8005")
  int centrifugoPort();

  @Key("CENTRIFUGO_CHANNEL_NAME")
  @DefaultValue("ws:test_channel")
  String centrifugoChannelName();

  @Key("CENTRIFUGO_API_KEY")
  @DefaultValue("api_key")
  String centrifugoApiKey();

  @Key("CENTRIFUGO_MESSAGE")
  @DefaultValue("success")
  String centrifugoMessage();

  @Key("CENTRIFUGO_USER_ID")
  @DefaultValue("1234")
  long centrifugoUserId();

  @Key("CENTRIFUGO_KEYS_PATH")
  @DefaultValue("keys.json")
  String centrifugoKeysPath();
}
