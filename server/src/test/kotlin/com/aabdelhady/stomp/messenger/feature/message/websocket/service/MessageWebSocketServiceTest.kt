package com.aabdelhady.stomp.messenger.feature.message.websocket.service

import com.aabdelhady.stomp.messenger.feature.websocket.service.WebSocketPushService
import com.aabdelhady.stomp.messenger.test.createDummyConversation
import com.aabdelhady.stomp.messenger.test.createDummyMessage
import com.aabdelhady.stomp.messenger.test.createDummyUser
import com.aabdelhady.stomp.messenger.test.response
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

internal class MessageWebSocketServiceTest {
    private val webSocketPushService = mock(WebSocketPushService::class.java)

    private val messageWebSocketService = MessageWebSocketService(webSocketPushService)

    @Test
    fun pushNewMessage_shouldPushToTheCorrectDestination() {
        // given
        val sender = createDummyUser()
        val receiver = createDummyUser()
        val conversation = createDummyConversation(listOf(sender, receiver))
        val message = createDummyMessage(conversation, sender)

        val messageResponse = message.response()

        // when
        messageWebSocketService.pushNewMessage(receiver.id, messageResponse)

        // then
        verify(webSocketPushService).pushToUserQueue("messenger/message", receiver.id, messageResponse)
    }
}
