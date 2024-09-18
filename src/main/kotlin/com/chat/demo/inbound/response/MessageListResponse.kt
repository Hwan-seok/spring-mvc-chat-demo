package com.chat.demo.inbound.response

import com.chat.demo.core.dto.MessageDto

data class MessageListResponse(val messages: List<MessageDto>)
