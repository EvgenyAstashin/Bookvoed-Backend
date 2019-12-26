package com.astashin.bookvoed.controllers

import com.astashin.bookvoed.IBookHandler
import com.astashin.bookvoed.exceptions.BookNotFoundException
import com.astashin.bookvoed.http.requests.AddBookRequest
import com.astashin.bookvoed.models.Book
import com.astashin.bookvoed.models.User
import com.astashin.bookvoed.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(BookController.PATH, produces = [MediaType.APPLICATION_JSON_VALUE])
class BookController {

    companion object {
        const val PATH = "/books"
    }

    @Autowired
    lateinit var bookHandler: IBookHandler
    @Autowired
    lateinit var userRepository: UserRepository

    @GetMapping(value = ["/{isbn}"])
    fun getBookByISBN(@PathVariable isbn: String): Book? {
        return bookHandler.getBookByISBN(isbn)
    }

    @GetMapping()
    fun getAllStoredBooks(): List<Book> {
        return bookHandler.getAllStoredBooks()
    }

    @GetMapping(value = ["/my"])
    fun getAllMyBooks(@AuthenticationPrincipal user: User): List<Book> {
        return bookHandler.getMyBooks(user)
    }

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun addBookToByBooksList(@RequestBody request: AddBookRequest, @AuthenticationPrincipal user: User) {
        if(bookHandler.isBookExist(request.isbn)) {
            user.myBooks.add(request.isbn)
            userRepository.save(user)
        } else {
            throw BookNotFoundException(request.isbn)
        }
    }
}