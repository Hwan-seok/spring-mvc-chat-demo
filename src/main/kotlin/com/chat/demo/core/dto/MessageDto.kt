package com.chat.demo.core.dto

import com.chat.demo.model.GroupChatMessage
import com.chat.demo.model.PrivateChatMessage
import java.time.LocalDateTime

data class MessageDto(
  val roomId: String,
  val sender: UserDto,
  val text: String,
  val sentAt: LocalDateTime,
) {

  companion object {
    fun fromPrivateChatMessage(privateChatMessage: PrivateChatMessage): MessageDto {
      return MessageDto(
        roomId = privateChatMessage.roomId.toString(),
        sender = UserDto.fromEntity(privateChatMessage.sender),
        text = privateChatMessage.text,
        sentAt = privateChatMessage.createdAt,
      )
    }

    fun fromGroupChatMessage(groupChatMessage: GroupChatMessage): MessageDto {
      return MessageDto(
        roomId = groupChatMessage.roomId.toString(),
        sender = UserDto.fromEntity(groupChatMessage.sender),
        text = groupChatMessage.text,
        sentAt = groupChatMessage.createdAt,
      )
    }
  }
}
