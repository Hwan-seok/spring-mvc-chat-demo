package com.chat.demo.core

import java.util.*

fun sortUUid(uuid1: UUID, uuid2: UUID): Pair<UUID, UUID> {
  return if (uuid1 < uuid2) {
    Pair(uuid1, uuid2)
  } else {
    Pair(uuid2, uuid1)
  }
}
