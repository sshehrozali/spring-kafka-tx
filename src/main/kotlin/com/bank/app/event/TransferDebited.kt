package com.bank.app.event

import java.time.Instant

data class TransferDebited(
  val transferId: String,
  val senderAccountNumber: String,
  val receiverAccountNumber: String,
  val amount: Int,
  val debitTimestamp: Instant
)
