package com.chat.demo.outbound.jpa

import com.chat.demo.model.GroupChatMember
import java.util.*
import org.springframework.data.jpa.repository.JpaRepository

interface GroupChatMemberJpaRepository : JpaRepository<GroupChatMember, UUID> {
  fun findByUserIdAndRoomId(userId: UUID, roomId: UUID): GroupChatMember?

  fun deleteByUserIdAndRoomId(userId: UUID, roomId: UUID)

  fun findByRoomId(roomId: UUID): List<GroupChatMember>

  fun countByRoomId(roomId: UUID): Long
}
