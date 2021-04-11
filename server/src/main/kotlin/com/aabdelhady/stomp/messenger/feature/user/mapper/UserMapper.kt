package com.aabdelhady.stomp.messenger.feature.user.mapper

import com.aabdelhady.stomp.messenger.feature.user.model.User
import com.aabdelhady.stomp.messenger.feature.user.model.UserResponse
import org.springframework.stereotype.Component

@Component
class UserMapper {
    fun mapList(users: List<User>) = users.map(this::mapOne)
    fun mapOne(user: User) = with(user) { UserResponse(id, firstName, lastName, profilePictureUrl) }
}
