package com.aabdelhady.stomp.messenger.feature.message.core.service

import com.aabdelhady.stomp.messenger.feature.conversation.service.ConversationService
import com.aabdelhady.stomp.messenger.feature.message.core.mapper.MessageMapper
import com.aabdelhady.stomp.messenger.feature.message.core.model.Message
import com.aabdelhady.stomp.messenger.feature.message.core.model.MessageRequest
import com.aabdelhady.stomp.messenger.feature.message.core.model.MessageResponse
import com.aabdelhady.stomp.messenger.feature.message.core.repository.MessageRepository
import com.aabdelhady.stomp.messenger.feature.message.websocket.service.MessageWebSocketService
import com.aabdelhady.stomp.messenger.feature.user.repository.UserRepository
import com.aabdelhady.stomp.messenger.system.auth.util.setAuthorizedUser
import com.aabdelhady.stomp.messenger.system.exception.NotFoundException
import com.aabdelhady.stomp.messenger.system.exception.UnauthorizedException
import com.aabdelhady.stomp.messenger.test.*
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.*
import org.mockito.stubbing.Answer
import java.time.Instant
import java.util.*

internal class MessageServiceTest {
    private val messageRepository = mock(MessageRepository::class.java)
    private val userRepository = mock(UserRepository::class.java)
    private val conversationService = mock(ConversationService::class.java)
    private val messageMapper = mock(MessageMapper::class.java)
    private val messageWebSocketService = mock(MessageWebSocketService::class.java)

    private val messageService = MessageService(messageRepository, userRepository, conversationService, messageMapper, messageWebSocketService)

    @Test
    fun findConversationMessages_shouldThrowUnauthorizedException_whenAuthorizedUserNotSet() {
        // given
        clearAuthorizedUser()

        // when
        assertThrows(UnauthorizedException::class.java) { messageService.findConversationMessages(randomId()) }
    }

    @Test
    fun findConversationMessages_shouldReturnMessageResponses_whenAuthorizedUserIsSet() {
        // given
        val authorizedUser = createDummyUser()
        val conversation = createDummyConversation(listOf(authorizedUser))

        val message = createDummyMessage(conversation, authorizedUser)

        val messages = listOf(message)
        `when`(messageRepository.findByConversationId(conversation.id)).thenReturn(messages)

        setAuthorizedUser(authorizedUser)

        // when
        messageService.findConversationMessages(conversation.id)

        // then
        verify(conversationService).assertParticipant(conversation.id, authorizedUser.id)
        verify(messageMapper).mapList(messages)
    }

    @Test
    fun sendMessage_shouldThrowUnauthorizedException_whenAuthorizedUserNotSet() {
        // given
        clearAuthorizedUser()

        val request = MessageRequest("message text")

        // when
        assertThrows(UnauthorizedException::class.java) { messageService.sendMessage(randomId(), request) }

        // then
        verify(messageRepository, never()).save(anyOf(Message::class.java))
        verify(messageWebSocketService, never()).pushNewMessage(anyLong(), anyOf(MessageResponse::class.java))
    }

    @Test
    fun sendMessage_shouldThrowNotFoundException_whenAuthorizedUserNotFound() {
        // given
        val authorizedUser = createDummyUser()
        val participantA = createDummyUser()
        val participantB = createDummyUser()

        val conversation = createDummyConversation(listOf(authorizedUser, participantA, participantB))

        `when`(conversationService.findById(conversation.id)).thenReturn(conversation)

        `when`(userRepository.findById(authorizedUser.id)).thenReturn(Optional.empty())

        setAuthorizedUser(authorizedUser)

        val request = MessageRequest("message text")

        // when
        assertThrows(NotFoundException::class.java) { messageService.sendMessage(randomId(), request) }

        // then
        verify(messageRepository, never()).save(anyOf(Message::class.java))
        verify(messageWebSocketService, never()).pushNewMessage(anyLong(), anyOf(MessageResponse::class.java))
    }

    @Test
    fun sendMessage_shouldSaveAndPushNewMessage_whenAuthorizedUserIsSet() {
        // given
        val authorizedUser = createDummyUser()
        val participantA = createDummyUser()
        val participantB = createDummyUser()

        val conversation = createDummyConversation(listOf(authorizedUser, participantA, participantB))

        `when`(conversationService.findById(conversation.id)).thenReturn(conversation)

        `when`(userRepository.findById(authorizedUser.id)).thenReturn(Optional.of(authorizedUser))

        `when`(messageRepository.save(anyOf(Message::class.java))).then(messageSaveAnswer())

        `when`(messageMapper.mapOne(anyOf(Message::class.java))).then(messageMapAnswer())

        setAuthorizedUser(authorizedUser)

        val request = MessageRequest("message text")

        // when
        messageService.sendMessage(conversation.id, request)

        // then
        val captor = ArgumentCaptor.forClass(Message::class.java)
        verify(messageRepository).save(captor.capture())
        val savedMessage = captor.value

        verify(messageMapper).mapOne(savedMessage)

        verify(messageWebSocketService).pushNewMessage(participantA.id, savedMessage.response())
        verify(messageWebSocketService).pushNewMessage(participantB.id, savedMessage.response())

    }

    private fun messageSaveAnswer() = Answer {
        it.getArgument(0, Message::class.java).apply {
            id = randomId()
            sent = Instant.now()
        }
    }

    private fun messageMapAnswer() = Answer { it.getArgument(0, Message::class.java).run { this.response() } }
}
