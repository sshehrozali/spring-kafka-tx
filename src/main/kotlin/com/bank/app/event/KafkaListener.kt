package com.bank.app.event

import lombok.extern.slf4j.Slf4j
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Slf4j
class KafkaListener {

  private val log = LoggerFactory.getLogger(KafkaListener::class.java)
  @KafkaListener(
    groupId = "unique-group-id",
    containerFactory = "concurrentKafkaListenerContainerFactory",
    topics = ["payments"]
  )
  @Transactional("transactionManager")
  fun listen(event: String) {
    log.info("Received message: $event")
  }
}