package com.aday2dream.aday2dream.controller

import com.aday2dream.aday2dream.dto.PromptDto
import com.aday2dream.aday2dream.entity.Prompt
import com.aday2dream.aday2dream.service.PromptService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/prompts")
class PromptController(
    private val promptService: PromptService
) {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(AccountController::class.java)
    }

    @PostMapping
    fun createPrompt(@RequestBody promptDto: PromptDto): ResponseEntity<Prompt> {
        val createdPrompt = promptService.createPrompt(promptDto)
        logger.info("Prompt created: ${createdPrompt.toString()}")
        return ResponseEntity.ok(createdPrompt)
    }

    @GetMapping("/{id}")
    fun getPromptById(@PathVariable id: Long): ResponseEntity<PromptDto> {
        val prompt = promptService.getPromptById(id)
        logger.info("Prompt retrieved: ${prompt.toString()}")
        return ResponseEntity.ok(prompt)
    }

    @GetMapping("/account/{accountId}")
    fun getPromptsByAccountId(@PathVariable accountId: Long) : ResponseEntity<List<PromptDto>>{
        val prompts = promptService.getPromptsByAccountId(accountId)
        logger.info("Prompts by accountId retrieved: ${prompts.toString()}")
        return ResponseEntity.ok(prompts)
    }

    @GetMapping
    fun getAllPrompts(): ResponseEntity<List<PromptDto>> {
        val prompts = promptService.getAllPrompts()
        logger.info("All prompts: ${prompts.toString()}")
        return ResponseEntity.ok(prompts)
    }

    @GetMapping("/post/{postId}")
    fun getPromptsByPostId(@PathVariable postId: Long) : ResponseEntity<List<PromptDto>>
    {
        val prompts = promptService.getPromptsByPostId(postId)
        logger.info("Prompts by postId: ${prompts.toString()}")
        return ResponseEntity.ok(prompts)
    }

    @PutMapping("/{id}")
    fun updatePrompt(@PathVariable id: Long, @RequestBody updatedPromptDto: PromptDto): ResponseEntity<PromptDto> {
        val updatedPrompt = promptService.updatePrompt(id, updatedPromptDto)
        logger.info("Updated prompt: ${updatedPrompt.toString()}")
        return ResponseEntity.ok(updatedPrompt)
    }

    @DeleteMapping("/{id}")
    fun deletePrompt(@PathVariable id: Long): ResponseEntity<Void> {
        promptService.deletePrompt(id)
        logger.info("Prompt deleted.")
        return ResponseEntity.noContent().build()
    }
}
