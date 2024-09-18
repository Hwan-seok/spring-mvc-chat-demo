package com.chat.demo.repository

import com.chat.demo.model.PrivateChatMessage
import com.chat.demo.model.PrivateChatRoom
import java.util.*

interface PrivateChatRoomRepository {
  fun findByRoomId(roomId: UUID): PrivateChatRoom?

  fun findMyRooms(userId: UUID): List<PrivateChatRoom>

  fun findOrCreatePrivateChatRoom(userId1: UUID, userId2: UUID): PrivateChatRoom

  fun findAllMessagesByRoomId(chatRoomId: UUID): List<PrivateChatMessage>

  fun saveMessage(chatRoomId: UUID, userId: UUID, message: String): PrivateChatMessage
}
