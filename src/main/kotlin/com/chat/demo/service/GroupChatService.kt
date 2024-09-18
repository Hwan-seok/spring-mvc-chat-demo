package com.chat.demo.service

import com.chat.demo.core.dto.GroupChatRoomDto
import com.chat.demo.core.dto.UserDto
import java.util.*

interface GroupChatService {

  fun findAllGroups(): List<GroupChatRoomDto>

  fun findChatRoomMembers(roomId: UUID): List<UserDto>

  fun createGroupChatRoom(userId: UUID, title: String): GroupChatRoomDto

  fun enterGroupChatRoom(roomId: UUID, userId: UUID): GroupChatRoomDto

  fun leaveGroupChatRoom(roomId: UUID, userId: UUID)

  fun sendMessage(roomId: UUID, userId: UUID, text: String)
}
