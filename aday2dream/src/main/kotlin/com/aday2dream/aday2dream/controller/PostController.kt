package com.aday2dream.aday2dream.controller

import com.aday2dream.aday2dream.dto.PostDto
import com.aday2dream.aday2dream.service.PostService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import jakarta.validation.Valid
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@RestController
@RequestMapping("/posts")
@Validated
class PostController(
    private val postService: PostService
) {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(AccountController::class.java)
    }

    @PostMapping
    fun createPost(@Valid @RequestBody postDto: PostDto): ResponseEntity<PostDto> {
        val createdPost = postService.createPost(postDto)
        logger.info("Post created successfully.")
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost)
    }

    @GetMapping("/{id}")
    fun getPostById(@PathVariable id: Long): ResponseEntity<PostDto> {
        val post = postService.getPostById(id)
            ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)

        return ResponseEntity.ok(post)
    }

    @GetMapping
    fun getAllPosts(): ResponseEntity<List<PostDto>> {
        val posts = postService.getAllPosts()
        return if (posts.isEmpty()) {
            logger.warn("There are no posts.")
            ResponseEntity.noContent().build()
        } else {
            logger.info("Posts retrieved.")
            ResponseEntity.ok(posts)
        }
    }

    @GetMapping("/publisher/{publisherId}")
    fun getPostsByPublisher(@PathVariable publisherId: Long): ResponseEntity<List<PostDto>> {
        val posts = postService.getPostsByPublisherId(publisherId)
        return if (posts.isEmpty()) {
            logger.warn("There are no posts made by the publisher.")
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        } else {
            logger.info("Posts by publisher retrieved.")
            ResponseEntity.ok(posts)
        }
    }

    @PutMapping("/{id}")
    fun updatePost(
        @PathVariable id: Long,
        @Valid @RequestBody updatedPostDto: PostDto
    ): ResponseEntity<PostDto> {
        val post = postService.updatePost(id, updatedPostDto)
            ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
        return ResponseEntity.ok(post)
    }

    @DeleteMapping("/{id}")
    fun deletePost(@PathVariable id: Long): ResponseEntity<Map<String, String>> {
        postService.deletePostById(id)
        logger.info("Post with ID $id was deleted successfully.")
        return ResponseEntity.ok(mapOf("message" to "Post with ID $id was deleted successfully."))
    }
}
