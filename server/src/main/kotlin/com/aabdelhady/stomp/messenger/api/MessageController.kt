package com.aabdelhady.stomp.messenger.api

import com.aabdelhady.stomp.messenger.feature.message.core.model.MessageRequest
import com.aabdelhady.stomp.messenger.feature.message.core.service.MessageService
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("message")
class MessageController(val messageService: MessageService) {
    @PostMapping
    fun sendMessage(@RequestBody @Valid request: MessageRequest) = messageService.sendMessage(request)

    @GetMapping("{conversationId}")
    fun findConversationMessages(@PathVariable conversationId: Long) = messageService.findConversationMessages(conversationId)
}
