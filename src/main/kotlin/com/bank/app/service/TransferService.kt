package com.bank.app.service

import com.bank.app.event.TransferConfirmed
import com.bank.app.exception.FailedToProcessTransferConfirmation
import com.bank.app.model.TransferStatus
import com.bank.app.repository.TransferRepository
import lombok.extern.slf4j.Slf4j
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.Exception
import java.time.Instant
import java.util.*
import java.util.logging.Logger
import kotlin.math.log

@Service
@Slf4j
class TransferService(
  private val transferRepository: TransferRepository,
  @Qualifier("transferConfirmedKafkaTemplate") private val transferConfirmedKafkaTemplate: KafkaTemplate<String, TransferConfirmed>
) {

  private val log = LoggerFactory.getLogger(TransferService::class.java)
  private final val transfersTopic = "TRANSFERS"

  @Transactional
  fun confirmTransfers(transferIds: List<String>): Int {
    transferIds
      .forEach { transferId ->
        transferRepository.findByTransferId(UUID.fromString(transferId))
          .ifPresent { transfer ->
            log.info("Performing update operation...")
            try {
              if (transfer.status == TransferStatus.CONFIRMED) {
                log.info("Skipping transferId: $transferId because it's already confirmed")
              } else {
                transfer.status = TransferStatus.CONFIRMED
                transferRepository.save(transfer)

                val event = TransferConfirmed(transferId, Instant.now())
                transferConfirmedKafkaTemplate.send(transfersTopic, transferId, event)
              }
            } catch (e: Exception) {
              log.error("Transfer confirmation operation failed at transferId: $transferId. Operation rolled back successfully.$e")
              throw FailedToProcessTransferConfirmation()
            }
          }
      }
    return transferIds.size
  }
}