package com.aabdelhady.stomp.messenger.api

import com.aabdelhady.stomp.messenger.feature.user.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("user")
class UserController(val userService: UserService) {
    @GetMapping
    fun findAll() = userService.findAll()

    @GetMapping("me")
    fun findAuthorizedUser() = userService.findAuthorizedUser()
}
