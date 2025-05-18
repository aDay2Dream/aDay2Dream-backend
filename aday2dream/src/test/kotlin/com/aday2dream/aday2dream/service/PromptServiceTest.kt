package com.aday2dream.aday2dream.service

import com.aday2dream.aday2dream.dto.PromptDto
import com.aday2dream.aday2dream.entity.Account
import com.aday2dream.aday2dream.entity.Post
import com.aday2dream.aday2dream.entity.Prompt
import com.aday2dream.aday2dream.mapper.PromptMapper
import com.aday2dream.aday2dream.repository.AccountRepository
import com.aday2dream.aday2dream.repository.PostRepository
import com.aday2dream.aday2dream.repository.PromptRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
class PromptServiceTest {

    @Mock
    private lateinit var promptRepository: PromptRepository

    @Mock
    private lateinit var postRepository: PostRepository

    @Mock
    private lateinit var accountRepository: AccountRepository

    @Mock
    private lateinit var promptMapper: PromptMapper

    @InjectMocks
    private lateinit var promptService: PromptService

    @Test
    fun `should create prompt successfully`() {
        val promptDto = PromptDto(
            postId = 1L,
            buyerId = 1L,
            promptTitle = "Title",
            promptDescription = "Description",
            promptLinks = "link1"
        )
        val post = mock(Post::class.java)
        val buyer = mock(Account::class.java)
        val prompt = mock(Prompt::class.java)

        `when`(postRepository.findById(promptDto.postId)).thenReturn(Optional.of(post))
        `when`(accountRepository.findById(promptDto.buyerId)).thenReturn(Optional.of(buyer))
        `when`(promptMapper.toEntity(promptDto, post, buyer)).thenReturn(prompt)
        `when`(promptRepository.save(prompt)).thenReturn(prompt)

        val result = promptService.createPrompt(promptDto)

        assertEquals(prompt, result)
        verify(promptRepository).save(prompt)
    }

    @Test
    fun `should throw exception when post not found`() {
        val promptDto = PromptDto(
            postId = 1L,
            buyerId = 1L,
            promptTitle = "Title",
            promptDescription = "Description",
            promptLinks = "link1"
        )

        `when`(postRepository.findById(promptDto.postId)).thenReturn(Optional.empty())

        assertThrows<IllegalArgumentException> {
            promptService.createPrompt(promptDto)
        }
    }

    @Test
    fun `should retrieve prompt by id`() {
        val promptId = 1L
        val prompt = mock(Prompt::class.java)
        val promptDto = PromptDto(
            promptId = promptId,
            postId = 1L,
            buyerId = 1L,
            promptTitle = "Title",
            promptDescription = "Description",
            promptLinks = "link1"
        )

        `when`(promptRepository.findById(promptId)).thenReturn(Optional.of(prompt))
        `when`(promptMapper.toDto(prompt)).thenReturn(promptDto)

        val result = promptService.getPromptById(promptId)

        assertEquals(promptDto, result)
        verify(promptRepository).findById(promptId)
    }

    @Test
    fun `should throw exception when prompt not found`() {
        val promptId = 1L

        `when`(promptRepository.findById(promptId)).thenReturn(Optional.empty())

        assertThrows<IllegalArgumentException> {
            promptService.getPromptById(promptId)
        }
    }
}


