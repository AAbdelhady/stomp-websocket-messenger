package com.aabdelhady.stomp.messenger.feature.dummy.service

import com.aabdelhady.stomp.messenger.feature.user.mapper.UserMapper
import com.aabdelhady.stomp.messenger.feature.user.model.AuthProvider
import com.aabdelhady.stomp.messenger.feature.user.model.User
import com.aabdelhady.stomp.messenger.feature.user.repository.UserRepository
import com.aabdelhady.stomp.messenger.system.auth.util.*
import com.aabdelhady.stomp.messenger.system.exception.BadRequestException
import com.aabdelhady.stomp.messenger.system.jwt.TokenProvider
import com.github.javafaker.Faker
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.web.DefaultRedirectStrategy
import org.springframework.stereotype.Service
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.transaction.Transactional

private val faker = Faker()

@Service
@Transactional
class DummyUserService(private val userRepository: UserRepository, private val tokenProvider: TokenProvider,
                       private val userMapper: UserMapper) {
    @Value("\${frontend.base-url}") private val frontendBaseUrl: String = ""
    @Value("\${server.secured}") private val isHttps = false

    fun addDummyUsers(count: Int) = repeat(count) {
        val firstName = faker.name().firstName()
        val lastName = faker.name().lastName()
        val email = "$firstName.$lastName@gmail.com"
        val profilePictureUrl = "https://picsum.photos/200/300"
        User(UUID.randomUUID().toString(), AuthProvider.GOOGLE, firstName, lastName, email, profilePictureUrl, dummy = true)
            .let(userRepository::save)
    }

    fun getDummyUsers() = userRepository.findByDummyIsTrue().let { userMapper.mapList(it) }

    fun fakeLogin(userId: Long, request: HttpServletRequest, response: HttpServletResponse) {
        val user = userRepository.findById(userId).filter { it.dummy }.orElseThrow { BadRequestException() }
        setAuthorizedUser(user)
        addTokenCookie(user, response)
    }

    private fun addTokenCookie(loggedInUser: User, response: HttpServletResponse) {
        val token = tokenProvider.createToken(loggedInUser)
        addCookie(response, JWT_COOKIE_NAME, token, JWT_TTL_SECONDS, true, isHttps)
    }
}
