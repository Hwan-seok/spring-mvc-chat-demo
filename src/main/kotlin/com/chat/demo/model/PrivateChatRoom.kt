package com.chat.demo.model

import com.chat.demo.core.sortUUid
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "private_chat_rooms")
class PrivateChatRoom(
  @Column(nullable = false) val userId1: UUID,
  @Column(nullable = false) val userId2: UUID,
  @Column(nullable = false) val lastMessageAt: LocalDateTime = LocalDateTime.now(),
) : BaseEntity() {
  init {
    require(userId1 < userId2) { "User IDs must be ordered" }
  }

  companion object {
    fun fromUserIds(userId1: UUID, userId2: UUID): PrivateChatRoom {
      val (u1, u2) = sortUUid(userId1, userId2)
      return PrivateChatRoom(userId1 = u1, userId2 = u2)
    }
  }
}
