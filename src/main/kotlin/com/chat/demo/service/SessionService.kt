package com.chat.demo.service

import com.chat.demo.configuration.RedisConfiguration
import com.chat.demo.configuration.RelayMessage
import com.chat.demo.core.dto.MessageDto
import com.fasterxml.jackson.databind.ObjectMapper
import io.github.oshai.kotlinlogging.KotlinLogging
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import org.redisson.api.RedissonClient
import org.springframework.stereotype.Service
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession

fun WebSocketSession.getUserId(): UUID {
  return UUID.fromString(this.handshakeHeaders["X-User-Id"]!![0])
}

private val log = KotlinLogging.logger {}

@Service
class SessionService(
  private val objectMapper: ObjectMapper,
  private val redissonClient: RedissonClient,
) {
  private val idToSession = ConcurrentHashMap<String, WebSocketSession>()
  private val userIdToSessionId = ConcurrentHashMap<UUID, String>()

  fun addSession(sessionId: String, session: WebSocketSession) {
    idToSession[sessionId] = session
    userIdToSessionId[session.getUserId()] = sessionId
  }

  fun removeSession(sessionId: String) {
    userIdToSessionId.remove(idToSession[sessionId]?.getUserId())
    idToSession.remove(sessionId)
  }

  fun getSession(sessionId: String): WebSocketSession? {
    return idToSession[sessionId]
  }

  fun getSessions(): MutableCollection<WebSocketSession> {
    return idToSession.values
  }

  fun sendToUsers(userIds: Collection<UUID>, message: MessageDto) {
    log.info { "Sending message to users: $userIds, message: $message" }
    val relayMessage = RelayMessage(userIds.toList(), message)
    val received = redissonClient.getTopic(RedisConfiguration.RELAY_TOPIC).publish(relayMessage)
    log.info { "Received by $received listeners" }
  }

  fun handleRelayedMessage(userIds: Collection<UUID>, message: MessageDto) {
    log.info { "Handling relayed message for users: $userIds, message: $message" }
    val messageJson = objectMapper.writeValueAsString(message)
    for (userId in userIds) {
      val sessionId = userIdToSessionId[userId]
      if (sessionId == null) {
        log.warn { "No session found for user $userId within this node" }
        continue
      }
      val session = idToSession[sessionId]
      session?.sendMessage(TextMessage(messageJson))
    }
  }
}
