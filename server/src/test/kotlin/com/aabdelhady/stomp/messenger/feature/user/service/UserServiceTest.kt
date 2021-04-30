package com.aabdelhady.stomp.messenger.feature.user.service

import com.aabdelhady.stomp.messenger.feature.user.mapper.UserMapper
import com.aabdelhady.stomp.messenger.feature.user.repository.UserRepository
import com.aabdelhady.stomp.messenger.system.auth.setAuthorizedUser
import com.aabdelhady.stomp.messenger.system.exception.BadRequestException
import com.aabdelhady.stomp.messenger.system.exception.UnauthorizedException
import com.aabdelhady.stomp.messenger.test.clearAuthorizedUser
import com.aabdelhady.stomp.messenger.test.createDummyUser
import com.aabdelhady.stomp.messenger.test.randomEmail
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import java.util.*

internal class UserServiceTest {
    private val userRepository = mock(UserRepository::class.java)
    private val userMapper = mock(UserMapper::class.java)

    private val userService = UserService(userRepository, userMapper)

    @Test
    fun findAuthorizedUser_shouldThrowUnauthorizedException_whenAuthorizedUserNotSet() {
        // given
        clearAuthorizedUser()

        // when
        assertThrows(UnauthorizedException::class.java) { userService.findAuthorizedUser() }
    }

    @Test
    fun findAuthorizedUser_shouldThrowBadRequest_whenNoUserWithSetAuthorizedUserIdExists() {
        // given
        val user = createDummyUser()

        `when`(userRepository.findById(user.id)).thenReturn(Optional.empty())

        setAuthorizedUser(user)

        // when
        assertThrows(BadRequestException::class.java) { userService.findAuthorizedUser() }
    }

    @Test
    fun findAuthorizedUser_shouldReturnUser_whenUserWithSetAuthorizedUserIdExists() {
        // given
        val user = createDummyUser()

        `when`(userRepository.findById(user.id)).thenReturn(Optional.of(user))

        setAuthorizedUser(user)

        // when
        userService.findAuthorizedUser()

        // then
        verify(userRepository).findById(user.id)
        verify(userMapper).mapOne(user)
    }

    @Test
    fun findByEmail_shouldThrowBadRequest_whenNoUserWithProvidedEmailExists() {
        // given
        val email = randomEmail()

        `when`(userRepository.findByEmail(email)).thenReturn(Optional.empty())

        // when
        assertThrows(BadRequestException::class.java) { userService.findByEmail(email) }
    }

    @Test
    fun findByEmail_shouldReturnUser_whenUserWithProvidedEmailExists() {
        // given
        val user = createDummyUser()

        `when`(userRepository.findByEmail(user.email)).thenReturn(Optional.of(user))

        // when
        userService.findByEmail(user.email)

        // then
        verify(userRepository).findByEmail(user.email)
        verify(userMapper).mapOne(user)
    }
}
