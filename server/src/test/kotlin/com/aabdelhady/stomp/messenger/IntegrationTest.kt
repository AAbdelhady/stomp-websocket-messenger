package com.aabdelhady.stomp.messenger

import com.aabdelhady.stomp.messenger.feature.conversation.repository.ConversationRepository
import com.aabdelhady.stomp.messenger.feature.user.model.AuthProvider
import com.aabdelhady.stomp.messenger.feature.user.model.User
import com.aabdelhady.stomp.messenger.feature.user.repository.UserRepository
import com.aabdelhady.stomp.messenger.test.createDummyConversation
import com.aabdelhady.stomp.messenger.test.createDummyUser
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.transaction.annotation.Transactional
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@ExtendWith(SpringExtension::class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
@Testcontainers
abstract class IntegrationTest {
    @Container
    val container = PostgreSQLContainer<Nothing>("postgres").apply {
        start()
        System.setProperty("TEST_DB_URL", jdbcUrl)
        System.setProperty("TEST_DB_USERNAME", username)
        System.setProperty("TEST_DB_PASSWORD", password)
    }

    @Autowired private lateinit var userRepository: UserRepository
    @Autowired private lateinit var conversationRepository: ConversationRepository

    protected fun createUser(authProvider: AuthProvider = AuthProvider.GOOGLE) = createDummyUser(authProvider).let {
        delay()
        userRepository.saveAndFlush(it)
    }

    protected fun createConversation(vararg participants: User) = createDummyConversation(participants.asList()).let {
        delay()
        conversationRepository.saveAndFlush(it)
    }

    private fun delay() = Thread.sleep(10)
}
