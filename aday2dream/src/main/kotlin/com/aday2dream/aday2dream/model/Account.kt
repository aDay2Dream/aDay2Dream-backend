package com.aday2dream.aday2dream.model

import java.sql.Timestamp
import jakarta.persistence.*
import lombok.*


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "account")
data class Account(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val accountId: Long? = null,

    @Column(name="username", nullable = false, unique = true)
    val username: String,

    @Column(name="password", nullable = false)
    val password: String,

    @Column(name = "email", nullable = false, unique = true)
    val email: String,

    @Column(name = "first_name", nullable = false)
    val firstName: String,

    @Column(name = "last_name", nullable = false)
    val lastName: String,

    @Column(name = "profile_picture")
    val profilePicture: String? = null,

    @Column(columnDefinition = "TEXT")
    val description: String? = null,

    @Column(columnDefinition = "TEXT")
    val links: String? = null,

    @Column(name = "created_at", updatable = false, insertable = false)
    val createdAt: Timestamp? = null,

    @Column(name = "updated_at", insertable = false)
    val updatedAt: Timestamp? = null
)
