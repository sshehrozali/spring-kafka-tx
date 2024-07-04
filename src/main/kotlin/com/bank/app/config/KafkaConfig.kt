package com.bank.app.config

import com.bank.app.event.NewIncomingTransfer
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.TopicBuilder
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
@EnableTransactionManagement
class KafkaConfig(
  @Value("\${kafka.bootstrap-servers}") private val bootstrapServers: String,
) {

  @Bean("transferConfirmedEventProducerFactory")
  fun transferConfirmedEventProducerFactory(): ProducerFactory<String, NewIncomingTransfer> {
    val configProps: MutableMap<String, Any> = HashMap()
    configProps[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapServers
    configProps[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
    configProps[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
    return DefaultKafkaProducerFactory<String, NewIncomingTransfer>(configProps)
  }

  @Bean("transferConfirmedKafkaTemplate")
  fun transferConfirmedKafkaTemplate(@Qualifier("transferConfirmedEventProducerFactory") newIncomingTransferProducerFactory: ProducerFactory<String, NewIncomingTransfer>):
      KafkaTemplate<String, NewIncomingTransfer> {
    return KafkaTemplate(newIncomingTransferProducerFactory)
  }

  @Bean
  fun moneyTopic(): NewTopic {
    return TopicBuilder.name("money")
      .partitions(1)
      .replicas(1)
      .build()
  }
}