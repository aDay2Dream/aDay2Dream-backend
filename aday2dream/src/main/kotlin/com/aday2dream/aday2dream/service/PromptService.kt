package com.aday2dream.aday2dream.service

import com.aday2dream.aday2dream.dto.PromptDto
import com.aday2dream.aday2dream.entity.Prompt
import com.aday2dream.aday2dream.mapper.PromptMapper
import com.aday2dream.aday2dream.repository.AccountRepository
import com.aday2dream.aday2dream.repository.PostRepository
import com.aday2dream.aday2dream.repository.PromptRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class PromptService(
    private val promptRepository: PromptRepository,
    private val postRepository: PostRepository,
    private val accountRepository: AccountRepository,
    private val promptMapper: PromptMapper
) {
        fun createPrompt(promptDto: PromptDto): Prompt {
            val post = postRepository.findById(promptDto.postId)
                .orElseThrow { IllegalArgumentException("Post not found with id: ${promptDto.postId}") }

            val buyer = accountRepository.findById(promptDto.buyerId)
                .orElseThrow { IllegalArgumentException("User not found with id: ${promptDto.buyerId}") }

            val prompt = promptMapper.toEntity(promptDto, post, buyer)
            println(prompt)
            val savedPrompt = promptRepository.save(prompt)
            return savedPrompt
        }

        fun getPromptById(promptId: Long): PromptDto {
            val prompt = promptRepository.findById(promptId)
                .orElseThrow { IllegalArgumentException("Prompt not found with id: $promptId") }
            return promptMapper.toDto(prompt)
        }

        fun getPromptsByAccountId(accountId: Long): List<PromptDto>
        {
            val prompts = promptRepository.findByBuyerAccountId(accountId)

            return promptMapper.toDtoList(prompts)
        }

        fun getPromptsByPostId(postId: Long) : List<PromptDto>
        {
            val prompts = promptRepository.findByPostPostId(postId)
            return promptMapper.toDtoList(prompts)
        }
    
        fun getAllPrompts(): List<PromptDto> {
            return promptRepository.findAll().map { promptMapper.toDto(it) }
        }

        fun updatePrompt(promptId: Long, updatedPromptDto: PromptDto): PromptDto {
            val prompt = promptRepository.findById(promptId)
                .orElseThrow { IllegalArgumentException("Prompt not found with id: $promptId") }

            val post = postRepository.findById(updatedPromptDto.postId)
                .orElseThrow { IllegalArgumentException("Post not found with id: ${updatedPromptDto.postId}") }

            val buyer = accountRepository.findById(updatedPromptDto.buyerId)
                .orElseThrow { IllegalArgumentException("User not found with id: ${updatedPromptDto.buyerId}") }

            val updatedPrompt = prompt.copy(
                post = post,
                buyer = buyer,
                promptTitle = updatedPromptDto.promptTitle,
                promptDescription = updatedPromptDto.promptDescription,
                promptLinks = updatedPromptDto.promptLinks,
                startDate = updatedPromptDto.startDate ?: LocalDateTime.now(),
                endDate = updatedPromptDto.endDate,
                updatedAt = LocalDateTime.now()
            )
            val savedPrompt = promptRepository.save(updatedPrompt)
            return promptMapper.toDto(savedPrompt)
        }

        fun deletePrompt(promptId: Long) {
            if (!promptRepository.existsById(promptId)) {
                throw IllegalArgumentException("Prompt not found with id: $promptId")
            }
            promptRepository.deleteById(promptId)
        }
    }

