package com.aabdelhady.stomp.messenger.feature.user.repository

import com.aabdelhady.stomp.messenger.feature.user.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository: JpaRepository<User, Long> {
    fun findByLoginId(loginId: String): Optional<User>
    fun findByEmail(email: String): Optional<User>
}
