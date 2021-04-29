package com.aabdelhady.stomp.messenger.api

import com.aabdelhady.stomp.messenger.feature.dummy.service.DummyUserService
import com.aabdelhady.stomp.messenger.feature.user.service.UserService
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("dummy")
class DummyController(val dummyUserService: DummyUserService) {

    @GetMapping
    fun getDummyUsers() = dummyUserService.getDummyUsers()

    @PostMapping("{count}")
    fun addDummyUsers(@PathVariable count: Int) = dummyUserService.addDummyUsers(count)

    @PostMapping("login/{userId}")
    fun fakeLogin(@PathVariable userId: Long, request: HttpServletRequest, response: HttpServletResponse) = dummyUserService.fakeLogin(userId, request, response)
}
