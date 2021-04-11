package com.aabdelhady.stomp.messenger.test

import com.aabdelhady.stomp.messenger.feature.conversation.model.Conversation
import com.aabdelhady.stomp.messenger.feature.user.model.AuthProvider
import com.aabdelhady.stomp.messenger.feature.user.model.User
import java.util.*

fun createDummyUser(authProvider: AuthProvider = AuthProvider.GOOGLE,
                        firstName: String = randomFirstName(), lastName: String = randomLastName(),
                        email: String = "$firstName.$lastName@gmail.com".toLowerCase()) =
        User(UUID.randomUUID().toString(), authProvider, firstName, lastName, email).apply { id = randomId() }

fun createDummyUsers(count: Int): List<User> {
    val users = mutableListOf<User>()
    repeat(count) { users.add(createDummyUser()) }
    return users
}

fun createDummyConversation(participants: List<User> = createDummyUsers(2)) = Conversation(participants).apply { id = randomId() }
