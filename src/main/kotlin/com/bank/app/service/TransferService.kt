package com.bank.app.service

import com.bank.app.event.TransferConfirmed
import com.bank.app.repository.TransferRepository
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class TransferService(private val transferRepository: TransferRepository,
    @Qualifier("transferConfirmedKafkaTemplate") private val transferConfirmedKafkaTemplate: KafkaTemplate<String, TransferConfirmed>) {

  fun confirmTransfers(transferIds: List<String>): Int {
    // TODO
    return 1
  }
}