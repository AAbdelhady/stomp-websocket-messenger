package com.aabdelhady.stomp.messenger.feature.common.util

import org.springframework.data.domain.Page
import java.util.function.Function

data class PageResponse<T>(val content: List<T>, val totalElements: Long)

fun <E, R> mapPageOf(page: Page<E>, mapList: Function<List<E>, List<R>>): PageResponse<R> {
    val responseList = mapList.apply(page.content)
    return PageResponse(responseList, page.totalElements)
}
