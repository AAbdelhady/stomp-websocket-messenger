package com.aabdelhady.stomp.messenger.feature.conversation.repository

import com.aabdelhady.stomp.messenger.IntegrationTest
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired

internal class ConversationRepositoryIntegrationTest : IntegrationTest() {
    @Autowired private lateinit var conversationRepository: ConversationRepository;

    @Test
    fun findByParticipantsIds_shouldReturnConversationWithExactParticipants() {
        // given
        val userA = createUser()
        val userB = createUser()
        val userC = createUser()

        val conversationA = createConversation(userA)
        val conversationAB = createConversation(userA, userB)
        val conversationBC = createConversation(userB, userC)
        val conversationABC = createConversation(userA, userB, userC)

        // when
        val resultA = conversationRepository.findByExactParticipantsIds(listOf(userA.id))
        val resultB = conversationRepository.findByExactParticipantsIds(listOf(userB.id))
        val resultC = conversationRepository.findByExactParticipantsIds(listOf(userA.id, userB.id))
        val resultD = conversationRepository.findByExactParticipantsIds(listOf(userA.id, userC.id))
        val resultE = conversationRepository.findByExactParticipantsIds(listOf(userB.id, userC.id))
        val resultF = conversationRepository.findByExactParticipantsIds(listOf(userA.id, userB.id, userC.id))

        // then
        assertTrue(resultA.isPresent)
        assertEquals(conversationA, resultA.get())

        assertFalse(resultB.isPresent)

        assertTrue(resultC.isPresent)
        assertEquals(conversationAB, resultC.get())

        assertFalse(resultD.isPresent)

        assertTrue(resultE.isPresent)
        assertEquals(conversationBC, resultE.get())

        assertTrue(resultF.isPresent)
        assertEquals(conversationABC, resultF.get())
    }
}
