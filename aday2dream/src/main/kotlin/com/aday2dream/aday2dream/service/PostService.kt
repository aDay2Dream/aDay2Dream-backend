package com.aday2dream.aday2dream.service

import com.aday2dream.aday2dream.dto.PostDto
import com.aday2dream.aday2dream.entity.Post
import com.aday2dream.aday2dream.mapper.PostMapper
import com.aday2dream.aday2dream.repository.AccountRepository
import com.aday2dream.aday2dream.repository.AudioFileRepository
import com.aday2dream.aday2dream.repository.PostRepository
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
    fun createPost(post: PostDto): PostDto {
        val account = accountRepository.findById(post.accountId).orElseThrow { throw IllegalArgumentException("Account with ID ${post.accountId} not found") }
        val audiofile = audioFileRepository.findById(post.audiofileId) .orElseThrow { throw IllegalArgumentException("Audio file with ID ${post.audiofileId} not found") }

        return postMapper.toDto(postRepository.save(postMapper.toEntity(post, account, audiofile)))
    }

    fun getPostById(id: Long): PostDto? {
        return postRepository.findById(id).map(postMapper::toDto).orElse(null)
    }

    fun getAllPosts(): List<PostDto> {
        return postRepository.findAll().map(postMapper::toDto)
    }

    fun getPostsByPublisherId(accountId: Long): List<PostDto> {
        return postRepository.findByAccountId(accountId).map(postMapper::toDto)
    }

    fun updatePost(postId: Long, updatedPostDto: PostDto): PostDto? {
        val existingPost = postRepository.findById(postId).orElse(null) ?: return null
        val updatedPost = existingPost.copy(
            title = updatedPostDto.title,
            description = updatedPostDto.description,
            backgroundImage = updatedPostDto.backgroundImage,
            hyperlinks = updatedPostDto.hyperlinks,
            price = updatedPostDto.price,
            updatedAt = LocalDateTime.now()
        )
        return postMapper.toDto(postRepository.save(updatedPost))
    }

    fun deletePostById(postId: Long) {
        postRepository.deleteById(postId)
    }
}