package com.astashin.bookvoed

import com.astashin.bookvoed.models.Book

interface IBookHandler {

    fun getBookByISBN(isbn: String): Book?

    fun getAllStoredBooks(): List<Book>
}