package com.chat.demo.outbound.redis

interface DistributedLocker {

  fun <R> lockAndExecute(key: String, f: () -> R): R
}
