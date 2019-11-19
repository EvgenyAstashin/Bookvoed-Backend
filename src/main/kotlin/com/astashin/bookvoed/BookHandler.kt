package com.astashin.bookvoed

import com.astashin.bookvoed.models.Book
import com.astashin.bookvoed.models.google.GoogleBookResponse
import com.astashin.bookvoed.repositories.BookRepository
import com.google.gson.Gson
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import java.net.URL


class BookHandler : IBookHandler {

    @Autowired
    lateinit var bookRepository: BookRepository

    override fun getBookByISBN(isbn: String): Book? {
        var book = bookRepository.findByIdOrNull(isbn)
        if(book != null)
            return book
        book = loadBookFromGoogleBooks(isbn).toBook()
        saveBookToDB(book)
        return book
    }

    override fun getAllStoredBooks(): List<Book> {
        return bookRepository.findAll()
    }

    private fun loadBookFromGoogleBooks(isbn: String): GoogleBookResponse {
        val googleBookJson =  URL("https://www.googleapis.com/books/v1/volumes?q=isbn:$isbn&key=AIzaSyDau66ejImbYqQznYhJ8qEF_tw440myAIQ").readText()
        return Gson().fromJson<GoogleBookResponse>(googleBookJson, GoogleBookResponse::class.java)
    }

    private fun saveBookToDB(book: Book?) {
        if(book != null)
            bookRepository.insert(book)
    }
}