package com.chat.demo.configuration

import com.chat.demo.inbound.request.MessageType
import com.chat.demo.inbound.request.UserSentMessage
import com.chat.demo.service.GroupChatService
import com.chat.demo.service.PrivateChatService
import com.chat.demo.service.SessionService
import com.chat.demo.service.getUserId
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.github.oshai.kotlinlogging.KotlinLogging
import java.util.*
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import org.springframework.web.socket.*
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor

private val log = KotlinLogging.logger {}

@Configuration
@EnableWebSocket
class WebSocketConfig(private val demoHandler: MyWebSocketHandler) : WebSocketConfigurer {

  override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
    registry
      .addHandler(demoHandler, "/ws")
      .setAllowedOrigins("*")
      .addInterceptors(HttpSessionHandshakeInterceptor())
    //      .withSockJS() //
  }
}

@Component
class MyWebSocketHandler(
  private val objectMapper: ObjectMapper,
  private val sessionStore: SessionService,
  private val privateChatService: PrivateChatService,
  private val groupChatService: GroupChatService,
) : WebSocketHandler {

  override fun afterConnectionEstablished(session: WebSocketSession) {
    sessionStore.addSession(session.id, session)
    log.info {
      "afterConnectionEstablished, session: $session, total sessions: ${sessionStore.getSessions().size}"
    }
  }

  override fun handleMessage(session: WebSocketSession, message: WebSocketMessage<*>) {
    log.info { "handleMessage, session: $session, message: $message" }
    val payload: String = message.payload as String
    val parsedMessage = objectMapper.readValue<UserSentMessage>(payload)
    val roomId = UUID.fromString(parsedMessage.roomId)
    val userId = session.getUserId()
    val text = parsedMessage.text

    log.info { "parsedMessage: $parsedMessage" }

    if (text.length > MAX_MESSAGE_LENGTH) {
      session.sendMessage(TextMessage("Message is too long"))
      return
    }

    when (parsedMessage.type) {
      MessageType.GROUP_MESSAGE ->
        privateChatService.sendMessage(roomId = roomId, userId = userId, text = text)
      MessageType.PRIVATE_MESSAGE ->
        groupChatService.sendMessage(roomId = roomId, userId = userId, text = text)
    }
  }

  override fun handleTransportError(session: WebSocketSession, exception: Throwable) {
    log.error(exception) { "handleTransportError, session: $session" }
  }

  override fun afterConnectionClosed(session: WebSocketSession, closeStatus: CloseStatus) {
    sessionStore.removeSession(session.id)

    log.info { "afterConnectionClosed, session: $session, closeStatus: $closeStatus" }
  }

  override fun supportsPartialMessages(): Boolean {
    return false
  }

  companion object {
    private const val MAX_MESSAGE_LENGTH = 1000
  }
}
