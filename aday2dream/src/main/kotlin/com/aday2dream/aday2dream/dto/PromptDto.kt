package com.aday2dream.aday2dream.dto

import java.time.LocalDateTime

data class PromptDto(
    val promptId: Long? = null,
    val postId: Long,
    val buyerId: Long,
    val promptTitle: String,
    val promptDescription: String,
    val promptLinks: String? = null,
    val startDate: LocalDateTime = LocalDateTime.now(),
    val endDate: LocalDateTime? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now()
)