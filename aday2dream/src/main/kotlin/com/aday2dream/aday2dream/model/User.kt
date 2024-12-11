package com.aday2dream.aday2dream.model;
import jakarta.persistence.*
import lombok.Data
import lombok.NonNull
import java.time.LocalDateTime

@Data
@Entity
data class User(
    @Id
    @NonNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long,

    @Column(name="first_name", length = 50, nullable = false)
    var firstName: String,

    @Column(name="last_name", length = 50, nullable = false)
    var lastName: String,

    @Column(name="email", length = 50, nullable = false)
    var email: String,


    @Id
    @NonNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    var credentialsId: Long,


    @Column(name="created_at")
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name="updated_at")
    var updatedAt: LocalDateTime = LocalDateTime.now()
)
