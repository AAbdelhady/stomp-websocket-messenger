package com.aabdelhady.stomp.messenger.feature.conversation.service

import com.aabdelhady.stomp.messenger.feature.conversation.mapper.ConversationMapper
import com.aabdelhady.stomp.messenger.feature.conversation.model.Conversation
import com.aabdelhady.stomp.messenger.feature.conversation.model.ConversationRequest
import com.aabdelhady.stomp.messenger.feature.conversation.repository.ConversationRepository
import com.aabdelhady.stomp.messenger.feature.user.repository.UserRepository
import com.aabdelhady.stomp.messenger.system.auth.setAuthorizedUser
import com.aabdelhady.stomp.messenger.system.exception.ForbiddenException
import com.aabdelhady.stomp.messenger.system.exception.NotFoundException
import com.aabdelhady.stomp.messenger.system.exception.UnauthorizedException
import com.aabdelhady.stomp.messenger.test.clearAuthorizedUser
import com.aabdelhady.stomp.messenger.test.createDummyConversation
import com.aabdelhady.stomp.messenger.test.createDummyUser
import com.aabdelhady.stomp.messenger.test.randomId
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.*
import org.mockito.stubbing.Answer
import java.util.*

internal class ConversationServiceTest {
    private val conversationRepository = mock(ConversationRepository::class.java)
    private val conversationMapper = mock(ConversationMapper::class.java)
    private val userRepository = mock(UserRepository::class.java)

    private val conversationService = ConversationService(conversationRepository, conversationMapper, userRepository)

    @Test
    fun findAuthorizedUserConversations_shouldThrowUnauthorizedException_whenAuthorizedUserNotSet() {
        // given
        clearAuthorizedUser()

        // when
        assertThrows(UnauthorizedException::class.java) { conversationService.findAuthorizedUserConversations() }
    }

    @Test
    fun findAuthorizedUserConversations_shouldInvokeRepositoryAndMapper_whenAuthorizedUserSet() {
        // given
        val user = createDummyUser()

        val conversation = createDummyConversation(listOf(user))

        val conversations = listOf(conversation)
        `when`(conversationRepository.findByParticipantId(user.id)).thenReturn(conversations)

        setAuthorizedUser(user)

        // when
        conversationService.findAuthorizedUserConversations()

        // then
        verify(conversationRepository).findByParticipantId(user.id)

        verify(conversationMapper).mapList(conversations)
    }

    @Test
    fun findById_shouldThrowNotFoundException_whenNoConversationExistsWithId() {
        // given
        val conversationId = randomId()

        `when`(conversationRepository.findById(conversationId)).thenReturn(Optional.empty())

        // when
        assertThrows(NotFoundException::class.java) { conversationService.findById(conversationId) }
    }

    @Test
    fun findOrCreateByParticipants_shouldThrowUnauthorizedException_whenAuthorizedUserNotSet() {
        // given
        clearAuthorizedUser()

        val request = ConversationRequest(listOf(randomId()))

        // when
        assertThrows(UnauthorizedException::class.java) { conversationService.findOrCreateByParticipants(request) }
    }

    @Test
    fun findOrCreateByParticipants_shouldReturnExistingConversation_whenConversationExistsWithParticipants() {
        // given
        val userA = createDummyUser()
        val userB = createDummyUser()

        val existingConversation = createDummyConversation(listOf(userA, userB))

        val request = ConversationRequest(listOf(userB.id))

        `when`(conversationRepository.findByExactParticipantsIds(setOf(userA.id, userB.id))).thenReturn(Optional.of(existingConversation))

        setAuthorizedUser(userA)

        // when
        val result = conversationService.findOrCreateByParticipants(request)

        // then
        assertEquals(existingConversation, result)

        verify(conversationRepository).findByExactParticipantsIds(setOf(userA.id, userB.id))

        verify(userRepository, never()).findAllById(anyCollection())

        verify(conversationRepository, never()).save(any(Conversation::class.java))
    }

    @Test
    fun findOrCreateByParticipants_shouldCreateNewConversation_whenNoConversationExistsWithParticipants() {
        // given
        val userA = createDummyUser()
        val userB = createDummyUser()

        val request = ConversationRequest(listOf(userB.id))

        `when`(conversationRepository.findByExactParticipantsIds(setOf(userA.id, userB.id))).thenReturn(Optional.empty())

        `when`(userRepository.findAllById(setOf(userA.id, userB.id))).thenReturn(listOf(userA, userB))

        `when`(conversationRepository.save(any(Conversation::class.java))).then(conversationSaveAnswer())

        setAuthorizedUser(userA)

        // when
        val result = conversationService.findOrCreateByParticipants(request)

        // then
        assertNotNull(result.id)
        assertEquals(listOf(userA, userB), result.participants)

        verify(conversationRepository).findByExactParticipantsIds(setOf(userA.id, userB.id))

        verify(userRepository).findAllById(setOf(userA.id, userB.id))

        verify(conversationRepository).save(any(Conversation::class.java))
    }

    @Test
    fun assertParticipant_shouldThrowForbiddenException_whenUserIsNotParticipant() {
        // given
        val user = createDummyUser()
        val conversation = createDummyConversation()

        `when`(conversationRepository.isParticipant(conversation.id, user.id)).thenReturn(false)

        // when
        assertThrows(ForbiddenException::class.java) { conversationService.assertParticipant(conversation.id, user.id) }
    }

    @Test
    fun assertParticipant_shouldDoNothing_whenUserIsNotParticipant() {
        // given
        val user = createDummyUser()
        val conversation = createDummyConversation()

        `when`(conversationRepository.isParticipant(conversation.id, user.id)).thenReturn(true)

        // when
        assertDoesNotThrow() { conversationService.assertParticipant(conversation.id, user.id) }
    }

    private fun conversationSaveAnswer() = Answer { it.getArgument(0, Conversation::class.java).apply { id = randomId() } }


}
