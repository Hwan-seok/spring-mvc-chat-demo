package com.chat.demo.repository

import com.chat.demo.core.exception.UserNotFoundException
import com.chat.demo.core.sortUUid
import com.chat.demo.model.PrivateChatMessage
import com.chat.demo.model.PrivateChatRoom
import com.chat.demo.outbound.jpa.PrivateChatMessageJpaRepository
import com.chat.demo.outbound.jpa.PrivateChatRoomJpaRepository
import com.chat.demo.outbound.jpa.UserJpaRepository
import com.chat.demo.outbound.redis.DistributedLocker
import java.util.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class PrivateChatRoomRepositoryImpl(
  private val distributedLocker: DistributedLocker,
  private val userJpaRepository: UserJpaRepository,
  private val privateChatRoomJpaRepository: PrivateChatRoomJpaRepository,
  private val privateChatMessageJpaRepository: PrivateChatMessageJpaRepository,
) : PrivateChatRoomRepository {
  override fun findByRoomId(roomId: UUID): PrivateChatRoom? {
    return privateChatRoomJpaRepository.findByIdOrNull(roomId)
  }

  override fun findMyRooms(userId: UUID): List<PrivateChatRoom> {
    return privateChatRoomJpaRepository.findByUserId1OrUserId2(userId, userId)
  }

  override fun findOrCreatePrivateChatRoom(userId1: UUID, userId2: UUID): PrivateChatRoom {
    val (u1, u2) = sortUUid(userId1, userId2)
    val key = "private-chat-room-${u1}-${u2}"
    return distributedLocker.lockAndExecute(key) {
      privateChatRoomJpaRepository.findByUserId1AndUserId2(u1, u2)
        ?: privateChatRoomJpaRepository.save(PrivateChatRoom(u1, u2))
    }
  }

  override fun findAllMessagesByRoomId(chatRoomId: UUID): List<PrivateChatMessage> {
    return privateChatMessageJpaRepository.findByRoomId(chatRoomId)
  }

  override fun saveMessage(chatRoomId: UUID, userId: UUID, message: String): PrivateChatMessage {
    val user = userJpaRepository.findByIdOrNull(userId) ?: throw UserNotFoundException()
    return privateChatMessageJpaRepository.save(PrivateChatMessage(chatRoomId, user, message))
  }
}
