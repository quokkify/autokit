package io.automation.websockets.steps;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.automation.constant.PollingInterval;
import io.automation.constant.Timeout;
import io.automation.util.JsonConverter;
import io.automation.util.Waiter;
import io.qameta.allure.Step;
import org.hamcrest.Matchers;

public abstract class MessagesSteps {

  abstract List<String> getMessages();

  /**
   * Gets a message with waiting until it appears with a specified timeout.
   *
   * @param condition The condition to be met for the message.
   * @param classOfT  The class type of the message.
   * @return The message that meets the specified condition within the timeout duration.
   */
  @Step("Get message with wait until appear")
  public <T> T getMessageWithWaitingUntilAppear(Predicate<T> condition, Class<T> classOfT) {
    return getMessageWithWaitingUntilAppear(condition, classOfT, Timeout.SECONDS_60.seconds());
  }

  /**
   * Gets a message with waiting until it appears with a specified timeout.
   *
   * @param condition          The condition to be met for the message.
   * @param classOfT           The class type of the message.
   * @param waitTimeoutSeconds The timeout duration in seconds to wait for the message.
   * @return The message that meets the specified condition within the timeout duration.
   */
  @Step("Get message with wait until appear with '{waitTimeoutSeconds}' seconds timeout")
  public <T> T getMessageWithWaitingUntilAppear(Predicate<T> condition, Class<T> classOfT, int waitTimeoutSeconds) {
    return Waiter.awaitCondition(() -> getMessagesWithCondition(condition, classOfT).findFirst().orElse(null),
        Matchers.notNullValue(),
        "No message in Centrifugo with condition '%s'".formatted(condition),
        waitTimeoutSeconds,
        PollingInterval.MILLIS_1000.getMillis());
  }

  /**
   * Gets a messages with waiting until it appears with a specified timeout.
   *
   * @param condition The condition to be met for the message.
   * @param classOfT  The class type of the message.
   * @return The message that meets the specified condition within the timeout duration.
   */
  @Step("Get messages with wait until appear")
  public <T> List<T> getMessagesWithWaitingUntilAppear(Predicate<T> condition, Class<T> classOfT) {
    return getMessagesWithWaitingUntilAppear(condition, classOfT, Timeout.SECONDS_60.seconds());
  }

  /**
   * Gets a messages with waiting until it appears with a specified timeout.
   *
   * @param condition          The condition to be met for the message.
   * @param classOfT           The class type of the message.
   * @param waitTimeoutSeconds The timeout duration in seconds to wait for the message.
   * @return The messages that meets the specified condition within the timeout duration.
   */
  @Step("Get messages with wait until appear with '{waitTimeoutSeconds}' seconds timeout")
  public <T> List<T> getMessagesWithWaitingUntilAppear(Predicate<T> condition, Class<T> classOfT,
                                                       int waitTimeoutSeconds) {
    return Waiter.awaitCondition(() -> getMessagesWithCondition(condition, classOfT).collect(Collectors.toList()),
        Matchers.not(Matchers.empty()),
        "No messages in Centrifugo with condition '%s'".formatted(condition),
        waitTimeoutSeconds,
        PollingInterval.MILLIS_1000.getMillis());
  }

  private <T> Stream<T> getMessagesWithCondition(Predicate<T> condition, Class<T> classOfT) {
    return getMessages().stream()
        .map(message -> JsonConverter.fromString(message, classOfT))
        .filter(condition);
  }
}
