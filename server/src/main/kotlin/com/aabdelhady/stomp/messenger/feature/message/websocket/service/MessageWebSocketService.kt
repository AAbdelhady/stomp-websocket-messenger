package com.aabdelhady.stomp.messenger.feature.message.websocket.service

import com.aabdelhady.stomp.messenger.feature.message.core.model.MessageResponse
import com.aabdelhady.stomp.messenger.feature.websocket.service.QueueWebSocketService
import com.aabdelhady.stomp.messenger.feature.websocket.service.WebSocketPushService
import com.aabdelhady.stomp.messenger.feature.websocket.service.slashJoin
import org.springframework.stereotype.Service

@Service
class MessageWebSocketService(private val webSocketPushService: WebSocketPushService) : QueueWebSocketService {
    override val destinationPrefix = "messenger"

    fun pushNewMessage(receiverId: Long, payload: MessageResponse) {
        val destination = slashJoin(destinationPrefix, "message")
        webSocketPushService.pushToUserQueue(destination, receiverId, payload)
    }

    /*fun pushTypingNotification(receiverId: Long, payload: ConversationTypingResponse?) {
        val destination: String = slashJoin(getDestinationPrefix(), "typing")
        webSocketPushService.sendToUser(destination, receiverId!!, payload)
    }*/
}
