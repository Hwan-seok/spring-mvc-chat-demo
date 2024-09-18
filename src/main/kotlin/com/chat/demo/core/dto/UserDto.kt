package com.chat.demo.core.dto

import com.chat.demo.model.User

data class UserDto(val id: String) {

  companion object {
    fun fromEntity(entity: User): UserDto {
      return UserDto(entity.id.toString())
    }
  }
}
