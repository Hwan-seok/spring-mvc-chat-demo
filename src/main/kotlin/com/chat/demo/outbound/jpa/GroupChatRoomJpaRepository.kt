package com.chat.demo.outbound.jpa

import com.chat.demo.model.GroupChatRoom
import java.util.*
import org.springframework.data.jpa.repository.JpaRepository

interface GroupChatRoomJpaRepository : JpaRepository<GroupChatRoom, UUID>
