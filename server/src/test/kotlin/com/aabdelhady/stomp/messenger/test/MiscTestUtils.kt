package com.aabdelhady.stomp.messenger.test

import org.mockito.Mockito
import org.springframework.security.core.context.SecurityContextHolder

fun <T> anyOf(type: Class<T>): T = Mockito.any<T>(type)

fun clearAuthorizedUser() {
    SecurityContextHolder.getContext().authentication = null
}
