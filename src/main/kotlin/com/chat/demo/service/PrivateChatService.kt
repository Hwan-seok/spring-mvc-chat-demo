package com.chat.demo.service

import com.chat.demo.core.dto.MessageDto
import com.chat.demo.core.dto.PrivateChatRoomDto
import java.util.*

interface PrivateChatService {

  fun findMyRooms(myId: UUID): List<PrivateChatRoomDto>

  fun getAllMessagesByRoomId(userId: UUID, roomId: UUID): List<MessageDto>

  fun findOrCreateChatRoomWith(myId: UUID, opponentId: UUID): PrivateChatRoomDto

  fun sendMessage(roomId: UUID, userId: UUID, text: String)
}
