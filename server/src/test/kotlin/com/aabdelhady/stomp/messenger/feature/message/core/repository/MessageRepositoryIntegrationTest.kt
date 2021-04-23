package com.aabdelhady.stomp.messenger.feature.message.core.repository

import com.aabdelhady.stomp.messenger.IntegrationTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class MessageRepositoryIntegrationTest : IntegrationTest() {
    @Autowired private lateinit var messageRepository: MessageRepository

    @Test
    fun findByConversationId_shouldReturnCorrectMessageInConversation() {
        // given
        val userA = createUser()
        val userB = createUser()
        val userC = createUser()

        val conversationAB = createConversation(userA, userB)
        val conversationAC = createConversation(userA, userC)

        val messageA = createMessage(conversationAB, userA)
        val messageB = createMessage(conversationAB, userB)
        val messageC = createMessage(conversationAC, userC)

        // when
        val resultA = messageRepository.findByConversationId(conversationAB.id)
        val resultB = messageRepository.findByConversationId(conversationAC.id)

        // then
        assertEquals(2, resultA.size)
        assertTrue(resultA.containsAll(listOf(messageA, messageB)))

        assertEquals(1, resultB.size)
        assertTrue(resultB.contains(messageC))
    }

    @Test
    fun findConversationsLastMessages_shouldReturnCorrectLastMessages() {
        // given
        val userA = createUser()
        val userB = createUser()
        val userC = createUser()

        val conversationAB = createConversation(userA, userB)
        val conversationAC = createConversation(userA, userC)
        val conversationBC = createConversation(userB, userC)

        val messageA = createMessage(conversationAB, userA)
        val messageB = createMessage(conversationAB, userB)
        val messageC = createMessage(conversationAC, userC)
        val messageD = createMessage(conversationAC, userA)

        // when
        val result = messageRepository.findConversationsLastMessages(listOf(conversationAB.id, conversationAC.id, conversationBC.id))

        // then
        assertEquals(2, result.size)

        assertEquals(messageB, result[conversationAB.id])
        assertEquals(messageD, result[conversationAC.id])
    }
}
