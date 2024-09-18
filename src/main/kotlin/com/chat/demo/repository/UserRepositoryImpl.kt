package com.chat.demo.repository

import com.chat.demo.model.User
import com.chat.demo.outbound.jpa.UserJpaRepository
import java.util.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class UserRepositoryImpl(private val userJpaRepository: UserJpaRepository) : UserRepository {
  override fun findById(id: UUID): User? = userJpaRepository.findByIdOrNull(id)
}
