package com.chat.demo.inbound.request

enum class MessageType {
  GROUP_MESSAGE,
  PRIVATE_MESSAGE,
}

sealed class UserSentMessage(val type: MessageType, val roomId: String, val text: String) {

  class GroupMessage(roomId: String, text: String) :
    UserSentMessage(MessageType.GROUP_MESSAGE, roomId, text)

  class PrivateMessage(roomId: String, text: String) :
    UserSentMessage(MessageType.PRIVATE_MESSAGE, roomId, text)
}
