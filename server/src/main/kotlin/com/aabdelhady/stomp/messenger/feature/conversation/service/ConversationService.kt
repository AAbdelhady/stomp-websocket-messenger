package com.aabdelhady.stomp.messenger.feature.conversation.service

import com.aabdelhady.stomp.messenger.feature.conversation.mapper.ConversationMapper
import com.aabdelhady.stomp.messenger.feature.conversation.model.Conversation
import com.aabdelhady.stomp.messenger.feature.conversation.model.ConversationRequest
import com.aabdelhady.stomp.messenger.feature.conversation.model.ConversationResponse
import com.aabdelhady.stomp.messenger.feature.conversation.repository.ConversationRepository
import com.aabdelhady.stomp.messenger.feature.user.repository.UserRepository
import com.aabdelhady.stomp.messenger.system.auth.util.getAuthorizedUserIdOrThrowUnauthorized
import com.aabdelhady.stomp.messenger.system.exception.NotFoundException
import org.springframework.stereotype.Service

@Service
class ConversationService(private val conversationRepository: ConversationRepository, private val conversationMapper: ConversationMapper,
                          private val userRepository: UserRepository) {

    fun findAuthorizedUserConversations(): List<ConversationResponse> {
        val authorizedUserId = getAuthorizedUserIdOrThrowUnauthorized()
        val conversations = conversationRepository.findByParticipantsId(authorizedUserId)
        return conversationMapper.mapList(conversations)
    }

    fun findById(conversationId: Long): Conversation = conversationRepository.findById(conversationId).orElseThrow { NotFoundException() }

    fun findOrCreateByParticipants(request: ConversationRequest): Conversation {
        val participantsIds = request.participantsIds
        return conversationRepository.findByExactParticipantsIds(participantsIds).orElseGet { createConversation(participantsIds) }
    }

    private fun createConversation(participantsIds: List<Long>): Conversation {
        val participants = userRepository.findAllById(participantsIds)
        return conversationRepository.save(Conversation(participants))
    }
}
