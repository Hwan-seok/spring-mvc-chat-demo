package com.chat.demo.model

import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(
  name = "group_chat_members",
  uniqueConstraints = [UniqueConstraint(columnNames = ["user_id", "room_id"])],
)
class GroupChatMember(
  @JoinColumn(name = "user_id", nullable = false) @ManyToOne(fetch = FetchType.LAZY) val user: User,
  @Column(nullable = false) val roomId: UUID,
) : BaseEntity()
