package com.chat.demo.repository

import com.chat.demo.core.exception.UserNotFoundException
import com.chat.demo.model.GroupChatMember
import com.chat.demo.model.GroupChatMessage
import com.chat.demo.model.GroupChatRoom
import com.chat.demo.model.User
import com.chat.demo.outbound.jpa.GroupChatMemberJpaRepository
import com.chat.demo.outbound.jpa.GroupChatMessageJpaRepository
import com.chat.demo.outbound.jpa.GroupChatRoomJpaRepository
import com.chat.demo.outbound.jpa.UserJpaRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import java.util.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

private val log = KotlinLogging.logger {}

@Repository
class GroupChatRoomRepositoryImpl(
  private val userJpaRepository: UserJpaRepository,
  private val groupChatRoomJpaRepository: GroupChatRoomJpaRepository,
  private val groupChatMemberJpaRepository: GroupChatMemberJpaRepository,
  private val groupChatMessageRepository: GroupChatMessageJpaRepository,
) : GroupChatRoomRepository {
  override fun findAllGroupChatRooms(): List<GroupChatRoom> {
    return groupChatRoomJpaRepository.findAll()
  }

  override fun findGroupChatRoomById(roomId: UUID): GroupChatRoom? {
    return groupChatRoomJpaRepository.findByIdOrNull(roomId)
  }

  override fun findChatRoomMembers(roomId: UUID): List<User> {
    return groupChatMemberJpaRepository.findByRoomId(roomId).map(GroupChatMember::user)
  }

  override fun createGroupChatRoom(userId: UUID, title: String): GroupChatRoom {
    val room = groupChatRoomJpaRepository.save(GroupChatRoom(title))
    val user = userJpaRepository.findByIdOrNull(userId) ?: throw UserNotFoundException()
    groupChatMemberJpaRepository.save(GroupChatMember(user, room.id))

    return room
  }

  override fun enterGroupChatRoom(roomId: UUID, userId: UUID) {
    val user = userJpaRepository.findByIdOrNull(userId) ?: throw UserNotFoundException()

    val member = groupChatMemberJpaRepository.findByUserIdAndRoomId(userId, roomId)

    if (member == null) {
      groupChatMemberJpaRepository.save(GroupChatMember(user, roomId))
      log.info { "User $userId entered room $roomId" }
      // Possibly just throw an exception
    } else {
      log.info { "User $userId is already in room $roomId" }
    }
  }

  override fun leaveGroupChatRoom(roomId: UUID, userId: UUID) {
    groupChatMemberJpaRepository.deleteByUserIdAndRoomId(userId, roomId)
  }

  override fun saveMessage(roomId: UUID, userId: UUID, text: String): GroupChatMessage {
    val user = userJpaRepository.findByIdOrNull(userId) ?: throw UserNotFoundException()
    return groupChatMessageRepository.save(
      GroupChatMessage(text = text, sender = user, roomId = roomId)
    )
  }

  override fun countGroupChatRoomMembers(roomId: UUID): Long {
    return groupChatMemberJpaRepository.countByRoomId(roomId)
  }
}
