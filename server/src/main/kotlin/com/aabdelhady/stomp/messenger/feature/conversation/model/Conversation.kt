package com.aabdelhady.stomp.messenger.feature.conversation.model

import com.aabdelhady.stomp.messenger.feature.message.core.model.Message
import com.aabdelhady.stomp.messenger.feature.message.core.model.MessageResponse
import com.aabdelhady.stomp.messenger.feature.user.model.User
import com.aabdelhady.stomp.messenger.feature.user.model.UserResponse
import com.fasterxml.jackson.annotation.JsonInclude
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.Formula
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant
import javax.persistence.*

@Entity
@Table(name = "conversations")
class Conversation(participants: List<User>) {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "conversations_seq_generator")
    @SequenceGenerator(name = "conversations_seq_generator", sequenceName = "conversations_seq")
    @Column(name = "id")
    var id: Long = 0

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "conversation_participants", joinColumns = [JoinColumn(name = "conversation_id")], inverseJoinColumns = [JoinColumn(name = "user_id")])
    val participants: List<User> = participants

    @Column(name = "created", nullable = false, updatable = false)
    @CreationTimestamp
    val created: Instant? = null

    @Column(name = "modified", nullable = false)
    @UpdateTimestamp
    val modified: Instant? = null
}

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ConversationResponse(val id: Long, val participants: List<UserResponse>, val lastMessage: MessageResponse?)
