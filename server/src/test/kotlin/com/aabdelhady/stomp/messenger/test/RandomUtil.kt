package com.aabdelhady.stomp.messenger.test

import com.github.javafaker.Faker
import java.util.*

private val faker = Faker()

fun randomWord(): String = faker.lorem().word()
fun randomSentence(): String = faker.lorem().sentence()
fun randomFirstName(): String = faker.name().firstName()
fun randomLastName(): String = faker.name().lastName()

fun random(min: Long, max: Long): Long = Random().longs(min, max).findFirst().asLong

fun randomInt(min: Int, max: Int): Int = random(min.toLong(), max.toLong()).toInt()

fun randomId() = random(0, 10000)

private fun <T> randomItem(items: Array<T>) = items[randomInt(0, items.size)]
