package com.astashin.bookvoed.repositories

import com.astashin.bookvoed.models.User
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository

@Component
@Repository
interface UserRepository : MongoRepository<User, String> {

    fun findByUserName(userName: String?): User?

    fun existsByUserName(userName: String?): Boolean
}