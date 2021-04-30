package com.aabdelhady.stomp.messenger.feature.message.core.service

import com.aabdelhady.stomp.messenger.feature.conversation.model.Conversation
import com.aabdelhady.stomp.messenger.feature.conversation.service.ConversationService
import com.aabdelhady.stomp.messenger.feature.message.core.mapper.MessageMapper
import com.aabdelhady.stomp.messenger.feature.message.core.model.Message
import com.aabdelhady.stomp.messenger.feature.message.core.model.MessageRequest
import com.aabdelhady.stomp.messenger.feature.message.core.model.MessageResponse
import com.aabdelhady.stomp.messenger.feature.message.core.repository.MessageRepository
import com.aabdelhady.stomp.messenger.feature.message.websocket.service.MessageWebSocketService
import com.aabdelhady.stomp.messenger.feature.user.model.User
import com.aabdelhady.stomp.messenger.feature.user.repository.UserRepository
import com.aabdelhady.stomp.messenger.system.auth.getAuthorizedUserIdOrThrowUnauthorized
import com.aabdelhady.stomp.messenger.system.exception.NotFoundException
import org.springframework.stereotype.Service

@Service
class MessageService(private val messageRepository: MessageRepository, private val userRepository: UserRepository,
                     private val conversationService: ConversationService, private val messageMapper: MessageMapper,
                     private val messageWebSocketService: MessageWebSocketService) {
    fun findConversationMessages(conversationId: Long): List<MessageResponse> {
        val authorizedUserId = getAuthorizedUserIdOrThrowUnauthorized()
        conversationService.assertParticipant(conversationId, authorizedUserId)
        return messageRepository.findByConversationId(conversationId).let { messageMapper.mapList(it) }
    }

    fun sendMessage(conversationId: Long, request: MessageRequest): MessageResponse {
        val authorizedUserId = getAuthorizedUserIdOrThrowUnauthorized()
        val conversation = conversationService.findById(conversationId)
        val message = saveNewMessage(conversation, authorizedUserId, request.text)
        val messageResponse = messageMapper.mapOne(message)
        pushMessageToRecipients(conversation, authorizedUserId, messageResponse)
        return messageResponse
    }

    private fun saveNewMessage(conversation: Conversation, senderId: Long, text: String): Message {
        val sender: User = userRepository.findById(senderId).orElseThrow { NotFoundException() }
        return messageRepository.save(Message(conversation, sender, text))
    }

    private fun pushMessageToRecipients(conversation: Conversation, senderId: Long, messageResponse: MessageResponse) {
        conversation.participants
            .filter { it.id != senderId }
            .forEach { messageWebSocketService.pushNewMessage(it.id, messageResponse) }
    }
}
