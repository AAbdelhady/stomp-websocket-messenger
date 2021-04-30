package com.aabdelhady.stomp.messenger.system.websocket

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

private const val TOPIC = "/topic"
private const val QUEUE = "/queue"

object Destinations {
    const val NOTIFICATION = "$TOPIC/notification"
}

@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig : WebSocketMessageBrokerConfigurer {
    @Value("\${frontend.base-url}") private val frontendBaseUrl: String = ""

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint("ws").setAllowedOrigins("*").setAllowedOriginPatterns("${frontendBaseUrl}*")
    }

    override fun configureMessageBroker(config: MessageBrokerRegistry) {
        config.enableSimpleBroker(TOPIC, QUEUE)
        config.setApplicationDestinationPrefixes("/app")
    }
}
