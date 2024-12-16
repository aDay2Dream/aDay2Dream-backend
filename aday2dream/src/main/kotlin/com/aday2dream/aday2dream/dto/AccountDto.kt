package com.aday2dream.aday2dream.dto
import java.sql.Timestamp

data class AccountDto (
    val accountId: Long? = null,
    val username: String,
    val password: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val profilePicture: String? = null,
    val description: String? = null,
    val links: String? = null,
    val createdAt: Timestamp? = null,
    val updatedAt: Timestamp? = null
)
