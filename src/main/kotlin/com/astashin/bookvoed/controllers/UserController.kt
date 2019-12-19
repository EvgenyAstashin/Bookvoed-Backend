package com.astashin.bookvoed.controllers

import com.astashin.bookvoed.AvatarStorageService
import com.astashin.bookvoed.http.requests.LoginRequest
import com.astashin.bookvoed.http.requests.RegistrationRequest
import com.astashin.bookvoed.http.requests.UploadAvatarRequest
import com.astashin.bookvoed.http.requests.UserDataRequest
import com.astashin.bookvoed.http.responses.Jwt
import com.astashin.bookvoed.http.responses.LoginRegistrationResponse
import com.astashin.bookvoed.http.responses.UploadAvatarResponse
import com.astashin.bookvoed.models.User
import com.astashin.bookvoed.repositories.UserRepository
import com.astashin.bookvoed.security.JwtProvider
import com.mongodb.MongoWriteException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import javax.servlet.http.HttpServletRequest
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
    @Autowired
    lateinit var avatarStorageService: AvatarStorageService

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

    @GetMapping
    fun getUser(@AuthenticationPrincipal user: User): User {
        return user
    }

    @PatchMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun updateUserData(@RequestBody request: UserDataRequest, @AuthenticationPrincipal user: User): User {
        val updatedUser = request.toUser(user)
        return userRepository.save(updatedUser)
    }

    @PostMapping(path = ["/avatars"])
    fun uploadAvatar(@RequestBody request: UploadAvatarRequest, @AuthenticationPrincipal user: User): UploadAvatarResponse {
        val fileName = avatarStorageService.storeAvatar(request.avatarBase64, request.extension, user.userName)
        val avatarDownloadingUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/users/avatars/")
                .path(fileName)
                .toUriString()

        user.avatar = avatarDownloadingUri
        userRepository.save(user)
        return UploadAvatarResponse(avatarDownloadingUri)
    }

    @GetMapping(path = ["/avatars/{fileName:.+}"])
    fun getAvatar(@PathVariable fileName: String, request: HttpServletRequest): ResponseEntity<Resource> {
        val resource = avatarStorageService.loadAvatarAsResource(fileName)

        var contentType = request.servletContext.getMimeType(resource?.file?.absolutePath)

        if(contentType == null)
            contentType = "application/octet-stream"

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource?.filename + "\"")
                .body(resource)
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