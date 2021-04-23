package com.aabdelhady.stomp.messenger.feature.user.repository

import com.aabdelhady.stomp.messenger.IntegrationTest
import com.aabdelhady.stomp.messenger.test.randomEmail
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.util.*

internal class UserRepositoryIntegrationTest : IntegrationTest() {
    @Autowired private lateinit var userRepository: UserRepository

    @Test
    fun findByEmail_shouldReturnOptionalOfUser_whenUserWithEmailExists() {
        // given
        val userA = createUser()
        val userB = createUser()

        // when
        val resultA = userRepository.findByEmail(userA.email)
        val resultB = userRepository.findByEmail(userB.email)
        val resultC = userRepository.findByEmail(randomEmail())

        // then
        assertTrue(resultA.isPresent)
        assertEquals(userA, resultA.get())

        assertTrue(resultB.isPresent)
        assertEquals(userB, resultB.get())

        assertFalse(resultC.isPresent)
    }

    @Test
    fun findByLoginId_shouldReturnOptionalOfUser_whenUserWithLoginIdExists() {
        // given
        val userA = createUser()
        val userB = createUser()

        // when
        val resultA = userRepository.findByLoginId(userA.loginId)
        val resultB = userRepository.findByLoginId(userB.loginId)
        val resultC = userRepository.findByLoginId(UUID.randomUUID().toString())

        // then
        assertTrue(resultA.isPresent)
        assertEquals(userA, resultA.get())

        assertTrue(resultB.isPresent)
        assertEquals(userB, resultB.get())

        assertFalse(resultC.isPresent)
    }
}
