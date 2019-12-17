package com.astashin.bookvoed.http.responses

import com.astashin.bookvoed.models.User

class LoginRegistrationResponse(val user: User?, val jwt: Jwt)