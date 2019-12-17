package com.astashin.bookvoed

import com.astashin.bookvoed.properties.AvatarStorageProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(
    AvatarStorageProperties::class
)
class BookvoedApplication

fun main(args: Array<String>) {
    runApplication<BookvoedApplication>(*args)
}
