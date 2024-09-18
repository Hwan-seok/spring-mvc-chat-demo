package com.chat.demo.outbound.jpa

import com.chat.demo.model.User
import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository

interface UserJpaRepository : JpaRepository<User, UUID> {}
