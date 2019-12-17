package com.astashin.bookvoed.controllers

import com.astashin.bookvoed.http.requests.LoginRequest
import com.astashin.bookvoed.http.requests.RegistrationRequest
import com.astashin.bookvoed.http.requests.UserDataRequest
import com.astashin.bookvoed.http.responses.Jwt
import com.astashin.bookvoed.http.responses.LoginRegistrationResponse
import com.astashin.bookvoed.models.User
import com.astashin.bookvoed.repositories.UserRepository
import com.astashin.bookvoed.security.JwtProvider
import com.mongodb.MongoWriteException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping(UserController.PATH, produces = [MediaType.APPLICATION_JSON_VALUE])
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

    @PostMapping(path = ["/login"], consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun loginUser(@RequestBody request: LoginRequest): LoginRegistrationResponse {
        val token = generateToken(request.username, request.passphrase)
        val user = userRepository.findByUserName(request.username)
        return LoginRegistrationResponse(user, Jwt(token))
    }

    @PostMapping(path = ["/registration"], consumes = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    fun registerUser(@RequestBody request: RegistrationRequest): LoginRegistrationResponse {
        val user = User(request.username, passwordEncoder.encode(request.passphrase))
        userRepository.insert(user)

        val token = generateToken(request.username, request.passphrase)
        return LoginRegistrationResponse(user, Jwt(token))
    }

    @GetMapping()
    fun getUser(@AuthenticationPrincipal user: User): User {
        return user
    }

    @PatchMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun updateUserData(@RequestBody request: UserDataRequest, @AuthenticationPrincipal user: User): User {
        val updatedUser = request.toUser(user)
        return userRepository.save(updatedUser)
    }

    @ExceptionHandler(MongoWriteException::class)
    fun handleUserInsertingException(response: HttpServletResponse) {
        response.sendError(HttpServletResponse.SC_CONFLICT, "Fail -> Username is already taken!")
    }

    private fun generateToken(username: String, passphrase: String): String {
        val authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(username, passphrase)
        )
        SecurityContextHolder.getContext().authentication = authentication
        return jwtProvider.generateJwtToken(authentication)
    }
}