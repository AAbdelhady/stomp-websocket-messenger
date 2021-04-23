package com.aabdelhady.stomp.messenger.feature.message.core.mapper

import com.aabdelhady.stomp.messenger.feature.user.mapper.UserMapper
import com.aabdelhady.stomp.messenger.test.createDummyConversation
import com.aabdelhady.stomp.messenger.test.createDummyMessage
import com.aabdelhady.stomp.messenger.test.createDummyUser
import com.aabdelhady.stomp.messenger.test.response
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

internal class MessageMapperTest {
    private val userMapper = mock(UserMapper::class.java)

    private val messageMapper = MessageMapper(userMapper)

    @Test
    fun mapList_shouldIncludeLastMessage_whenConversationHasLastMessage() {
        // given
        val userA = createDummyUser()
        val userB = createDummyUser()
        val conversation = createDummyConversation(listOf(userA, userB))

        val messageA = createDummyMessage(conversation, userA)
        val messageB = createDummyMessage(conversation, userB)

        `when`(userMapper.mapOne(userA)).thenReturn(userA.response())
        `when`(userMapper.mapOne(userB)).thenReturn(userB.response())

        // when
        val response = messageMapper.mapList(listOf(messageA, messageB))

        // then
        assertEquals(2, response.size)

        assertEquals(messageA.id, response[0].id)
        assertEquals(userA.response(), response[0].sender)
        assertEquals(messageA.sent, response[0].sent)
        assertEquals(conversation.id, response[0].conversationId)
        assertEquals(messageA.text, response[0].text)

        assertEquals(messageB.id, response[1].id)
        assertEquals(userB.response(), response[1].sender)
        assertEquals(messageB.sent, response[1].sent)
        assertEquals(conversation.id, response[1].conversationId)
        assertEquals(messageB.text, response[1].text)

        Mockito.verify(userMapper).mapOne(userA)
        Mockito.verify(userMapper).mapOne(userB)
    }
}
