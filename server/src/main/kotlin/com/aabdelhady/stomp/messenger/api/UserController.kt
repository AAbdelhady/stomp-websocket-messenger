package com.aabdelhady.stomp.messenger.api

import com.aabdelhady.stomp.messenger.feature.user.service.UserService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("user")
class UserController(val userService: UserService) {
    @GetMapping("me")
    fun findAuthorizedUser() = userService.findAuthorizedUser()

    @GetMapping("search/{email}")
    fun findByEmail(@PathVariable email: String) = userService.findByEmail(email)
}
