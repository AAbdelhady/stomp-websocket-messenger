package com.aabdelhady.stomp.messenger.system.auth

import com.aabdelhady.stomp.messenger.system.auth.util.JWT_COOKIE_NAME
import com.aabdelhady.stomp.messenger.system.auth.util.addCookie
import com.aabdelhady.stomp.messenger.system.jwt.TokenAuthenticationFilter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.security.web.authentication.logout.LogoutHandler
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
class SecurityConfig(
    val tokenAuthenticationFilter: TokenAuthenticationFilter,
    val socialAuthenticationSuccessHandler: AuthenticationSuccessHandler,
    val socialAuthenticationFailureHandler: AuthenticationFailureHandler,
    val authenticationEntryPoint: CustomAuthenticationEntryPoint,
    val logoutHandler: CustomLogoutHandler
) : WebSecurityConfigurerAdapter() {
    @Value("\${frontend.base-url}") private val frontendBaseUrl: String = ""

    override fun configure(web: WebSecurity) {
        web.ignoring().mvcMatchers("/swagger-ui.html/**", "/configuration/**", "/swagger-resources/**", "/v2/api-docs", "/webjars/**", "/image/**")
    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http
            .antMatcher("/**")
            .cors()
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers("/test").permitAll()
            .antMatchers("/**").authenticated()
            .and()
            .oauth2Login()
            .successHandler(socialAuthenticationSuccessHandler)
            .failureHandler(socialAuthenticationFailureHandler)
            .and()
            .logout()
            .addLogoutHandler(logoutHandler)
            .logoutSuccessUrl(frontendBaseUrl)
            .and()
            .exceptionHandling()
            .authenticationEntryPoint(authenticationEntryPoint)
            .and()
            .csrf()
            .disable()
            .addFilterBefore(tokenAuthenticationFilter, BasicAuthenticationFilter::class.java)
        // .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
    }
}

@Component
class CustomAuthenticationEntryPoint : AuthenticationEntryPoint {
    override fun commence(request: HttpServletRequest, response: HttpServletResponse, e: AuthenticationException) {
        response.status = HttpServletResponse.SC_UNAUTHORIZED
    }
}

@Component
class CustomLogoutHandler : LogoutHandler {
    @Value("\${server.secured:false}") private val isHttps = false
    override fun logout(request: HttpServletRequest, response: HttpServletResponse, authentication: Authentication) {
        addCookie(response, JWT_COOKIE_NAME, null, 0, true, isHttps)
    }
}
