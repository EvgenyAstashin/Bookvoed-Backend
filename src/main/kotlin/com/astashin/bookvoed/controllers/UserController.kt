package com.astashin.bookvoed.controllers

import com.astashin.bookvoed.http.requests.LoginRequest
import com.astashin.bookvoed.http.requests.RegistrationRequest
import com.astashin.bookvoed.http.responses.Jwt
import com.astashin.bookvoed.models.User
import com.astashin.bookvoed.repositories.UserRepository
import com.astashin.bookvoed.security.JwtProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(UserController.PATH)
class UserController {

    companion object {
        const val PATH = "/users"
    }

    @Autowired
    lateinit var userRepository: UserRepository
    @Autowired
    lateinit var passwordEncoder: PasswordEncoder
    @Autowired
    lateinit var authenticationManager: AuthenticationManager
    @Autowired
    lateinit var jwtProvider: JwtProvider

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getAllUsers(): List<User> {
        return userRepository.findAll()
    }

    @PostMapping(path = ["/login"], consumes = [MediaType.APPLICATION_JSON_VALUE],
            produces = [MediaType.APPLICATION_JSON_VALUE])
    fun loginUser(@RequestBody request: LoginRequest): Jwt {
        val token = generateToken(request.username, request.passphrase)
        return Jwt(token)
    }

    @PostMapping(path = ["/registration"], consumes = [MediaType.APPLICATION_JSON_VALUE],
            produces = [MediaType.APPLICATION_JSON_VALUE])
    fun registerUser(@RequestBody request: RegistrationRequest): Jwt {
        userRepository.insert(User(request.username, passwordEncoder.encode(request.passphrase)))

        val token = generateToken(request.username, request.passphrase)
        return Jwt(token)
    }

    private fun generateToken(username: String, passphrase: String): String {
        val authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(username, passphrase)
        )
        SecurityContextHolder.getContext().authentication = authentication
        return jwtProvider.generateJwtToken(authentication)
    }
}