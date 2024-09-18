package com.chat.demo.configuration

import com.chat.demo.core.dto.MessageDto
import com.chat.demo.service.SessionService
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.util.*
import org.redisson.Redisson
import org.redisson.api.RTopic
import org.redisson.api.RedissonClient
import org.redisson.codec.JsonJacksonCodec
import org.redisson.config.Config
import org.redisson.spring.data.connection.RedissonConnectionFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
data class RelayMessage(val targets: List<UUID>, val message: MessageDto)

@Configuration
class RedisConfiguration {

  @Bean
  fun objectMapper(): ObjectMapper {
    val objectMapper = ObjectMapper().registerKotlinModule()
    objectMapper.registerModule(JavaTimeModule())
    return objectMapper
  }

  @Configuration
  class RedisConfig {

    @Bean(destroyMethod = "shutdown")
    fun redissonClient(objectMapper: ObjectMapper): RedissonClient {
      val config = Config()

      config
        .setCodec(JsonJacksonCodec(objectMapper))
        .useSingleServer()
        .setAddress("redis://localhost:6379")
        .setDnsMonitoringInterval(-1)
        .setSubscriptionConnectionMinimumIdleSize(3)
        .setSubscriptionConnectionPoolSize(10)
      return Redisson.create(config)
    }

    @Bean
    fun redisConnectionFactory(redissonClient: RedissonClient): RedisConnectionFactory {
      return RedissonConnectionFactory(redissonClient)
    }
  }

  @Bean
  fun topic(
    redissonClient: RedissonClient,
    mapper: ObjectMapper,
    sessionService: SessionService,
  ): RTopic {
    val topic: RTopic = redissonClient.getTopic(RELAY_TOPIC)
    topic.addListener(RelayMessage::class.java) { channel, msg ->
      sessionService.handleRelayedMessage(msg.targets, msg.message)
    }

    return topic
  }

  companion object {
    const val RELAY_TOPIC = "message"
  }
}
