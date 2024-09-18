package com.chat.demo.model

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "group_chat_messages")
class GroupChatMessage(
  @Column(nullable = false) val roomId: UUID,
  @JoinColumn(name = "sender_id", nullable = false)
  @ManyToOne(fetch = FetchType.LAZY)
  val sender: User,
  @Column(nullable = false) val text: String,
) : BaseEntity()
