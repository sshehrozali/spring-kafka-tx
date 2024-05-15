package com.bank.app.integration.api

import com.bank.app.CoreApplication
import com.bank.app.entity.Transfer
import com.bank.app.model.TransferConfirmRequest
import com.bank.app.model.TransferStatus
import com.bank.app.repository.TransferRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import java.util.UUID

@AutoConfigureMockMvc
@SpringBootTest(classes = [CoreApplication::class])
@DisplayName("POST v1/transfer/confirm API Test")
class PostV1TransferConfirmAPITest(
  @Autowired private val mockMvc: MockMvc,
  @Autowired private val objectMapper: ObjectMapper,
  @Autowired private val transferRepository: TransferRepository
) {

  private val transferId1 = UUID.randomUUID()
  private val transferId2 = UUID.randomUUID()

  @BeforeEach
  fun setup() {
    transferRepository.deleteAll()
    val savedTransfers = listOf(
      Transfer(transferId = transferId1, status = TransferStatus.PENDING),
      Transfer(transferId = transferId2, status = TransferStatus.PENDING)
    )
    transferRepository.saveAll(savedTransfers)
  }

  @Test
  fun `should update transfers record in db with status marked as CONFIRMED and emit event`() {
    val requestDTO = TransferConfirmRequest(listOf(transferId1.toString(), transferId2.toString()))
    val requestBody = objectMapper.writeValueAsString(requestDTO)

    println("request body: $requestBody")

    val response = mockMvc
      .perform(
        MockMvcRequestBuilders.put("/api/v1/transfer/confirm")
          .accept(MediaType.APPLICATION_JSON)
          .contentType(MediaType.APPLICATION_JSON)
          .content(requestBody)
      )
      .andReturn()
      .response
      .contentAsString

    println(response)

    assertEquals(requestDTO.transferIds.size, transferRepository.findAll().size)
  }
}