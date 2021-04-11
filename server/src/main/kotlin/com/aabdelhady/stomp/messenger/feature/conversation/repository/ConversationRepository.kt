package com.aabdelhady.stomp.messenger.feature.conversation.repository

import com.aabdelhady.stomp.messenger.feature.conversation.model.Conversation
import com.aabdelhady.stomp.messenger.system.util.optionalResult
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*
import javax.persistence.EntityManager

@Repository
interface ConversationRepository : JpaRepository<Conversation, Long>, ConversationRepositoryCustom {
    fun findByParticipantsId(participantsId: Long): List<Conversation>
}

interface ConversationRepositoryCustom {
    fun findByExactParticipantsIds(participantsIds: Collection<Long>): Optional<Conversation>
}

@Repository
class ConversationRepositoryImpl(val entityManager: EntityManager) : ConversationRepositoryCustom {
    override fun findByExactParticipantsIds(participantsIds: Collection<Long>): Optional<Conversation> {
        val sql = """
            select c.* from conversations c join conversation_participants cp
            on c.id = cp.conversation_id
            where cp.user_id in (:user_ids)
            and not exists (select 1 from conversation_participants cp2
                where cp2.conversation_id = c.id and cp2.user_id not in (:user_ids))
            group by c.id
            having count(1) = :count
        """.trimIndent()
        return entityManager.createNativeQuery(sql, Conversation::class.java)
            .setParameter("user_ids", participantsIds)
            .setParameter("count", participantsIds.size)
            .let { optionalResult(it) }
    }
}

