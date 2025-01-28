package com.aday2dream.aday2dream.entity

import jakarta.persistence.*
import lombok.AllArgsConstructor
import lombok.NoArgsConstructor

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "audiofile")
data class AudioFile(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val audiofileId: Long = 0,

    @Column(name = "uri", nullable = false)
    val uri: String,

    @Column(name = "title",nullable = false)
    val title: String,

    @Column(name = "duration",nullable = false)
    val duration: Int,
)