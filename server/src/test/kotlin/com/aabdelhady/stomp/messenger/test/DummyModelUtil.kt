package com.aabdelhady.stomp.messenger.test

import com.aabdelhady.stomp.messenger.feature.conversation.model.Conversation
import com.aabdelhady.stomp.messenger.feature.message.core.model.Message
import com.aabdelhady.stomp.messenger.feature.message.core.model.MessageResponse
import com.aabdelhady.stomp.messenger.feature.user.model.AuthProvider
import com.aabdelhady.stomp.messenger.feature.user.model.User
import com.aabdelhady.stomp.messenger.feature.user.model.UserResponse
import java.time.Instant
import java.util.*

fun createDummyUser(
    authProvider: AuthProvider = AuthProvider.GOOGLE,
    firstName: String = randomFirstName(), lastName: String = randomLastName(),
    email: String = "$firstName.$lastName@gmail.com".toLowerCase(),
    profilePictureUrl: String = "https://picsum.photos/200"
) = User(UUID.randomUUID().toString(), authProvider, firstName, lastName, email, profilePictureUrl).apply { id = randomId() }

fun createDummyUsers(count: Int): List<User> {
    val users = mutableListOf<User>()
    repeat(count) { users.add(createDummyUser()) }
    return users
}

fun createDummyConversation(participants: List<User> = createDummyUsers(2)) = Conversation(participants).apply { id = randomId() }

fun createDummyMessage(conversation: Conversation, sender: User, text: String = randomSentence()) = Message(conversation, sender, text).apply {
    id = randomId()
    sent = Instant.now()
}

fun User.response() = UserResponse(id, firstName, lastName, profilePictureUrl)
fun Message.response() = MessageResponse(id, text, sender.response(), conversation.id, sent!!)
