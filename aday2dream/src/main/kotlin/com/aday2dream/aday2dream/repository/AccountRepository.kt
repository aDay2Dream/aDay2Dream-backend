package com.aday2dream.aday2dream.repository

import com.aday2dream.aday2dream.model.Account
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.stereotype.Repository

@Repository
@EnableJpaRepositories
interface AccountRepository : JpaRepository<Account, Long> {
    fun findByUsername(username: String) : Account?
    fun existsByUsername(username: String) : Boolean
    fun existsByEmail(email: String) : Boolean
}
