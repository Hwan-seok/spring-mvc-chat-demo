package com.chat.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories

@EnableJpaRepositories(basePackages = ["com.chat.demo.outbound.jpa"])
@EnableRedisRepositories(basePackages = ["com.chat.demo.outbound.redis"])
@EnableJpaAuditing
@SpringBootApplication
class ChatDemoApplication

fun main(args: Array<String>) {
  runApplication<ChatDemoApplication>(*args)
}
