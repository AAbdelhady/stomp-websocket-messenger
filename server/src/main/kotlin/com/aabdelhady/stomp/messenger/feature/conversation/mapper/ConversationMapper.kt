package com.aabdelhady.stomp.messenger.feature.conversation.mapper

import com.aabdelhady.stomp.messenger.feature.conversation.model.Conversation
import com.aabdelhady.stomp.messenger.feature.conversation.model.ConversationResponse
import com.aabdelhady.stomp.messenger.feature.message.core.mapper.MessageMapper
import com.aabdelhady.stomp.messenger.feature.message.core.model.Message
import com.aabdelhady.stomp.messenger.feature.message.core.repository.MessageRepository
import com.aabdelhady.stomp.messenger.feature.user.mapper.UserMapper
import org.springframework.stereotype.Component

@Component
class ConversationMapper(private val messageRepository: MessageRepository, private val userMapper: UserMapper, private val messageMapper: MessageMapper) {
    fun mapList(conversations: List<Conversation>): List<ConversationResponse> {
        val lastMessages = messageRepository.findConversationsLastMessages(conversations.map { it.id })
        return conversations.map { mapOne(it, lastMessages[it.id]) }
    }

    private fun mapOne(conversation: Conversation, lastMessage: Message?): ConversationResponse {
        val participantsResponse = userMapper.mapList(conversation.participants)
        val lastMessageResponse = if (lastMessage != null) messageMapper.mapOne(lastMessage) else null
        return ConversationResponse(conversation.id, participantsResponse, lastMessageResponse)
    }
}
