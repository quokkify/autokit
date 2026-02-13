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
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Getter
@Log4j2
public class CentrifugoSubscriptionEventListener extends SubscriptionEventListener {

  private final List<WebSocketMessage> webSocketMessages = new ArrayList<>();
  private final Map<String, Boolean> subscribedChannels = new HashMap<>();

  /**
   * Called when a channel subscription is successful.
   *
   * @param sub   The Subscription object representing the subscribed channel.
   * @param event The SubscribedEvent object containing information about the subscription event.
   */
  @Override
  public void onSubscribed(Subscription sub, SubscribedEvent event) {
    subscribedChannels.put(sub.getChannel(), true);
    log.info("Centrifugo subscribed channel '%s', recovered '%s'".formatted(sub.getChannel(),
        event.getRecovered()));
  }

  /**
   * Called when a channel is in the process of subscribing.
   *
   * @param sub   The Subscription object representing the channel being subscribed to.
   * @param event The SubscribingEvent object containing information about the subscribing event.
   */
  @Override
  public void onSubscribing(Subscription sub, SubscribingEvent event) {
    log.debug("Centrifugo subscribing: %s".formatted(sub.getChannel()));
  }

  /**
   * Called when a channel is unsubscribed from.
   *
   * @param sub   The Subscription object representing the unsubscribed channel.
   * @param event The UnsubscribedEvent object containing information about the unsubscription event.
   */
  @Override
  public void onUnsubscribed(Subscription sub, UnsubscribedEvent event) {
    subscribedChannels.put(sub.getChannel(), false);
    log.debug("Centrifugo unsubscribed '%s' reason '%s' ".formatted(sub.getChannel(), event.getReason()));
  }

  /**
   * Called when an error occurs during a subscription channel.
   *
   * @param sub   The Subscription object representing the channel where the error occurred.
   * @param event The SubscriptionErrorEvent object containing information about the error event.
   */
  @Override
  public void onError(Subscription sub, SubscriptionErrorEvent event) {
    log.error("Centrifugo error subscription channel '%s' with error %s".formatted(sub.getChannel(),
        event.getError().toString()));
  }

  /**
   * Called when a publication is received on the subscribed channel. Processes the event data
   * and logs the message details along with the channel from where the message originated.
   *
   * @param sub   The Subscription object representing the subscribed channel.
   * @param event The PublicationEvent object containing information about the publication event.
   */
  @Override
  public void onPublication(Subscription sub, PublicationEvent event) {
    String data = new String(event.getData(), StandardCharsets.UTF_8);
    webSocketMessages.add(new WebSocketMessage().setTimestamp(LocalDateTimeGenerator.generateNow())
        .setMessage(data));
    log.info("Centrifugo message from channel '%s', data: %s".formatted(sub.getChannel(), data));
  }

  /**
   * Handles the event when a client joins a channel.
   *
   * @param sub   the Subscription object representing the channel subscription
   * @param event the JoinEvent object containing information about the join event
   */
  @Override
  public void onJoin(Subscription sub, JoinEvent event) {
    log.debug("Centrifugo client '%s' joined channel '%s'".formatted(event.getInfo().getClient(), sub.getChannel()));
  }

  /**
   * Handles the event when a client leaves a channel.
   *
   * @param sub   The subscription object representing the subscription of the client to the channel.
   * @param event The LeaveEvent containing information about the client leaving the channel.
   */
  @Override
  public void onLeave(Subscription sub, LeaveEvent event) {
    log.debug("Centrifugo client '%s' left channel '%s'".formatted(event.getInfo().getClient(), sub.getChannel()));
  }

  /**
   * Checks if the specified channel is subscribed.
   *
   * @param channelName The name of the channel to check subscription for.
   * @return true if the channel is subscribed, false otherwise.
   */
  public boolean isSubscribed(String channelName) {
    return subscribedChannels.containsKey(channelName) && subscribedChannels.get(channelName);
  }
}
