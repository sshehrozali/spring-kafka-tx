package com.bank.app.config

import com.bank.app.event.TransferConfirmed
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.transaction.KafkaTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import org.springframework.transaction.support.AbstractPlatformTransactionManager

@Configuration
@EnableTransactionManagement
class KafkaConfig(@Value("\${kafka.bootstrap-servers}") private val bootstrapServers: String,
                  @Value("\${kafka.transaction-id}") private val kafkaTransactionId: String) {

  @Bean("transferConfirmedEventProducerFactory")
  fun transferConfirmedEventProducerFactory(): ProducerFactory<String, TransferConfirmed> {
    val configProps: MutableMap<String, Any> = HashMap()
    configProps[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapServers
    configProps[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
    configProps[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
    configProps[ProducerConfig.TRANSACTIONAL_ID_CONFIG] = kafkaTransactionId
    return DefaultKafkaProducerFactory<String, TransferConfirmed>(configProps)
  }

  @Bean("transferConfirmedKafkaTransactionManager")
  fun transferConfirmedKafkaTransactionManager(@Qualifier("transferConfirmedEventProducerFactory") transferConfirmedEventProducerFactory: ProducerFactory<String, TransferConfirmed>):
      KafkaTransactionManager<String, TransferConfirmed> {
    val kafkaTransactionManager = KafkaTransactionManager<String, TransferConfirmed>(transferConfirmedEventProducerFactory)
    kafkaTransactionManager.transactionSynchronization = AbstractPlatformTransactionManager.SYNCHRONIZATION_ON_ACTUAL_TRANSACTION
    return kafkaTransactionManager
  }

  @Bean("transferConfirmedKafkaTemplate")
  fun transferConfirmedKafkaTemplate(@Qualifier("transferConfirmedEventProducerFactory") transferConfirmedEventProducerFactory: ProducerFactory<String, TransferConfirmed>):
      KafkaTemplate<String, TransferConfirmed> {
    return KafkaTemplate(transferConfirmedEventProducerFactory)
  }
}