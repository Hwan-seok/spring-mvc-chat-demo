package com.chat.demo.inbound.request

import jakarta.validation.constraints.NotBlank

data class CreateGroupChatRequestBody(@field:NotBlank val title: String)
