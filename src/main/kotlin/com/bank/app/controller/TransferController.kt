package com.bank.app.controller

import com.bank.app.exception.FailedToProcessTransferConfirmation
import com.bank.app.model.TransferConfirmRequest
import com.bank.app.model.TransferConfirmedSuccessResponse
import com.bank.app.service.TransferService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1")
class TransferController(private val transferService: TransferService) {

  @PutMapping("/transfer/confirm")
  fun confirmTransfers(@RequestBody transferConfirmRequest: TransferConfirmRequest): ResponseEntity<TransferConfirmedSuccessResponse> {
    return try {
      val totalNumber = transferService.confirmTransfers(transferConfirmRequest.transferIds)
      ResponseEntity.ok(TransferConfirmedSuccessResponse(totalNumber))
    } catch (e: FailedToProcessTransferConfirmation) {
      ResponseEntity.internalServerError().build()
    }
  }
}