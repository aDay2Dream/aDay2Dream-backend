package com.aday2dream.aday2dream.entity

import java.sql.Timestamp
import jakarta.persistence.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import lombok.*


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "account")
data class Account(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    val accountId: Long? = null,

    @field:NotBlank(message = "Username is required.")
    @field:Size(max = 50, message = "Username must not exceed 50 characters.")
    @Column(name="username", nullable = false, unique = true)
    val username: String,


    @Column(name="password", nullable = false)
    var password: String,

    @Email
    @field:NotBlank(message = "Email is required.")
    @Column(name = "email", nullable = false, unique = true)
    val email: String,

    @field:NotBlank(message = "First name is required.")
    @Column(name = "first_name", nullable = false)
    val firstName: String,

    @field:NotBlank(message = "Last name is required.")
    @Column(name = "last_name", nullable = false)
    val lastName: String,

    @field:Size(max = 255, message = "Profile picture URL must not exceed 255 characters.")
    @Column(name = "profile_picture")
    val profilePicture: String? = null,

    @field:Size(max = 1000, message = "Description must not exceed 1000 characters.")
    @Column(columnDefinition = "TEXT")
    val description: String? = null,

    @field:Size(max = 500, message = "Links must not exceed 500 characters.")
    @Column(columnDefinition = "TEXT")
    val links: String? = null,

    @Column(name = "created_at", updatable = false, insertable = false)
    val createdAt: Timestamp? = null,

    @Column(name = "updated_at", insertable = false)
    val updatedAt: Timestamp? = null
)
