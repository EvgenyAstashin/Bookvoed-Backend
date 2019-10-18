package com.astashin.bookvoed.models.repositories

import com.astashin.bookvoed.models.Book
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository

@Component
@Repository
interface  BookRepository : MongoRepository<Book, String> {
}