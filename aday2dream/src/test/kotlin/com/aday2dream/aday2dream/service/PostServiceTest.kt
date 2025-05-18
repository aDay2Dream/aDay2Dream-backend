package com.aday2dream.aday2dream.service

import com.aday2dream.aday2dream.dto.PostDto
import com.aday2dream.aday2dream.entity.Post
import com.aday2dream.aday2dream.entity.Account
import com.aday2dream.aday2dream.entity.AudioFile
import com.aday2dream.aday2dream.mapper.PostMapper
import com.aday2dream.aday2dream.repository.AccountRepository
import com.aday2dream.aday2dream.repository.AudioFileRepository
import com.aday2dream.aday2dream.repository.PostRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

@ExtendWith(MockitoExtension::class)
class PostServiceTest {

    private lateinit var postRepository: PostRepository
    private lateinit var accountRepository: AccountRepository
    private lateinit var audioFileRepository: AudioFileRepository
    private lateinit var postMapper: PostMapper
    private lateinit var postService: PostService

    @BeforeEach
    fun setUp() {
        postRepository = mock(PostRepository::class.java)
        accountRepository = mock(AccountRepository::class.java)
        audioFileRepository = mock(AudioFileRepository::class.java)
        postMapper = mock(PostMapper::class.java)
        postService = PostService(postRepository, accountRepository, audioFileRepository, postMapper)
    }

    @Test
    fun `should create post successfully`() {
        val postDto = PostDto(
            postId = 1L,
            accountId = 1L,
            audiofileId = 1L,
            title = "Title",
            description = "Description",
            backgroundImage = "image.png",
            hyperlinks = "https://example.com",
            price = BigDecimal.TEN
        )

        val account = Account(
            accountId = 1L,
            username = "testuser",
            password = "securepassword",
            email = "test@example.com",
            firstName = "Test",
            lastName = "User"
        )

        val audioFile = AudioFile(
            audiofileId = 1L,
            uri = "audio.mp3",
            title = "Audio Title",
            duration = 120
        )

        val post = Post(
            postId = 1L,
            account = account,
            audiofile = audioFile,
            title = postDto.title,
            description = postDto.description,
            backgroundImage = postDto.backgroundImage,
            hyperlinks = postDto.hyperlinks,
            price = postDto.price,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        `when`(accountRepository.findById(postDto.accountId)).thenReturn(Optional.of(account))
        `when`(audioFileRepository.findById(postDto.audiofileId)).thenReturn(Optional.of(audioFile))
        `when`(postMapper.toEntity(postDto, account, audioFile)).thenReturn(post)
        `when`(postRepository.save(post)).thenReturn(post)
        `when`(postMapper.toDto(post)).thenReturn(postDto)

        val result = postService.createPost(postDto)

        assertNotNull(result)
        assertEquals(postDto.title, result.title)
        verify(postRepository).save(post)
    }

    @Test
    fun `should retrieve post by ID`() {
        val account = Account(
            accountId = 1L,
            username = "testuser",
            password = "securepassword",
            email = "test@example.com",
            firstName = "Test",
            lastName = "User"
        )

        val audioFile = AudioFile(
            audiofileId = 1L,
            uri = "audio.mp3",
            title = "Audio Title",
            duration = 120
        )

        val post = Post(
            postId = 1L,
            account = account,
            audiofile = audioFile,
            title = "Title",
            description = "Description",
            backgroundImage = "image.png",
            hyperlinks = "https://example.com",
            price = BigDecimal.TEN,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        val postDto = PostDto(
            postId = 1L,
            accountId = account.accountId!!,
            audiofileId = audioFile.audiofileId,
            title = "Title",
            description = "Description",
            backgroundImage = "image.png",
            hyperlinks = "https://example.com",
            price = BigDecimal.TEN
        )

        `when`(postRepository.findById(1L)).thenReturn(Optional.of(post))
        `when`(postMapper.toDto(post)).thenReturn(postDto)

        val result = postService.getPostById(1L)

        assertNotNull(result)
        assertEquals("Title", result?.title)
        verify(postRepository).findById(1L)
    }

    @Test
    fun `should return null when retrieving non-existent post`() {
        `when`(postRepository.findById(1L)).thenReturn(Optional.empty())

        val result = postService.getPostById(1L)

        assertNull(result)
        verify(postRepository).findById(1L)
    }

    @Test
    fun `should delete post by ID`() {
        doNothing().`when`(postRepository).deleteById(1L)

        postService.deletePostById(1L)

        verify(postRepository).deleteById(1L)
    }

}