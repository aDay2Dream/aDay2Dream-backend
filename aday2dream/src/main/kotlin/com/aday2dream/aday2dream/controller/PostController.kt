package com.aday2dream.aday2dream.controller

import com.aday2dream.aday2dream.model.Post
import com.aday2dream.aday2dream.service.PostService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import jakarta.validation.Valid


@RestController
@RequestMapping("/posts")
@Validated
class PostController(
    private val postService: PostService
) {
    @PostMapping
    fun createPost(@Valid @RequestBody post: Post): ResponseEntity<Post> {
        val createdPost = postService.createPost(post)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost)
    }

    @GetMapping("/{id}")
    fun getPostById(@PathVariable id: Long): ResponseEntity<Post> {
        val post = postService.getPostById(id)
            ?: return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(null)
        return ResponseEntity.ok(post)
    }

    @GetMapping
    fun getAllPosts(): ResponseEntity<List<Post>> {
        val posts = postService.getAllPosts()
        return if (posts.isEmpty()) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.ok(posts)
        }
    }

    @GetMapping("/publisher/{publisherId}")
    fun getPostsByPublisher(@PathVariable publisherId: Long): ResponseEntity<List<Post>> {
        val posts = postService.getPostsByPublisherId(publisherId)
        return if (posts.isEmpty()) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        } else {
            ResponseEntity.ok(posts)
        }
    }

    @PutMapping("/{id}")
    fun updatePost(
        @PathVariable id: Long,
        @Valid @RequestBody updatedPost: Post
    ): ResponseEntity<Post> {
        val post = postService.updatePost(id, updatedPost)
            ?: return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(null)
        return ResponseEntity.ok(post)
    }

    @DeleteMapping("/{id}")
    fun deletePost(@PathVariable id: Long): ResponseEntity<Map<String, String>> {
        postService.deletePostById(id)
        return ResponseEntity.ok(mapOf("message" to "Post with ID $id was deleted successfully."))
    }
}