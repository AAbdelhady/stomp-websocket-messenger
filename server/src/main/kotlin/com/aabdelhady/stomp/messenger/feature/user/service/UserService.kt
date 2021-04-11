package com.aabdelhady.stomp.messenger.feature.user.service

import com.aabdelhady.stomp.messenger.feature.user.mapper.UserMapper
import com.aabdelhady.stomp.messenger.feature.user.model.UserResponse
import com.aabdelhady.stomp.messenger.feature.user.repository.UserRepository
import com.aabdelhady.stomp.messenger.system.auth.util.getAuthorizedUserId
import com.aabdelhady.stomp.messenger.system.auth.util.getAuthorizedUserIdOrThrowUnauthorized
import com.aabdelhady.stomp.messenger.system.exception.BadRequestException
import com.aabdelhady.stomp.messenger.system.exception.UnauthorizedException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserService(private val userRepository: UserRepository, private val userMapper: UserMapper) {
    fun findAll(): List<UserResponse> = userRepository.findAll().let{ userMapper.mapList(it) }

    fun findAuthorizedUser(): UserResponse {
        val authorizedUserId = getAuthorizedUserIdOrThrowUnauthorized()
        val authorizedUser = userRepository.findById(authorizedUserId).orElseThrow { BadRequestException() }
        return userMapper.mapOne(authorizedUser)
    }
}
