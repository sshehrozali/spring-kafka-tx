package com.bank.app.event

import java.time.Instant

data class NewIncomingTransfer(
    val transferId: String,
    val senderAccountNumber: String,
    val receiverAccountNumber: String,
    val amount: Int,
    val initiatedAtTimestamp: Instant
)