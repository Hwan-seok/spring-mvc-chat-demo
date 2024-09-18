package com.chat.demo.service

import com.chat.demo.core.dto.GroupChatRoomDto
import com.chat.demo.core.dto.MessageDto
import com.chat.demo.core.dto.UserDto
import com.chat.demo.core.exception.ChatRoomNotFoundException
import com.chat.demo.core.exception.TooManyMembersException
import com.chat.demo.outbound.redis.DistributedLocker
import com.chat.demo.repository.GroupChatRoomRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import java.util.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

private val log = KotlinLogging.logger {}

@Service
@Transactional(readOnly = true)
class GroupChatServiceImpl(
  private val groupChatRoomRepository: GroupChatRoomRepository,
  private val distributedLocker: DistributedLocker,
  private val sessionService: SessionService,
) : GroupChatService {

  override fun findAllGroups(): List<GroupChatRoomDto> {
    return groupChatRoomRepository.findAllGroupChatRooms().map {
      GroupChatRoomDto(it.id.toString(), it.title, it.lastMessageAt)
    }
  }

  override fun findChatRoomMembers(roomId: UUID): List<UserDto> {
    return groupChatRoomRepository.findChatRoomMembers(roomId).map(UserDto::fromEntity)
  }

  @Transactional
  override fun createGroupChatRoom(userId: UUID, title: String): GroupChatRoomDto {
    return groupChatRoomRepository
      .createGroupChatRoom(userId, title)
      .run(GroupChatRoomDto::fromEntity)
  }

  @Transactional
  override fun enterGroupChatRoom(roomId: UUID, userId: UUID): GroupChatRoomDto {
    val room =
      groupChatRoomRepository.findGroupChatRoomById(roomId) ?: throw ChatRoomNotFoundException()
    val lockKey = KEY_PREFIX + roomId
    distributedLocker.lockAndExecute(lockKey) {
      val memberCount = groupChatRoomRepository.countGroupChatRoomMembers(roomId)
      log.info { "Member count: $memberCount" }
      if (memberCount >= MAX_MEMBERS) throw TooManyMembersException()

      groupChatRoomRepository.enterGroupChatRoom(roomId, userId)
    }
    return GroupChatRoomDto.fromEntity(room)
  }

  @Transactional
  override fun leaveGroupChatRoom(roomId: UUID, userId: UUID) {
    groupChatRoomRepository.leaveGroupChatRoom(roomId, userId)
  }

  @Transactional
  override fun sendMessage(roomId: UUID, userId: UUID, text: String) {
    val members = groupChatRoomRepository.findChatRoomMembers(roomId).map { it.id }
    val chatMessage = groupChatRoomRepository.saveMessage(roomId, userId, text)
    sessionService.sendToUsers(members, MessageDto.fromGroupChatMessage(chatMessage))
  }

  companion object {
    private const val MAX_MEMBERS = 1000
    private const val KEY_PREFIX = "group-chat-room-"
  }
}
