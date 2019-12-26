package com.astashin.bookvoed

import com.astashin.bookvoed.models.Book
import com.astashin.bookvoed.models.User

interface IBookHandler {

    fun getBookByISBN(isbn: String): Book?

    fun getAllStoredBooks(): List<Book>

    fun isBookExist(isbn: String): Boolean

    fun getMyBooks(user: User): List<Book>
}