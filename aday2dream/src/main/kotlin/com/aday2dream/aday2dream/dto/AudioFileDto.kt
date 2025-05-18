package com.aday2dream.aday2dream.dto

data class AudioFileDto(
    val audiofileId: Long,
    val uri: String,
    val title: String,
    val duration: Int,
)