package com.chat.demo.model

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseEntity {
  @Id @GeneratedValue(strategy = GenerationType.UUID) var id: UUID = UUID(0L, 0L)

  @CreatedDate
  @Column(nullable = false, updatable = false)
  var createdAt: LocalDateTime = LocalDateTime.now()

  @LastModifiedDate
  @Column(nullable = false, updatable = true)
  var updatedAt: LocalDateTime = LocalDateTime.now()
}
