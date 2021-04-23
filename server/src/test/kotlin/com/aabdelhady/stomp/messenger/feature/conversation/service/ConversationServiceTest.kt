package com.aabdelhady.stomp.messenger.feature.conversation.service

import com.aabdelhady.stomp.messenger.feature.conversation.mapper.ConversationMapper
import com.aabdelhady.stomp.messenger.feature.conversation.model.Conversation
import com.aabdelhady.stomp.messenger.feature.conversation.repository.ConversationRepository
import com.aabdelhady.stomp.messenger.feature.user.repository.UserRepository
import com.aabdelhady.stomp.messenger.system.exception.NotFoundException
import com.aabdelhady.stomp.messenger.test.randomId
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import java.util.*

internal class ConversationServiceTest {
    private val conversationRepository = mock(ConversationRepository::class.java)
    private val conversationMapper = mock(ConversationMapper::class.java)
    private val userRepository = mock(UserRepository::class.java)

    private val conversationService = ConversationService(conversationRepository, conversationMapper, userRepository)

    @Test
    fun findAuthorizedUserConversations() {
    }

    @Test
    fun findById_shouldThrowNotFoundException_whenNoConversationExistsWithId() {
        // given
        val conversationId = randomId()

        Mockito.`when`(conversationRepository.findById(conversationId)).thenReturn(Optional.empty<Conversation>())

        // when
        assertThrows(NotFoundException::class.java) { conversationService.findById(conversationId) }
    }
}
