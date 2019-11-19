package com.astashin.bookvoed.security


import com.astashin.bookvoed.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class AppUserDetailsService : UserDetailsService {

    @Autowired
    lateinit var userRepository: UserRepository

    override fun loadUserByUsername(name: String): UserDetails {
        val user = userRepository.findByUserName(name)
        return user ?: throw UsernameNotFoundException("User name not found")
    }
}