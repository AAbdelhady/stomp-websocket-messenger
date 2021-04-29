package com.aabdelhady.stomp.messenger.system.auth

import com.aabdelhady.stomp.messenger.system.auth.util.JWT_COOKIE_NAME
import com.aabdelhady.stomp.messenger.system.auth.util.addCookie
import com.aabdelhady.stomp.messenger.system.jwt.TokenAuthenticationFilter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
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
import org.springframework.security.web.authentication.HttpStatusEntryPoint
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler
import org.springframework.security.web.authentication.logout.LogoutHandler
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpServletResponse.SC_OK
import javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
class SecurityConfig(
    val tokenAuthenticationFilter: TokenAuthenticationFilter,
    val socialAuthenticationSuccessHandler: AuthenticationSuccessHandler,
    val socialAuthenticationFailureHandler: AuthenticationFailureHandler,
    val logoutHandler: CustomLogoutHandler
) : WebSecurityConfigurerAdapter() {

    override fun configure(web: WebSecurity) {
        web.ignoring().mvcMatchers("/swagger-ui.html/**", "/configuration/**", "/swagger-resources/**", "/v2/api-docs", "/webjars/**", "/image/**")
    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http
            .cors()
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers("/dummy", "/dummy/**").permitAll()
            .anyRequest().authenticated()
            .and()
            .oauth2Login()
            .successHandler(socialAuthenticationSuccessHandler)
            .failureHandler(socialAuthenticationFailureHandler)
            .and()
            .logout()
            .addLogoutHandler(logoutHandler)
            .logoutSuccessHandler(HttpStatusReturningLogoutSuccessHandler())
            .and()
            .exceptionHandling()
            .authenticationEntryPoint(HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
            .and()
            .csrf()
            .disable()
            .addFilterBefore(tokenAuthenticationFilter, BasicAuthenticationFilter::class.java)
    }
}

@Component
class CustomLogoutHandler : LogoutHandler {
    @Value("\${server.secured:false}") private val isHttps = false
    override fun logout(request: HttpServletRequest, response: HttpServletResponse, authentication: Authentication?) {
        addCookie(response, JWT_COOKIE_NAME, null, 0, true, isHttps)
    }
}
