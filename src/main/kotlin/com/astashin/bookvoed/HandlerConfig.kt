package com.astashin.bookvoed

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class HandlerConfig {

    @Bean
    fun bookHandlerBean(): IBookHandler {
        return BookHandler()
    }
}