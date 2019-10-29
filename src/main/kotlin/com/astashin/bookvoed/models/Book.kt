package com.astashin.bookvoed.models

import org.springframework.data.annotation.Id

class Book() {

    @Id
    var isbn: String = ""
    var title: String = ""
    var subtitle: String = ""
    var description: String = ""
    var authors: List<String> = listOf()
    var pages: Int = 0
    var publisher: String = ""
    var publishedDate: String = ""
    var image = ""
}