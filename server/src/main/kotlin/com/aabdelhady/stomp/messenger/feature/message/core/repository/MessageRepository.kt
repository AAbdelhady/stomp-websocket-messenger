package com.aabdelhady.stomp.messenger.feature.message.core.repository

import com.aabdelhady.stomp.messenger.feature.conversation.model.Conversation
import com.aabdelhady.stomp.messenger.feature.message.core.model.Message
import com.aabdelhady.stomp.messenger.system.util.resultList
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager

@Repository
interface MessageRepository : JpaRepository<Message, Long>, MessageRepositoryCustom {
    fun findByConversationId(conversationId: Long): List<Message>
}

interface MessageRepositoryCustom {
    fun findConversationsLastMessages(conversationIds: List<Long>): Map<Long, Message>
}

@Repository
class MessageRepositoryImpl(private val entityManager: EntityManager) : MessageRepositoryCustom {
    override fun findConversationsLastMessages(conversationIds: List<Long>): Map<Long, Message> {
        val sql = """
            select m.* from messages m where m.id in (
                select (select m2.id from messages m2 where m2.conversation_id = c.id order by m2.sent desc limit 1)
                from conversations c where c.id in (:conversation_ids)
            )"""
        val q = entityManager.createNativeQuery(sql, Message::class.java).setParameter("conversation_ids", conversationIds)
        return resultList<Message>(q).associateBy { it.conversation.id }
    }
}
