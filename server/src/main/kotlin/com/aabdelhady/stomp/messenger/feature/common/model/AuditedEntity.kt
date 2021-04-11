package com.aabdelhady.stomp.messenger.feature.common.model

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant
import javax.persistence.Column
import javax.persistence.MappedSuperclass


@MappedSuperclass
abstract class AuditedEntity {
    @Column(name = "created", nullable = false, updatable = false)
    @CreationTimestamp
    val created: Instant? = null

    @Column(name = "modified", nullable = false)
    @UpdateTimestamp
    val modified: Instant? = null
}

