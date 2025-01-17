package com.aday2dream.aday2dream.controller

import com.aday2dream.aday2dream.model.Post
import com.aday2dream.aday2dream.service.PostService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/posts")
class PostController(
    private val postService: PostService
) {
    @PostMapping
    fun createPost(@RequestBody post: Post): ResponseEntity<Post> {
        val createdPost = postService.createPost(post)
        return ResponseEntity.ok(createdPost)
    }

    @GetMapping("/{id}")
    fun getPostById(@PathVariable id: Long): ResponseEntity<Post> {
        val post = postService.getPostById(id)
            ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(post)
    }

    @GetMapping
    fun getAllPosts(): ResponseEntity<List<Post>> {
        val posts = postService.getAllPosts()
        return ResponseEntity.ok(posts)
    }

    @GetMapping("/publisher/{publisherId}")
    fun getPostsByPublisher(@PathVariable publisherId: Long): ResponseEntity<List<Post>> {
        val posts = postService.getPostsByPublisherId(publisherId)
        return ResponseEntity.ok(posts)
    }

    @PutMapping("/{id}")
    fun updatePost(
        @PathVariable id: Long,
        @RequestBody updatedPost: Post
    ): ResponseEntity<Post> {
        val post = postService.updatePost(id, updatedPost)
            ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(post)
    }

    @DeleteMapping("/{id}")
    fun deletePost(@PathVariable id: Long): ResponseEntity<Void> {
        postService.deletePostById(id)
        return ResponseEntity.noContent().build()
    }
}
