package com.aabdelhady.stomp.messenger.api

import com.aabdelhady.stomp.messenger.feature.conversation.service.ConversationService
import com.aabdelhady.stomp.messenger.feature.message.core.model.MessageRequest
import com.aabdelhady.stomp.messenger.feature.message.core.model.MessageResponse
import com.aabdelhady.stomp.messenger.feature.message.core.service.MessageService
import com.aabdelhady.stomp.messenger.feature.message.websocket.service.MessageWebSocketService
import com.aabdelhady.stomp.messenger.feature.user.model.UserResponse
import org.springframework.messaging.simp.user.SimpUserRegistry
import org.springframework.web.bind.annotation.*
import java.time.Instant
import javax.validation.Valid

@RestController
@RequestMapping("test")
class TestController(val messageWebSocketService: MessageWebSocketService, val userRegistry: SimpUserRegistry) {
    @GetMapping
    fun sendWebSocketMessage() {
        val user = UserResponse(2L, "asdsa", "aaf", null)
        val tmp = MessageResponse(1000L, "some text " + System.currentTimeMillis(), user, 51L, Instant.now())
        messageWebSocketService.pushNewMessage(1L, tmp)
    }

    @GetMapping("connected")
    fun allConnected() = userRegistry.users.map { it.name }
}
