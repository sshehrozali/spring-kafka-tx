package com.bank.app.controller

import com.bank.app.model.TransferConfirmRequest
import com.bank.app.model.TransferConfirmedSuccessResponse
import com.bank.app.service.TransferService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1")
class TransferController(private val transferService: TransferService) {

  @PostMapping("/transfer/confirm")
  fun confirmTransfers(@RequestBody transferConfirmRequest: TransferConfirmRequest): ResponseEntity<TransferConfirmedSuccessResponse> {
    val totalNumber = transferService.confirmTransfers(transferConfirmRequest.transferIds)
    return ResponseEntity.ok(TransferConfirmedSuccessResponse(totalNumber))
  }
}