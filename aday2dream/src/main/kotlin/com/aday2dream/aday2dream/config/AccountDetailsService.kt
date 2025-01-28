package com.aday2dream.aday2dream.config

import com.aday2dream.aday2dream.repository.AccountRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class AccountDetailsService(private val repository: AccountRepository) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val account = repository.findByUsername(username)
            ?: throw UsernameNotFoundException("User not found with username: $username")

        return User(account.username, account.password, listOf(SimpleGrantedAuthority("ROLE_USER")))
    }
}