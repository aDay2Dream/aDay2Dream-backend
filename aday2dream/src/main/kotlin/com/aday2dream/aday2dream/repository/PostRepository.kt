package com.aday2dream.aday2dream.repository
import com.aday2dream.aday2dream.entity.Post
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface PostRepository : JpaRepository<Post, Long> {
        @Query("SELECT post FROM Post post WHERE post.account.accountId = :accountId")
        fun findByAccountId(@Param("accountId") accountId: Long): List<Post>
}