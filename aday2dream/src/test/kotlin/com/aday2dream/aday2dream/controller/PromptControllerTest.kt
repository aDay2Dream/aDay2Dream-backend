package com.aday2dream.aday2dream.controller

import com.aday2dream.aday2dream.dto.PromptDto
import com.aday2dream.aday2dream.entity.Account
import com.aday2dream.aday2dream.entity.Post
import com.aday2dream.aday2dream.entity.Prompt
import com.aday2dream.aday2dream.service.PromptService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
class PromptControllerTest {

    @Mock
    private lateinit var promptService: PromptService

    @Mock
    private lateinit var post: Post

    @Mock
    private lateinit var buyer: Account

    @InjectMocks
    private lateinit var promptController: PromptController

    @BeforeEach
    fun setUp() {
        promptController = PromptController(promptService)
    }

    @Test
    fun `should create prompt successfully`() {
        val promptDto = PromptDto(
            promptId = 1L, postId = 1L, buyerId = 2L, promptTitle = "Test Prompt", promptDescription = "Description",
            promptLinks = "http://example.com", startDate = LocalDateTime.now(), endDate = LocalDateTime.now().plusDays(1),
            createdAt = LocalDateTime.now(), updatedAt = LocalDateTime.now()
        )
        val prompt = Prompt(
            promptId = 1L, post = post, buyer = buyer, promptTitle = "Test Prompt", promptDescription = "Description",
            promptLinks = "http://example.com", startDate = LocalDateTime.now(), endDate = LocalDateTime.now().plusDays(1),
            createdAt = LocalDateTime.now(), updatedAt = LocalDateTime.now()
        )

        `when`(promptService.createPrompt(promptDto)).thenReturn(prompt)

        val response: ResponseEntity<Prompt> = promptController.createPrompt(promptDto)

        assert(response.statusCode == HttpStatus.OK)
        assert(response.body == prompt)
    }

    @Test
    fun `should retrieve prompt by ID`() {
        val promptDto = PromptDto(
            promptId = 1L, postId = 1L, buyerId = 2L, promptTitle = "Test Prompt", promptDescription = "Description",
            promptLinks = "http://example.com", startDate = LocalDateTime.now(), endDate = LocalDateTime.now().plusDays(1),
            createdAt = LocalDateTime.now(), updatedAt = LocalDateTime.now()
        )

        `when`(promptService.getPromptById(1L)).thenReturn(promptDto)

        val response: ResponseEntity<PromptDto> = promptController.getPromptById(1L)

        assert(response.statusCode == HttpStatus.OK)
        assert(response.body == promptDto)
    }

    @Test
    fun `should retrieve prompts by account ID`() {
        val promptList = listOf(
            PromptDto(1L, 1L, 2L, "Prompt1", "Description1", "http://example.com/1", LocalDateTime.now(), null, LocalDateTime.now(), LocalDateTime.now()),
            PromptDto(2L, 1L, 2L, "Prompt2", "Description2", "http://example.com/2", LocalDateTime.now(), null, LocalDateTime.now(), LocalDateTime.now())
        )

        `when`(promptService.getPromptsByAccountId(2L)).thenReturn(promptList)

        val response: ResponseEntity<List<PromptDto>> = promptController.getPromptsByAccountId(2L)

        assert(response.statusCode == HttpStatus.OK)
        assert(response.body?.size == 2)
    }

    @Test
    fun `should retrieve all prompts`() {
        val promptList = listOf(
            PromptDto(1L, 1L, 2L, "Prompt1", "Description1", "http://example.com/1", LocalDateTime.now(), null, LocalDateTime.now(), LocalDateTime.now()),
            PromptDto(2L, 1L, 2L, "Prompt2", "Description2", "http://example.com/2", LocalDateTime.now(), null, LocalDateTime.now(), LocalDateTime.now())
        )

        `when`(promptService.getAllPrompts()).thenReturn(promptList)

        val response: ResponseEntity<List<PromptDto>> = promptController.getAllPrompts()

        assert(response.statusCode == HttpStatus.OK)
        assert(response.body?.size == 2)
    }

    @Test
    fun `should retrieve prompts by post ID`() {
        val promptList = listOf(
            PromptDto(1L, 1L, 2L, "Prompt1", "Description1", "http://example.com/1", LocalDateTime.now(), null, LocalDateTime.now(), LocalDateTime.now()),
            PromptDto(2L, 1L, 2L, "Prompt2", "Description2", "http://example.com/2", LocalDateTime.now(), null, LocalDateTime.now(), LocalDateTime.now())
        )

        `when`(promptService.getPromptsByPostId(1L)).thenReturn(promptList)

        val response: ResponseEntity<List<PromptDto>> = promptController.getPromptsByPostId(1L)

        assert(response.statusCode == HttpStatus.OK)
        assert(response.body?.size == 2)
    }

    @Test
    fun `should update prompt successfully`() {
        val updatedPromptDto = PromptDto(
            promptId = 1L, postId = 1L, buyerId = 2L, promptTitle = "Updated Prompt", promptDescription = "Updated Description",
            promptLinks = "http://updated.com", startDate = LocalDateTime.now(), endDate = LocalDateTime.now().plusDays(1),
            createdAt = LocalDateTime.now(), updatedAt = LocalDateTime.now()
        )

        `when`(promptService.updatePrompt(1L, updatedPromptDto)).thenReturn(updatedPromptDto)

        val response: ResponseEntity<PromptDto> = promptController.updatePrompt(1L, updatedPromptDto)

        assert(response.statusCode == HttpStatus.OK)
        assert(response.body == updatedPromptDto)
    }

    @Test
    fun `should delete prompt successfully`() {
        doNothing().`when`(promptService).deletePrompt(1L)

        val response: ResponseEntity<Void> = promptController.deletePrompt(1L)

        assert(response.statusCode == HttpStatus.NO_CONTENT)
    }
}
