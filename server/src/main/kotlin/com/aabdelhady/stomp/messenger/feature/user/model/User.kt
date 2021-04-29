package com.aabdelhady.stomp.messenger.feature.user.model

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant
import javax.persistence.*
import javax.validation.constraints.Email

@Entity
@Table(name = "users")
data class User(
    @Column(name = "login_id", nullable = false, updatable = false) val loginId: String,
    @Column(name = "auth_provider", nullable = false) @Enumerated(EnumType.STRING) val authProvider: AuthProvider,
    @Column(name = "first_name", nullable = false) var firstName: String,
    @Column(name = "last_name", nullable = false) var lastName: String,
    @Column(name = "email", nullable = false) @Email var email: String,
    @Column(name = "profile_picture_url") var profilePictureUrl: String? = null,
    @Column(name = "phone") var phone: String? = null,
    @Column(name = "is_dummy", nullable = false) val dummy: Boolean = false) {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq_generator")
    @SequenceGenerator(name = "users_seq_generator", sequenceName = "users_seq")
    @Column(name = "id")
    var id: Long = 0

    @Column(name = "created", nullable = false, updatable = false)
    @CreationTimestamp
    val created: Instant? = null

    @Column(name = "modified", nullable = false)
    @UpdateTimestamp
    val modified: Instant? = null
}

data class UserResponse(val id: Long?, val firstName: String, val lastName: String, val profilePictureUrl: String?)
