package com.aday2dream.aday2dream.model

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import java.time.LocalDateTime

@Entity
@Table(name = "audiofile")
data class AudioFile(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val audiofileId: Long = 0,

    @field:NotBlank("No Uri provided")
    @Column(name = "uri", nullable = false)
    val uri: String,

    @field:NotBlank("Title not found")
    @Column(name = "title",nullable = false)
    val title: String,

    @field:NotBlank("Duration not found")
    @Column(name = "duration",nullable = false)
    val duration: Int,
)
