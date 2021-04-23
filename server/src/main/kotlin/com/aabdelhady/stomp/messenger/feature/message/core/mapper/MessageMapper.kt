package com.aabdelhady.stomp.messenger.feature.message.core.mapper

import com.aabdelhady.stomp.messenger.feature.message.core.model.Message
import com.aabdelhady.stomp.messenger.feature.message.core.model.MessageResponse
import com.aabdelhady.stomp.messenger.feature.user.mapper.UserMapper
import org.springframework.stereotype.Component

@Component
class MessageMapper(private val userMapper: UserMapper) {
    fun mapList(messages: List<Message>) = messages.map { mapOne(it) }

    fun mapOne(message: Message): MessageResponse {
        val senderResponse = userMapper.mapOne(message.sender)
        return MessageResponse(message.id, message.text, senderResponse, message.conversation.id, message.sent!!)
    }
}
