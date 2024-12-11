package com.aday2dream.aday2dream.model

import jakarta.persistence.*
import lombok.Data
import lombok.NonNull

@Data
@Entity
@Table(name = "credentials")
data class Credentials(

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long? = null,

    @Column(length = 255, nullable = false)
    val username: String,

    @Column(nullable = false, length = 255)
    val password: String
)
