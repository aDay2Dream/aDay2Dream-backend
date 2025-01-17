package com.aday2dream.aday2dream.repository
import com.aday2dream.aday2dream.model.Post
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PostRepository : JpaRepository<Post, Long> {
    fun findByAccountAccountId(accountId: Long): List<Post>

}