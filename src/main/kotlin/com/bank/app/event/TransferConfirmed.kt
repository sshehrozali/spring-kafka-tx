package com.bank.app.event

import java.sql.Timestamp

data class TransferConfirmed(
    val transferId: String,
    val timestamp: Timestamp
)