package com.aday2dream.aday2dream.repository

import com.aday2dream.aday2dream.entity.Prompt
import org.springframework.data.jpa.repository.JpaRepository

interface PromptRepository : JpaRepository<Prompt, Long> {
    fun findByBuyerAccountId(accountId: Long): List<Prompt>
    fun findByPostPostId(postId: Long): List<Prompt>
}