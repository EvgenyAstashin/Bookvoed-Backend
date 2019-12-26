package com.astashin.bookvoed.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.lang.RuntimeException

@ResponseStatus(HttpStatus.NOT_FOUND)
class BookNotFoundException(isbn: String) : RuntimeException("Book with isbn: $isbn not found")