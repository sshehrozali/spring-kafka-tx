package com.bank.app.config

import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.config.TopicBuilder
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory


@Configuration
class KafkaConfig(
  @Value("\${kafka.bootstrap-servers}") private val bootstrapServers: String,
) {

  @Bean
  fun producerFactory(): ProducerFactory<String, String> {
    val props: MutableMap<String, Any> = HashMap()
    props[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapServers
    props[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
    props[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
    return DefaultKafkaProducerFactory<String, String>(props)
  }

  @Bean
  fun consumerFactory(): ConsumerFactory<String, String> {
    val props: MutableMap<String, Any> = HashMap()
    props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = "localhost:9092"
    props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
    props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
    return DefaultKafkaConsumerFactory(props)
  }

  @Bean("kafkaTemplate")
  fun kafkaTemplate(@Qualifier("producerFactory") producerFactory: ProducerFactory<String, String>):
      KafkaTemplate<String, String> {
    return KafkaTemplate(producerFactory)
  }

  @Bean
  fun concurrentKafkaListenerContainerFactory(consumerFactory: ConsumerFactory<String, String>): ConcurrentKafkaListenerContainerFactory<String, String> {
    val factory = ConcurrentKafkaListenerContainerFactory<String, String>()
    factory.consumerFactory = consumerFactory
    return factory
  }

  @Bean
  fun moneyTopic(): NewTopic {
    return TopicBuilder.name("payments")
      .partitions(1)
      .replicas(1)
      .build()
  }
}