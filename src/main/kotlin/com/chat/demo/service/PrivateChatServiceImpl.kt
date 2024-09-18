package com.chat.demo.service

import com.chat.demo.core.dto.MessageDto
import com.chat.demo.core.dto.PrivateChatRoomDto
import com.chat.demo.core.exception.ChatRoomNotFoundException
import com.chat.demo.core.exception.UserNotFoundException
import com.chat.demo.repository.PrivateChatRoomRepository
import com.chat.demo.repository.UserRepository
import java.util.*
import org.springframework.stereotype.Service

@Service
class PrivateChatServiceImpl(
  private val privateChatRoomRepository: PrivateChatRoomRepository,
  private val userRepository: UserRepository,
  private val sessionService: SessionService,
) : PrivateChatService {
  override fun findMyRooms(myId: UUID): List<PrivateChatRoomDto> {
    val chatRooms = privateChatRoomRepository.findMyRooms(myId)
    return chatRooms.map { chatRoom ->
      val opponentId = if (chatRoom.userId1 == myId) chatRoom.userId2 else chatRoom.userId1
      val opponent = userRepository.findById(opponentId) ?: throw UserNotFoundException()
      PrivateChatRoomDto.of(chatRoom, opponent)
    }
  }

  override fun getAllMessagesByRoomId(userId: UUID, roomId: UUID): List<MessageDto> {
    val messages = privateChatRoomRepository.findAllMessagesByRoomId(roomId)
    return messages.map(MessageDto::fromPrivateChatMessage)
  }

  override fun findOrCreateChatRoomWith(myId: UUID, opponentId: UUID): PrivateChatRoomDto {
    val chatRoom = privateChatRoomRepository.findOrCreatePrivateChatRoom(myId, opponentId)
    val opponent = userRepository.findById(opponentId) ?: throw UserNotFoundException()

    return PrivateChatRoomDto.of(chatRoom, opponent)
  }

  override fun sendMessage(roomId: UUID, userId: UUID, text: String) {
    val room = privateChatRoomRepository.findByRoomId(roomId) ?: throw ChatRoomNotFoundException()
    val userIds = mutableSetOf(room.userId1, room.userId2)

    val chatMessage = privateChatRoomRepository.saveMessage(roomId, userId, text)
    sessionService.sendToUsers(userIds, MessageDto.fromPrivateChatMessage(chatMessage))
  }
}
