package com.chat.demo.repository

import com.chat.demo.model.User
import java.util.*

interface UserRepository {

  fun findById(id: UUID): User?
}
