package com.chat.demo.outbound.jpa

import com.chat.demo.model.PrivateChatMessage
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface PrivateChatMessageJpaRepository : JpaRepository<PrivateChatMessage, UUID> {

  fun findByRoomId(roomId: UUID): List<PrivateChatMessage>
}
