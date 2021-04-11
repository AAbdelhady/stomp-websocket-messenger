package com.aabdelhady.stomp.messenger.system.locale

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor
import org.springframework.web.servlet.i18n.SessionLocaleResolver
import java.util.*

private const val EN = "en"
private const val ET = "et"
private const val RU = "ru"
const val DEFAULT_LANG = ET

@Configuration
class LocaleConfig {
    @Bean
    fun localeResolver() = SessionLocaleResolver().also { it.setDefaultLocale(Locale(DEFAULT_LANG)) }
    @Bean
    fun localeChangeInterceptor() = LocaleChangeInterceptor().also { it.paramName = "lang" }
}
