package com.bank.app

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.kafka.core.KafkaOperations
import org.springframework.kafka.core.KafkaOperations.OperationsCallback
import org.springframework.kafka.core.KafkaTemplate


@SpringBootApplication
class CoreApplication

fun main(args: Array<String>) {
  runApplication<CoreApplication>(*args)
}

@Bean
fun sendKafkaMessage(template: KafkaTemplate<String, String>): ApplicationRunner {
  return ApplicationRunner { _: ApplicationArguments? ->
    template.executeInTransaction(
      OperationsCallback { t: KafkaOperations<String, String> ->
        t.send(
          "payments",
          "Customer sent $1000.00"
        )
      })
  }
}
