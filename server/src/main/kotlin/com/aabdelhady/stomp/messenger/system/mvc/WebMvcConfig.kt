package com.aabdelhady.stomp.messenger.system.mvc

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor

@Configuration
class WebMvcConfig(val localeChangeInterceptor: LocaleChangeInterceptor) : WebMvcConfigurer {
    @Value("\${frontend.base-url}") private val frontendBaseUrl: String = ""

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(localeChangeInterceptor)
    }

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOriginPatterns("${frontendBaseUrl}*")
            .allowedMethods("*")
            .allowCredentials(true)
    }
}
