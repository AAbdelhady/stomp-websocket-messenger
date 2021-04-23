package com.aabdelhady.stomp.messenger.feature.message.core.model

import com.aabdelhady.stomp.messenger.feature.conversation.model.Conversation
import com.aabdelhady.stomp.messenger.feature.user.model.User
import com.aabdelhady.stomp.messenger.feature.user.model.UserResponse
import com.fasterxml.jackson.annotation.JsonInclude
import org.hibernate.annotations.CreationTimestamp
import java.time.Instant
import javax.persistence.*
import javax.validation.constraints.NotEmpty

@Entity
@Table(name = "messages")
data class Message(
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "conversation_id", nullable = false) val conversation: Conversation,
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "sender_id", nullable = false) val sender: User,
    @Column(name = "text", nullable = false, updatable = false) val text: String) {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "messages_seq_generator")
    @SequenceGenerator(name = "messages_seq_generator", sequenceName = "messages_seq")
    @Column(name = "id")
    var id: Long = 0

    @Column(name = "sent", nullable = false, updatable = false)
    @CreationTimestamp
    var sent: Instant? = null
}

data class MessageRequest(@NotEmpty val text: String)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class MessageResponse(val id: Long, val text: String, val sender: UserResponse, val conversationId: Long, val sent: Instant)
