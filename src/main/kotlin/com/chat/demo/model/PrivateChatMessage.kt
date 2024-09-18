package com.chat.demo.model

import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "private_chat_messages")
class PrivateChatMessage(
  @Column(nullable = false) val roomId: UUID,
  @JoinColumn(name = "sender_id", nullable = false)
  @ManyToOne(fetch = FetchType.LAZY)
  val sender: User,
  @Column(nullable = false) val text: String,
) : BaseEntity()
