package com.astashin.bookvoed.http.requests

import com.astashin.bookvoed.models.User

class UserDataRequest {

    var firstName: String? = null
    var secondName: String? = null

    fun toUser(user: User): User {
        user.firstName = firstName
        user.secondName = secondName
        return user
    }
}