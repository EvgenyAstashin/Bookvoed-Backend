package com.astashin.bookvoed.models.google

import com.astashin.bookvoed.models.Book

data class GoogleBookResponse(
    val items: List<Item>,
    val kind: String,
    val totalItems: Int
) {
    fun toBook(): Book? {
        if(totalItems != 0) {
            val googleBookItem = items.first()
            val book = Book()
            book.isbn = getISBN(googleBookItem)
            book.title = googleBookItem.volumeInfo.title
            book.subtitle = googleBookItem.volumeInfo.subtitle
            book.description = googleBookItem.volumeInfo.description
            book.authors = googleBookItem.volumeInfo.authors
            book.pages = googleBookItem.volumeInfo.pageCount
            book.publisher = ""
            book.publishedDate = googleBookItem.volumeInfo.publishedDate
            return book
        }
        return null
    }

    private fun getISBN(googleBookItem: Item): String {
        googleBookItem.volumeInfo.industryIdentifiers.forEach{
            if(it.type == "ISBN_13")
                return it.identifier
        }
        return googleBookItem.volumeInfo.industryIdentifiers.first().identifier
    }
}