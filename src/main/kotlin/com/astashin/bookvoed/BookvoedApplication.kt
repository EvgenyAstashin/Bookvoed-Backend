package com.astashin.bookvoed

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BookvoedApplication

fun main(args: Array<String>) {
    runApplication<BookvoedApplication>(*args)
}
