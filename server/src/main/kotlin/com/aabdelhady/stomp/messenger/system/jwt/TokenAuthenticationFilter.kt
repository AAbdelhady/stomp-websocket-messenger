package com.aabdelhady.stomp.messenger.system.jwt

import com.aabdelhady.stomp.messenger.feature.user.repository.UserRepository
import com.aabdelhady.stomp.messenger.system.auth.util.JWT_COOKIE_NAME
import com.aabdelhady.stomp.messenger.system.auth.util.getCookie
import com.aabdelhady.stomp.messenger.system.auth.util.setAuthorizedUser
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.UnsupportedJwtException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

private const val AUTH_HEADER_NAME = "Authorization"
private const val TOKEN_PREFIX = "Bearer "

@Component
class TokenAuthenticationFilter(val tokenProvider: TokenProvider, val userRepository: UserRepository) : OncePerRequestFilter() {
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val jwt = getJwtFromCookie(request) ?: getJwtFromHeader(request)
        jwt?.let { setSecurityContextUsingJwt(it) }
        filterChain.doFilter(request, response)
    }

    private fun setSecurityContextUsingJwt(jwt: String) {
        try {
            val userId = tokenProvider.getUserIdFromToken(jwt)
            val user = userRepository.findById(userId).orElseThrow { MalformedJwtException("No user found for ID: $userId") }
            setAuthorizedUser(user)
        } catch (ex: Exception) {
            handleException(ex)
        }
    }

    private fun getJwtFromHeader(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader(AUTH_HEADER_NAME)
        return if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_PREFIX)) bearerToken.substring(TOKEN_PREFIX.length) else null
    }

    private fun getJwtFromCookie(request: HttpServletRequest) = getCookie(request, JWT_COOKIE_NAME)?.value

    private fun handleException(ex: Exception) {
        val logger = LoggerFactory.getLogger(TokenAuthenticationFilter::class.java)
        when(ex) {
            is ExpiredJwtException -> logger.warn("JWT for user with id [{}] expired at {}", ex.claims.subject, ex.claims.expiration.toInstant())
            is MalformedJwtException -> logger.error("Invalid JWT token", ex)
            is UnsupportedJwtException -> logger.error("Unsupported JWT token", ex)
            is IllegalArgumentException -> logger.error("JWT claims string is empty", ex)
            else -> throw ex
        }
    }
}
