package com.chat.demo.inbound.controller

import com.chat.demo.core.dto.PrivateChatRoomDto
import com.chat.demo.inbound.request.FindOrCreatePrivateChatRequestBody
import com.chat.demo.service.PrivateChatService
import java.util.*
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/private")
class PrivateChatRestController(private val privateChatService: PrivateChatService) {

  @GetMapping
  fun findMyRooms(@RequestHeader("X-User-Id") userId: String): List<PrivateChatRoomDto> {
    return privateChatService.findMyRooms(UUID.fromString(userId))
  }

  @PostMapping
  fun findOrCreateChatRoomWith(
    @RequestHeader("X-User-Id") userId: String,
    @RequestBody body: FindOrCreatePrivateChatRequestBody,
  ): PrivateChatRoomDto {
    return privateChatService.findOrCreateChatRoomWith(
      myId = UUID.fromString(userId),
      opponentId = UUID.fromString(body.opponentId),
    )
  }
}
