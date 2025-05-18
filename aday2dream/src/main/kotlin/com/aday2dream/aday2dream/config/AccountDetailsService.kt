package com.aday2dream.aday2dream.config

import com.aday2dream.aday2dream.controller.AccountController
import com.aday2dream.aday2dream.repository.AccountRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class AccountDetailsService(private val repository: AccountRepository) : UserDetailsService {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(AccountController::class.java)
    }

    override fun loadUserByUsername(username: String): UserDetails {
        logger.info("Loading user by username")
        val account = repository.findByUsername(username)
            ?: throw UsernameNotFoundException("User not found with username: $username")

        return User(account.username, account.password, listOf(SimpleGrantedAuthority("ROLE_USER")))
    }
}