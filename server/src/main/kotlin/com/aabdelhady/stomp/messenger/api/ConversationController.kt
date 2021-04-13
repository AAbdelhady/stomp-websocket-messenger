package com.aabdelhady.stomp.messenger.api

import com.aabdelhady.stomp.messenger.feature.conversation.service.ConversationService
import com.aabdelhady.stomp.messenger.feature.message.core.model.MessageRequest
import com.aabdelhady.stomp.messenger.feature.message.core.service.MessageService
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("conversation")
class ConversationController(val conversationService: ConversationService, val messageService: MessageService) {
    @GetMapping
    fun findAuthorizedUserConversations() = conversationService.findAuthorizedUserConversations()

    @PostMapping("{conversationId}")
    fun sendMessage(@PathVariable conversationId: Long, @RequestBody @Valid request: MessageRequest) = messageService.sendMessage(conversationId, request)

    @GetMapping("{conversationId}/messages")
    fun findConversationMessages(@PathVariable conversationId: Long) = messageService.findConversationMessages(conversationId)
}
