package com.aabdelhady.stomp.messenger.feature.user.service

import com.aabdelhady.stomp.messenger.feature.user.mapper.UserMapper
import com.aabdelhady.stomp.messenger.feature.user.model.UserResponse
import com.aabdelhady.stomp.messenger.feature.user.repository.UserRepository
import com.aabdelhady.stomp.messenger.system.auth.getAuthorizedUserIdOrThrowUnauthorized
import com.aabdelhady.stomp.messenger.system.exception.BadRequestException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserService(private val userRepository: UserRepository, private val userMapper: UserMapper) {
    fun findAuthorizedUser(): UserResponse {
        val authorizedUserId = getAuthorizedUserIdOrThrowUnauthorized()
        val authorizedUser = userRepository.findById(authorizedUserId).orElseThrow { BadRequestException() }
        return userMapper.mapOne(authorizedUser)
    }

    fun findByEmail(email: String): UserResponse {
        val user = userRepository.findByEmail(email).orElseThrow { BadRequestException() }
        return userMapper.mapOne(user)
    }
}
