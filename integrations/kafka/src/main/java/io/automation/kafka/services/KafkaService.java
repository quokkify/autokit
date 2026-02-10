package io.automation.kafka.services;

import java.util.Objects;

import io.automation.kafka.clients.ConnectionProperties;
import io.automation.kafka.configs.KafkaConfiguration;
import io.automation.kafka.steps.KafkaConsumerSteps;
import io.automation.kafka.steps.KafkaProducerSteps;
import io.automation.util.FileUtils;
import org.apache.kafka.common.security.auth.SecurityProtocol;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class KafkaService {

  private static final Logger LOG = LogManager.getLogger(KafkaService.class);
  private final KafkaConfiguration kafkaConfig;
  private KafkaProducerSteps<String, String, StringSerializer, StringSerializer> kafkaProducerSteps;
  private KafkaConsumerSteps<String, String, StringDeserializer, StringDeserializer> kafkaConsumerSteps;
  private ConnectionProperties connectionProperties;

  public KafkaService(KafkaConfiguration kafkaConfig) {
    this.kafkaConfig = kafkaConfig;
    setKafkaConnectionProperties();
  }

  public KafkaProducerSteps<String, String, StringSerializer, StringSerializer> getKafkaProducerSteps() {
    if (Objects.isNull(kafkaProducerSteps)) {
      try {
        kafkaProducerSteps = new KafkaProducerSteps<>(connectionProperties, StringSerializer.class,
            StringSerializer.class);
      } catch (Throwable error) {
        LOG.error("Kafka producer does not started: {}", error.getMessage());
        throw error;
      }
    }
    return kafkaProducerSteps;
  }

  public KafkaConsumerSteps<String, String, StringDeserializer, StringDeserializer> getKafkaConsumerSteps() {
    if (Objects.isNull(kafkaConsumerSteps)) {
      try {
        kafkaConsumerSteps = new KafkaConsumerSteps<>(connectionProperties, StringDeserializer.class,
            StringDeserializer.class);
      } catch (Throwable error) {
        LOG.error("Kafka consumer does not started: {}", error.getMessage());
        throw error;
      }
    }
    return kafkaConsumerSteps;
  }

  private void setKafkaConnectionProperties() {
    if (!SecurityProtocol.SSL.name().equals(kafkaConfig.securityProtocol())) {
      connectionProperties = ConnectionProperties.builder().bootstrapServers(kafkaConfig.kafkaServerAddress()).build();
    } else {
      connectionProperties = getSslKafkaConnectionProperties();
    }
  }

  private ConnectionProperties getSslKafkaConnectionProperties() {
    return ConnectionProperties.builder().bootstrapServers(kafkaConfig.kafkaServerAddress())
        .securityProtocol(kafkaConfig.securityProtocol())
        .sslKeystoreLocation(FileUtils.getResourcePath(kafkaConfig.sslKeystoreLocation()))
        .sslKeystorePassword(kafkaConfig.sslKeystorePassword())
        .sslTruststoreLocation(FileUtils.getResourcePath(kafkaConfig.sslTruststoreLocation()))
        .sslTruststorePassword(kafkaConfig.sslTruststorePassword())
        .build();
  }

  public void closeKafka() {
    if (Objects.nonNull(kafkaProducerSteps)) {
      kafkaProducerSteps.closeClientProducer();
    }
    if (Objects.nonNull(kafkaConsumerSteps)) {
      kafkaConsumerSteps.closeClientConsumer();
    }
  }
}
