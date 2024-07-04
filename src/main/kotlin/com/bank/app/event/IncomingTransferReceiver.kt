package com.bank.app.event

import lombok.extern.slf4j.Slf4j
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Slf4j
class IncomingTransferReceiver {

  private val log = LoggerFactory.getLogger(IncomingTransferReceiver::class.java)
  @KafkaListener(
    groupId = "incoming-transfers-receivers",
    containerFactory = "",
    topics = ["money"]
  )
  @Transactional("transactionManager")
  fun listen(event: NewIncomingTransfer) {
    log.info("Received message: $event")
  }
}