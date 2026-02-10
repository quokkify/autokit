package io.automation.kafka.clients;

import java.util.Objects;

/**
 * General message for send to kafka. Uses with {@link ClientProducer}
 *
 * @param <K> Type of message key
 * @param <V> Type of message value
 */
public class KafkaMessage<K, V> {

  private String topicName;
  private int partition;
  private long offset;
  private K key;
  private V value;

  public KafkaMessage() {
  }

  public KafkaMessage(String topicName, int partition, long offset, K key, V value) {
    this.topicName = topicName;
    this.partition = partition;
    this.offset = offset;
    this.key = key;
    this.value = value;
  }

  public KafkaMessage(String topicName, K key, V value) {
    this.topicName = topicName;
    this.key = key;
    this.value = value;
  }

  public String getTopicName() {
    return topicName;
  }

  public int getPartition() {
    return partition;
  }

  public long getOffset() {
    return offset;
  }

  public K getKey() {
    return key;
  }

  public V getValue() {
    return value;
  }

  public void setTopicName(String topicName) {
    this.topicName = topicName;
  }

  public void setPartition(int partition) {
    this.partition = partition;
  }

  public void setOffset(long offset) {
    this.offset = offset;
  }

  public void setKey(K key) {
    this.key = key;
  }

  public void setValue(V value) {
    this.value = value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof KafkaMessage<?, ?> that)) {
      return false;
    }
    return partition == that.partition
        && offset == that.offset
        && Objects.equals(topicName, that.topicName)
        && Objects.equals(key, that.key)
        && Objects.equals(value, that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(topicName, partition, offset, key, value);
  }

  @Override
  public String toString() {
    return "KafkaMessage{"
        + "topicName='" + topicName + '\''
        + ", partition=" + partition
        + ", offset=" + offset
        + ", key=" + key
        + ", value=" + value
        + '}';
  }
}
