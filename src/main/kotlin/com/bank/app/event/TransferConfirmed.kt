package com.bank.app.event

import java.time.Instant

data class TransferConfirmed(
    val transferId: String,
    val timestamp: Instant
)