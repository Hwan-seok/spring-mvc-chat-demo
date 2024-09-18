package com.chat.demo.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "group_chat_rooms")
class GroupChatRoom(
  @Column(nullable = false) val title: String,
  @Column(nullable = false) val lastMessageAt: LocalDateTime = LocalDateTime.now(),
) : BaseEntity()
