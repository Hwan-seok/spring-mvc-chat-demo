package com.chat.demo.outbound.redis

import io.github.oshai.kotlinlogging.KotlinLogging
import java.util.concurrent.TimeUnit
import org.redisson.api.RedissonClient
import org.springframework.stereotype.Repository

private val log = KotlinLogging.logger {}

@Repository
class RedissonLocker(private val redissonClient: RedissonClient) : DistributedLocker {
  override fun <R> lockAndExecute(key: String, f: () -> R): R {
    val lock = redissonClient.getLock("$LOCK_PREFIX$key")

    lock.lock(LOCK_TIMEOUT_BY_SECONDS, TimeUnit.SECONDS)
    log.info { "Lock acquired for key: $key" }
    try {
      return f()
    } finally {
      lock.unlock()
      log.info { "Lock released for key: $key" }
    }
  }

  companion object {
    const val LOCK_PREFIX = "lock:"
    const val LOCK_TIMEOUT_BY_SECONDS = 3L
  }
}
