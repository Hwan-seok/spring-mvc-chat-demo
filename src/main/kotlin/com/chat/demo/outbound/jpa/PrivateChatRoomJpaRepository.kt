package com.chat.demo.outbound.jpa

import com.chat.demo.model.PrivateChatRoom
import java.util.*
import org.springframework.data.jpa.repository.JpaRepository

interface PrivateChatRoomJpaRepository : JpaRepository<PrivateChatRoom, UUID> {

  fun findByUserId1AndUserId2(userId1: UUID, userId2: UUID): PrivateChatRoom?

  fun findByUserId1OrUserId2(userId1: UUID, userId2: UUID): List<PrivateChatRoom>
}
