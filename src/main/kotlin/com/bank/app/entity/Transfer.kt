package com.bank.app.entity

import com.bank.app.model.TransferStatus
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import lombok.Getter
import lombok.ToString
import java.io.Serializable
import java.sql.Timestamp
import java.time.Instant
import java.util.*

@Entity
@Table(name = "transfers")
@ToString
@Getter
class Transfer(
    @Id
    var id: UUID = UUID.randomUUID(),

    @Column(name = "transfer_id")
    var transferId: UUID,

    @Column(name = "transfer_status")
    var status: TransferStatus,

    @Column(name = "initiated_at")
    var initiatedAt: Instant = Instant.now()
) : Serializable