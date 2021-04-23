package com.aabdelhady.stomp.messenger.feature.conversation.mapper

import com.aabdelhady.stomp.messenger.feature.message.core.mapper.MessageMapper
import com.aabdelhady.stomp.messenger.feature.message.core.model.MessageResponse
import com.aabdelhady.stomp.messenger.feature.message.core.repository.MessageRepository
import com.aabdelhady.stomp.messenger.feature.user.mapper.UserMapper
import com.aabdelhady.stomp.messenger.feature.user.model.User
import com.aabdelhady.stomp.messenger.feature.user.model.UserResponse
import com.aabdelhady.stomp.messenger.test.createDummyConversation
import com.aabdelhady.stomp.messenger.test.createDummyMessage
import com.aabdelhady.stomp.messenger.test.createDummyUser
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.verify

internal class ConversationMapperTest {
    private val messageRepository = Mockito.mock(MessageRepository::class.java)
    private val userMapper = Mockito.mock(UserMapper::class.java)
    private val messageMapper = Mockito.mock(MessageMapper::class.java)

    private val conversationMapper = ConversationMapper(messageRepository, userMapper, messageMapper)

    @Test
    fun mapList_shouldIncludeLastMessage_whenConversationHasLastMessage() {
        // given
        val participantA = createDummyUser()
        val participantB = createDummyUser()
        val conversation = createDummyConversation(listOf(participantA, participantB))

        val participantsResponse = listOf(participantA.response(), participantB.response())
        Mockito.`when`(userMapper.mapList(conversation.participants)).thenReturn(participantsResponse)

        val lastMessage = createDummyMessage(conversation, participantA)
        Mockito.`when`(messageRepository.findConversationsLastMessages(listOf(conversation.id))).thenReturn(mapOf(conversation.id to lastMessage))

        val lastMessageResponse = MessageResponse(lastMessage.id, lastMessage.text, participantA.response(), conversation.id, lastMessage.sent!!)
        Mockito.`when`(messageMapper.mapOne(lastMessage)).thenReturn(lastMessageResponse)


        // when
        val response = conversationMapper.mapList(listOf(conversation))

        // then
        assertEquals(1, response.size)

        assertEquals(conversation.id, response[0].id)
        assertEquals(participantsResponse, response[0].participants)
        assertEquals(lastMessageResponse, response[0].lastMessage)

        verify(userMapper).mapList(listOf(participantA, participantB))

        verify(messageRepository).findConversationsLastMessages(listOf(conversation.id))

        verify(messageMapper).mapOne(lastMessage)
    }

    private fun User.response() = UserResponse(id, firstName, lastName, profilePictureUrl)
}
