package com.aabdelhady.stomp.messenger

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class StompMessengerApplication

fun main(args: Array<String>) {
	runApplication<StompMessengerApplication>(*args)
}
