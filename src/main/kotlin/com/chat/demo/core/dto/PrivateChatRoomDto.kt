package com.chat.demo.core.dto

import com.chat.demo.model.PrivateChatRoom
import com.chat.demo.model.User
import java.time.LocalDateTime

data class PrivateChatRoomDto(val id: String, val user: UserDto, val lastMessageAt: LocalDateTime) {

  companion object {
    fun of(entity: PrivateChatRoom, with: User): PrivateChatRoomDto {
      return PrivateChatRoomDto(
        id = entity.id.toString(),
        user = UserDto.fromEntity(with),
        lastMessageAt = entity.lastMessageAt,
      )
    }
  }
}
