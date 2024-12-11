package com.aday2dream.aday2dream.service

import com.aday2dream.aday2dream.repository.CredentialsRepository
import com.aday2dream.aday2dream.model.Credentials
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class CredentialsService(
    private val credentialsRepository: CredentialsRepository,
    private val passwordEncoder: BCryptPasswordEncoder
) {
    fun register(username: String, password: String): Credentials {
        if (credentialsRepository.findByUsername(username) != null) {
            throw IllegalArgumentException("Username already exists")
        }
        val hashedPassword = passwordEncoder.encode(password)
        return credentialsRepository.save(Credentials(username = username, password = hashedPassword))
    }

    fun login(username: String, password: String): Boolean {
        val user = credentialsRepository.findByUsername(username) ?: return false
        return passwordEncoder.matches(password, user.password)
    }
}
