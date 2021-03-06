package com.aabdelhady.stomp.messenger.feature.conversation.repository

import com.aabdelhady.stomp.messenger.IntegrationTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

internal class ConversationRepositoryIntegrationTest : IntegrationTest() {
    @Autowired private lateinit var conversationRepository: ConversationRepository

    @Test
    fun findByParticipantId_shouldReturnConversationWhereUserWithIdIsParticipant() {
        // given
        val userA = createUser()
        val userB = createUser()
        val userC = createUser()

        val conversationA = createConversation(userA)
        val conversationAB = createConversation(userA, userB)
        val conversationBC = createConversation(userB, userC)
        val conversationABC = createConversation(userA, userB, userC)

        // when
        val resultA = conversationRepository.findByParticipantId(userA.id)
        val resultB = conversationRepository.findByParticipantId(userB.id)
        val resultC = conversationRepository.findByParticipantId(userC.id)

        // then
        assertEquals(3, resultA.size)
        assertTrue(resultA.containsAll(listOf(conversationA, conversationAB, conversationABC)))

        assertEquals(3, resultB.size)
        assertTrue(resultB.containsAll(listOf(conversationAB, conversationBC, conversationABC)))

        assertEquals(2, resultC.size)
        assertTrue(resultC.containsAll(listOf(conversationBC, conversationABC)))
    }

    @Test
    fun findByExactParticipantsIds_shouldReturnConversationWithExactParticipants() {
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

    @Test
    fun isParticipant_shouldReturnTrue_whenUserIsParticipant() {
        // given
        val userA = createUser()
        val userB = createUser()
        val userC = createUser()

        val conversation = createConversation(userA, userB)

        // when
        val resultA = conversationRepository.isParticipant(conversation.id, userA.id)
        val resultB = conversationRepository.isParticipant(conversation.id, userB.id)
        val resultC = conversationRepository.isParticipant(conversation.id, userC.id)

        // then
        assertTrue(resultA)
        assertTrue(resultB)
        assertFalse(resultC)
    }
}
