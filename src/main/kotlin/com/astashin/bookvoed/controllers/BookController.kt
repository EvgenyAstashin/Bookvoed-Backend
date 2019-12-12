package com.astashin.bookvoed.controllers

import com.astashin.bookvoed.IBookHandler
import com.astashin.bookvoed.models.Book
import com.astashin.bookvoed.models.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(BookController.PATH)
class BookController {

    companion object {
        const val PATH = "/books"
    }

    @Autowired
    lateinit var bookHandler: IBookHandler

    @GetMapping(value = ["/{isbn}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getBookByISBN(@PathVariable isbn: String): Book? {
        return bookHandler.getBookByISBN(isbn)
    }

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getAllStoredBooks(): List<Book> {
        return bookHandler.getAllStoredBooks()
    }

    @PostMapping
    fun addBookToUserList(@AuthenticationPrincipal user: User) {

    }
}