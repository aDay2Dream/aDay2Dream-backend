package com.aday2dream.aday2dream.service

import com.aday2dream.aday2dream.controller.AccountController
import com.aday2dream.aday2dream.dto.PromptDto
import com.aday2dream.aday2dream.entity.Prompt
import com.aday2dream.aday2dream.mapper.PromptMapper
import com.aday2dream.aday2dream.repository.AccountRepository
import com.aday2dream.aday2dream.repository.PostRepository
import com.aday2dream.aday2dream.repository.PromptRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class PromptService(
    private val promptRepository: PromptRepository,
    private val postRepository: PostRepository,
    private val accountRepository: AccountRepository,
    private val promptMapper: PromptMapper
) {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(AccountController::class.java)
    }

    fun createPrompt(promptDto: PromptDto): Prompt {
            val post = postRepository.findById(promptDto.postId)
                .orElseThrow { IllegalArgumentException("Post not found with id: ${promptDto.postId}") }

            val buyer = accountRepository.findById(promptDto.buyerId)
                .orElseThrow { IllegalArgumentException("User not found with id: ${promptDto.buyerId}") }
            logger.info("Creating prompt for post: $post for account: $buyer")
            val prompt = promptMapper.toEntity(promptDto, post, buyer)
            logger.info("Prompt created: $prompt")
            val savedPrompt = promptRepository.save(prompt)
            logger.info("Prompt saved.")
            return savedPrompt
        }

        fun getPromptById(promptId: Long): PromptDto {
            logger.info("Retrieving prompt...")
            val prompt = promptRepository.findById(promptId)
                .orElseThrow { IllegalArgumentException("Prompt not found with id: $promptId") }
            logger.info("Retrieved prompt: $prompt")
            return promptMapper.toDto(prompt)
        }

        fun getPromptsByAccountId(accountId: Long): List<PromptDto>
        {
            logger.info("Retrieving prompts by accountId: $accountId")
            val prompts = promptRepository.findByBuyerAccountId(accountId)
            logger.info("Prompts: ${prompts}")
            return promptMapper.toDtoList(prompts)
        }

        fun getPromptsByPostId(postId: Long) : List<PromptDto>
        {
            logger.info("Retrieving prompts by postId: $postId")
            val prompts = promptRepository.findByPostPostId(postId)
            logger.info("Prompts: ${prompts}")
            return promptMapper.toDtoList(prompts)
        }
    
        fun getAllPrompts(): List<PromptDto> {
            logger.info("Retrieving all prompts...")
            return promptRepository.findAll().map { promptMapper.toDto(it) }
        }

        fun updatePrompt(promptId: Long, updatedPromptDto: PromptDto): PromptDto {
            val prompt = promptRepository.findById(promptId)
                .orElseThrow { IllegalArgumentException("Prompt not found with id: $promptId") }
            logger.info("Updating prompt: $prompt")
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
                startDate = updatedPromptDto.startDate,
                endDate = updatedPromptDto.endDate,
                updatedAt = LocalDateTime.now()
            )
            logger.info("Updated prompt: $prompt")
            val savedPrompt = promptRepository.save(updatedPrompt)
            logger.info("Prompt saved!")
            return promptMapper.toDto(savedPrompt)
        }

        fun deletePrompt(promptId: Long) {
            logger.info("Deleting prompt by id...")
            if (!promptRepository.existsById(promptId)) {
                logger.error("Prompt not found with id: $promptId")
                throw IllegalArgumentException("Prompt not found with id: $promptId")
            }
            promptRepository.deleteById(promptId)
            logger.info("Prompt deleted.")
        }
    }

