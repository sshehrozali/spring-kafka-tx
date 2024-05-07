package com.bank.app.repository

import com.bank.app.entity.Transfer
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository
interface TransferRepository : JpaRepository<Transfer, UUID> {
  fun findByTransferId(transferId: UUID): Optional<Transfer>
}