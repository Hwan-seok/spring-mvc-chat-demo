package com.chat.demo.outbound.jpa

import com.chat.demo.model.GroupChatMessage
import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository

interface GroupChatMessageJpaRepository : JpaRepository<GroupChatMessage, UUID>
