package com.aday2dream.aday2dream.dto

data class AudioFileDTO(
    val audiofileId: Long,
    val title: String,
    val uri: String,
    val duration: Int,
    val uploadedAt: String
)