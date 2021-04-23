package com.aabdelhady.stomp.messenger.system.auth.util

import com.aabdelhady.stomp.messenger.feature.user.model.User
import com.aabdelhady.stomp.messenger.system.exception.UnauthorizedException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.context.SecurityContextHolder
import java.util.*
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

const val JWT_COOKIE_NAME = "JWT"
const val JWT_HEADER_NAME = "JWT"
const val JWT_TTL_HEADER_NAME = "JWT_TTL_SEC"
const val JWT_TTL_SECONDS = 86400

fun getAuthorizedUserIdOrThrowUnauthorized(): Long = getAuthorizedUserId().orElseThrow { UnauthorizedException() }

fun getAuthorizedUserId(): Optional<Long> {
    val auth = SecurityContextHolder.getContext().authentication
    return if (auth != null && auth.principal != null) Optional.of(auth.principal.toString().toLong()) else Optional.empty()
}

fun setAuthorizedUser(user: User) {
    val updatedAuthorities = AuthorityUtils.createAuthorityList("ROLE_USER")
    SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(user.id, null, updatedAuthorities)
}

fun getCookie(request: HttpServletRequest, name: String) = request.cookies?.find { it.name == name }

fun addCookie(response: HttpServletResponse, name: String, value: String?, maxAge: Int, httpOnly: Boolean, secure: Boolean) {
    response.addCookie(cookie(name, value, maxAge, httpOnly, secure))
}

private fun cookie(name: String, value: String?, maxAge: Int, httpOnly: Boolean, secure: Boolean) = Cookie(name, value).apply {
    this.path = "/"
    this.isHttpOnly = httpOnly
    this.maxAge = maxAge
    this.secure = secure
}
