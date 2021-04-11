package com.aabdelhady.stomp.messenger.feature.websocket.service

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.messaging.simp.user.SimpUserRegistry
import org.springframework.stereotype.Service
import java.util.*

const val QUEUE_PREFIX = "/queue"
const val TOPIC_PREFIX = "/topic"

fun slashJoin(vararg segments: String): String  = segments.asList().joinToString("/")

@Service
class WebSocketPushService(val userRegistry: SimpUserRegistry, val messagingTemplate: SimpMessagingTemplate, val objectMapper: ObjectMapper) {
    fun userHasEstablishedConnection(userId: Long) = userRegistry.users.any { it.name == userId.toString() }

    fun send(destination: String, payload: Any): String {
        val endpoint: String = slashJoin(TOPIC_PREFIX, destination)
        return sendInternal(payload) { json -> messagingTemplate.convertAndSend(endpoint, json) }
    }

    fun sendToUser(destination: String, receiverId: Long, payload: Any): String {
        val endpoint: String = slashJoin(QUEUE_PREFIX, destination)
        return sendInternal(payload) { json -> messagingTemplate.convertAndSendToUser(receiverId.toString(), endpoint, json) }
    }

    private fun sendInternal(payload: Any, convertAndSendFunction: (String) -> (Unit)): String {
        val messageUuid = UUID.randomUUID().toString()
        val webSocketMessage = WebSocketMessage(messageUuid, payload)
        objectMapper.writeValueAsString(webSocketMessage).apply(convertAndSendFunction)
        return messageUuid
    }
}

@JsonInclude(value = JsonInclude.Include.NON_NULL)
data class WebSocketMessage(
    @JsonProperty("message_uuid") val uuid: String,
    @JsonProperty("payload") val payload: Any
)
