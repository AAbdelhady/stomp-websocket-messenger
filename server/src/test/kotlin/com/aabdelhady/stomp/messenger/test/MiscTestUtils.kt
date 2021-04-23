package com.aabdelhady.stomp.messenger.test

import org.springframework.security.core.context.SecurityContextHolder

fun clearAuthorizedUser() {
    SecurityContextHolder.getContext().authentication = null
}
