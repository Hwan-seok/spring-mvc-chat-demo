package com.chat.demo.repository

import com.chat.demo.model.GroupChatMessage
import com.chat.demo.model.GroupChatRoom
import com.chat.demo.model.User
import java.util.*

interface GroupChatRoomRepository {
  // offset & limit could be used to implement pagination
  fun findAllGroupChatRooms(): List<GroupChatRoom>

  fun findGroupChatRoomById(roomId: UUID): GroupChatRoom?

  fun createGroupChatRoom(userId: UUID, title: String): GroupChatRoom

  fun enterGroupChatRoom(roomId: UUID, userId: UUID)

  fun leaveGroupChatRoom(roomId: UUID, userId: UUID)

  fun saveMessage(roomId: UUID, userId: UUID, text: String): GroupChatMessage

  fun countGroupChatRoomMembers(roomId: UUID): Long

  fun findChatRoomMembers(roomId: UUID): List<User>
}
