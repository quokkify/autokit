package io.automation.websockets.services.centrifugo;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.automation.generator.LocalDateTimeGenerator;
import io.automation.websockets.entities.WebSocketMessage;
import io.github.centrifugal.centrifuge.JoinEvent;
import io.github.centrifugal.centrifuge.LeaveEvent;
import io.github.centrifugal.centrifuge.PublicationEvent;
import io.github.centrifugal.centrifuge.SubscribedEvent;
import io.github.centrifugal.centrifuge.SubscribingEvent;
import io.github.centrifugal.centrifuge.Subscription;
import io.github.centrifugal.centrifuge.SubscriptionErrorEvent;
import io.github.centrifugal.centrifuge.SubscriptionEventListener;
import io.github.centrifugal.centrifuge.UnsubscribedEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CentrifugoSubscriptionEventListener extends SubscriptionEventListener {

  private static final Logger LOG = LogManager.getLogger(CentrifugoSubscriptionEventListener.class);

  private final List<WebSocketMessage> webSocketMessages = new ArrayList<>();
  private final Map<String, Boolean> subscribedChannels = new HashMap<>();

  public List<WebSocketMessage> getWebSocketMessages() {
    return webSocketMessages;
  }

  public Map<String, Boolean> getSubscribedChannels() {
    return subscribedChannels;
  }

  @Override
  public void onSubscribed(Subscription sub, SubscribedEvent event) {
    subscribedChannels.put(sub.getChannel(), true);
    LOG.info("Centrifugo subscribed channel '{}', recovered '{}'", sub.getChannel(), event.getRecovered());
  }

  @Override
  public void onSubscribing(Subscription sub, SubscribingEvent event) {
    LOG.debug("Centrifugo subscribing: {}", sub.getChannel());
  }

  @Override
  public void onUnsubscribed(Subscription sub, UnsubscribedEvent event) {
    subscribedChannels.put(sub.getChannel(), false);
    LOG.debug("Centrifugo unsubscribed '{}' reason '{}'", sub.getChannel(), event.getReason());
  }

  @Override
  public void onError(Subscription sub, SubscriptionErrorEvent event) {
    LOG.error("Centrifugo error subscription channel '{}' with error {}", sub.getChannel(),
        event.getError().toString());
  }

  @Override
  public void onPublication(Subscription sub, PublicationEvent event) {
    String data = new String(event.getData(), StandardCharsets.UTF_8);
    webSocketMessages.add(new WebSocketMessage().setTimestamp(LocalDateTimeGenerator.generateNow())
        .setMessage(data));
    LOG.info("Centrifugo message from channel '{}', data: {}", sub.getChannel(), data);
  }

  @Override
  public void onJoin(Subscription sub, JoinEvent event) {
    LOG.debug("Centrifugo client '{}' joined channel '{}'", event.getInfo().getClient(), sub.getChannel());
  }

  @Override
  public void onLeave(Subscription sub, LeaveEvent event) {
    LOG.debug("Centrifugo client '{}' left channel '{}'", event.getInfo().getClient(), sub.getChannel());
  }

  public boolean isSubscribed(String channelName) {
    return subscribedChannels.containsKey(channelName) && subscribedChannels.get(channelName);
  }
}
