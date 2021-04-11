package com.aabdelhady.stomp.messenger.system.util

import java.util.*
import javax.persistence.Query

fun <T> optionalResult(q: Query): Optional<T> {
    return Optional.ofNullable(singleResult(q))
}

fun <T> singleResult(q: Query): T? {
    val l: List<T> = resultList(q)
    if (l.isEmpty()) return null
    check(l.size == 1) { "Unique result expected, found " + l.size }
    return l[0]
}

fun <T> resultList(q: Query): List<T> {
    return q.resultList as List<T>
}
