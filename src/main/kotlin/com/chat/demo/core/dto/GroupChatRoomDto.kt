package com.chat.demo.core.dto

import com.chat.demo.model.GroupChatRoom
import java.time.LocalDateTime

data class GroupChatRoomDto(val id: String, val title: String, val lastMessageAt: LocalDateTime) {

  companion object {
    fun fromEntity(entity: GroupChatRoom): GroupChatRoomDto {
      return GroupChatRoomDto(
        id = entity.id.toString(),
        title = entity.title,
        lastMessageAt = entity.lastMessageAt,
      )
    }
  }
}
