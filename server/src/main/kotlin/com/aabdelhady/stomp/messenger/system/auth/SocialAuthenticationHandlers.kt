package com.aabdelhady.stomp.messenger.system.auth

import com.aabdelhady.stomp.messenger.feature.user.model.AuthProvider
import com.aabdelhady.stomp.messenger.feature.user.model.User
import com.aabdelhady.stomp.messenger.feature.user.repository.UserRepository
import com.aabdelhady.stomp.messenger.system.auth.util.*
import com.aabdelhady.stomp.messenger.system.jwt.TokenProvider
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.web.DefaultRedirectStrategy
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

private const val REDIRECT_PAGE = "/redirect"

@Component
class SocialAuthenticationSuccessHandler(val userRepository: UserRepository, val tokenProvider: TokenProvider) : AuthenticationSuccessHandler {
    @Value("\${frontend.base-url}") private val frontendBaseUrl: String = ""
    @Value("\${server.secured}") private val isHttps = false

    override fun onAuthenticationSuccess(request: HttpServletRequest, response: HttpServletResponse, authentication: Authentication) {
        val details = getSocialUserDetails(authentication)
        val loggedInUser = userRepository.findByLoginId(details.loginId)
            .map { updateExistingSocialLoginUser(it, details) }
            .orElseGet { registerNewSocialLoginUser(details) }
//        val loggedInUser = if (userOptional.isPresent) updateExistingSocialLoginUser(userOptional.get(), details) else registerNewSocialLoginUser(details)
        updateAuthorizedUserContext(loggedInUser)
        addTokenToResponse(loggedInUser, response)
        DefaultRedirectStrategy().sendRedirect(request, response, frontendBaseUrl + REDIRECT_PAGE)
    }

    private fun addTokenToResponse(loggedInUser: User, response: HttpServletResponse) {
        val token = tokenProvider.createToken(loggedInUser)
        addCookie(response, JWT_COOKIE_NAME, token, JWT_TTL_SECONDS, true, isHttps) // for webapps
        response.setHeader(JWT_HEADER_NAME, token) // for mobile clients
        response.setHeader(JWT_TTL_HEADER_NAME, java.lang.String.valueOf(JWT_TTL_SECONDS))
    }

    private fun getSocialUserDetails(authentication: Authentication): SocialUserDetails {
        val socialDetailsMap = (authentication.principal as OAuth2User).attributes
        val socialLoginProviderName = (authentication as OAuth2AuthenticationToken).authorizedClientRegistrationId
        return when (AuthProvider.valueOf(socialLoginProviderName.toUpperCase())) {
            AuthProvider.FACEBOOK -> parseFacebookUserDetails(socialDetailsMap)
            AuthProvider.GOOGLE -> parseGoogleUserDetails(socialDetailsMap)
        }
    }

    private fun updateExistingSocialLoginUser(user: User, details: SocialUserDetails) = user.run {
        email = details.email
        firstName = details.firstName
        lastName = details.lastName
        profilePictureUrl = details.profilePictureUrl
        userRepository.save(user)
    }

    private fun registerNewSocialLoginUser(details: SocialUserDetails) = details.run {
        val user = User(loginId, authProvider, firstName, lastName, email, profilePictureUrl)
        userRepository.save(user)
    }
}

@Component
class SocialAuthenticationFailureHandler : AuthenticationFailureHandler {
    override fun onAuthenticationFailure(request: HttpServletRequest, response: HttpServletResponse, exception: AuthenticationException) {
        val logger = LoggerFactory.getLogger(SocialAuthenticationFailureHandler::class.java)
        logger.error(exception.message, exception)
        response.status = HttpServletResponse.SC_UNAUTHORIZED
    }
}

private data class SocialUserDetails(
        val loginId: String, val firstName: String, val lastName: String,
        val email: String, val authProvider: AuthProvider, val profilePictureUrl: String?
)

private fun parseFacebookUserDetails(socialDetailsMap: Map<String, Any>) = SocialUserDetails(
        socialDetailsMap["id"] as String, socialDetailsMap["first_name"] as String, socialDetailsMap["last_name"] as String,
        socialDetailsMap["email"] as String, AuthProvider.FACEBOOK, getFacebookProfilePictureUrl(socialDetailsMap)
)

private fun parseGoogleUserDetails(socialDetailsMap: Map<String, Any>) = SocialUserDetails(
        socialDetailsMap["sub"] as String, socialDetailsMap["given_name"] as String, socialDetailsMap["family_name"] as String,
        socialDetailsMap["email"] as String, AuthProvider.GOOGLE, socialDetailsMap["picture"] as String
)

private fun getFacebookProfilePictureUrl(socialDetailsMap: Map<String, Any>) =
    ((socialDetailsMap["picture"] as Map<*, *>)["data"] as LinkedHashMap<*, *>)["url"] as String?
