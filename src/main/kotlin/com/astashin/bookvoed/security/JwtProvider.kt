package com.astashin.bookvoed.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtProvider {

    @Value("\${vps.app.jwtSecret}")
    lateinit var jwtSecret: String
    @Value("\${vps.app.jwtExpiration}")
    private val jwtExpiration = 0

    fun generateJwtToken(authentication: Authentication): String {
        val userDetails = authentication.principal as UserDetails
        return Jwts.builder()
                .setSubject(userDetails.username)
                .setIssuedAt(Date())
                .setExpiration(Date(Date().time + jwtExpiration))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact()
    }

    fun getUserNameFromJwtToken(token: String?): String {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .body.subject
    }

    fun validateJwtToken(authToken: String?): Boolean {
        return try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken)
            true
        } catch (e: Exception) {
            false
        }
    }
}