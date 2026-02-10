package io.automation.kafka.converters;

import io.automation.kafka.clients.KafkaMessage;
import io.automation.kafka.steps.models.KafkaMessageValue;
import io.automation.util.JsonConverter;
import org.apache.commons.lang3.StringUtils;

/**
 * Used for kafka message converting.
 */
public final class MessageConverter {

  private MessageConverter() {
  }

  public static <M extends KafkaMessageValue> KafkaMessage<String, String> convertToJsonMessage(String topic,
                                                                                                 M messageValue) {
    return new KafkaMessage<>(topic, StringUtils.EMPTY, JsonConverter.toJson(messageValue));
  }
}
