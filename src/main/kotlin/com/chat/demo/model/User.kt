package com.chat.demo.model

import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity @Table(name = "users") class User : BaseEntity()
