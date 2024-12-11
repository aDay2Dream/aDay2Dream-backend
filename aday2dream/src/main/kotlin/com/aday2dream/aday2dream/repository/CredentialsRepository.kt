package com.aday2dream.aday2dream.repository
import com.aday2dream.aday2dream.model.Credentials
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CredentialsRepository : JpaRepository<Credentials, Long> {
    fun findByUsername(username: String): Credentials?
}