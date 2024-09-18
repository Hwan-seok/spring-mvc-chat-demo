package com.chat.demo.inbound.controller

import com.chat.demo.core.dto.GroupChatRoomDto
import com.chat.demo.core.dto.UserDto
import com.chat.demo.inbound.request.CreateGroupChatRequestBody
import com.chat.demo.service.GroupChatService
import jakarta.validation.Valid
import java.util.*
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/groups")
class GroupChatRestController(private val groupChatService: GroupChatService) {

  @GetMapping
  fun findAllGroups(): List<GroupChatRoomDto> {
    return groupChatService.findAllGroups()
  }

  @GetMapping("/{roomId}/members")
  fun findChatRoomMembers(@PathVariable roomId: String): List<UserDto> {
    return groupChatService.findChatRoomMembers(UUID.fromString(roomId))
  }

  @PostMapping
  fun createGroupChatRoom(
    @RequestHeader("X-User-Id") userId: String,
    @Valid @RequestBody body: CreateGroupChatRequestBody,
  ): GroupChatRoomDto {
    return groupChatService.createGroupChatRoom(UUID.fromString(userId), body.title)
  }

  @PostMapping("/{roomId}/members")
  fun enterGroupChatRoom(
    @PathVariable roomId: String,
    @RequestHeader("X-User-Id") userId: String,
  ): GroupChatRoomDto {
    return groupChatService.enterGroupChatRoom(UUID.fromString(roomId), UUID.fromString(userId))
  }

  @DeleteMapping("/{roomId}/members")
  fun leaveGroupChatRoom(@PathVariable roomId: String, @RequestHeader("X-User-Id") userId: String) {
    groupChatService.leaveGroupChatRoom(UUID.fromString(roomId), UUID.fromString(userId))
  }
}
