package com.aday2dream.aday2dream.service

import com.aday2dream.aday2dream.controller.AccountController
import com.aday2dream.aday2dream.dto.PostDto
import com.aday2dream.aday2dream.entity.Post
import com.aday2dream.aday2dream.mapper.PostMapper
import com.aday2dream.aday2dream.repository.AccountRepository
import com.aday2dream.aday2dream.repository.AudioFileRepository
import com.aday2dream.aday2dream.repository.PostRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional
class PostService(
    private val postRepository: PostRepository,
    private val accountRepository: AccountRepository,
    private val audioFileRepository: AudioFileRepository,
    private val postMapper: PostMapper
) {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(AccountController::class.java)
    }

    fun createPost(post: PostDto): PostDto {
        logger.info("Creating post: $post")
        val account = accountRepository.findById(post.accountId).orElseThrow { throw IllegalArgumentException("Account with ID ${post.accountId} not found") }
        val audiofile = audioFileRepository.findById(post.audiofileId) .orElseThrow { throw IllegalArgumentException("Audio file with ID ${post.audiofileId} not found") }

        return postMapper.toDto(postRepository.save(postMapper.toEntity(post, account, audiofile)))
    }

    fun getPostById(id: Long): PostDto? {
        logger.info("Retrieving post by id...")
        return postRepository.findById(id).map(postMapper::toDto).orElse(null)
    }

    fun getAllPosts(): List<PostDto> {
        logger.info("Retrieving all posts...")
        return postRepository.findAll().map(postMapper::toDto)
    }

    fun getPostsByPublisherId(accountId: Long): List<PostDto> {
        logger.info("Retrieving posts by publisher...")
        return postRepository.findByAccountId(accountId).map(postMapper::toDto)
    }

    fun updatePost(postId: Long, updatedPostDto: PostDto): PostDto? {
        logger.info("Updating post...")
        val existingPost = postRepository.findById(postId).orElse(null) ?: return null
        val updatedPost = existingPost.copy(
            title = updatedPostDto.title,
            description = updatedPostDto.description,
            backgroundImage = updatedPostDto.backgroundImage,
            hyperlinks = updatedPostDto.hyperlinks,
            price = updatedPostDto.price,
            updatedAt = LocalDateTime.now()
        )
        logger.info("Updated post: ${updatedPost.toString()} ")
        return postMapper.toDto(postRepository.save(updatedPost))
    }

    fun deletePostById(postId: Long) {
        logger.info("Deleting post by id: $postId")
        postRepository.deleteById(postId)
    }
}