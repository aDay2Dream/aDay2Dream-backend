package com.aday2dream.aday2dream.service

import com.aday2dream.aday2dream.entity.Post
import com.aday2dream.aday2dream.repository.PostRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional
class PostService(
    private val postRepository: PostRepository
) {
    fun createPost(post: Post): Post {
        return postRepository.save(post)
    }

    fun getPostById(id: Long): Post? {
        return postRepository.findById(id).orElse(null)
    }

    fun getAllPosts(): List<Post> {
        return postRepository.findAll()
    }

    fun getPostsByPublisherId(accountId: Long): List<Post> {
        return postRepository.findByAccountId(accountId = accountId)
    }

    fun updatePost(postId: Long, updatedPost: Post): Post? {
        val existingPost = getPostById(postId) ?: return null
        return postRepository.save(
            existingPost.copy(
                title = updatedPost.title,
                description = updatedPost.description,
                backgroundImage = updatedPost.backgroundImage,
                hyperlinks = updatedPost.hyperlinks,
                price = updatedPost.price,
                updatedAt = LocalDateTime.now()
            )
        )
    }

    fun deletePostById(postId: Long) {
        postRepository.deleteById(postId)
    }
}