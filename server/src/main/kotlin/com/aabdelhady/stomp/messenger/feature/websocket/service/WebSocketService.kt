package com.aabdelhady.stomp.messenger.feature.websocket.service

interface WebSocketService {
    val destinationPrefix: String
}

interface QueueWebSocketService : WebSocketService

interface TopicWebSocketService : WebSocketService {
    fun assertAuthorized(endpoint: String, subscriberId: Int)
}
