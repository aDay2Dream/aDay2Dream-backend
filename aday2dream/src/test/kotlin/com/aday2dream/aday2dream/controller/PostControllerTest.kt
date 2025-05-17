package com.aday2dream.aday2dream.controller

import com.aday2dream.aday2dream.dto.PostDto
import com.aday2dream.aday2dream.service.PostService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

@ExtendWith(MockitoExtension::class)
class PostControllerTest {

    @Mock
    private lateinit var postService: PostService

    @InjectMocks
    private lateinit var postController: PostController

    @BeforeEach
    fun setUp() {
        postController = PostController(postService)
    }

    @Test
    fun `should create post successfully`() {
        val postDto = PostDto(1L, 1L, 1L, "Title", "Description", "image.png", "https://example.com", 10.toBigDecimal())

        `when`(postService.createPost(postDto)).thenReturn(postDto)

        val response: ResponseEntity<PostDto> = postController.createPost(postDto)

        assert(response.statusCode == HttpStatus.CREATED)
        assert(response.body == postDto)
    }

    @Test
    fun `should retrieve post by ID`() {
        val postDto = PostDto(1L, 1L, 1L, "Title", "Description", "image.png", "https://example.com", 10.toBigDecimal())

        `when`(postService.getPostById(1L)).thenReturn(postDto)

        val response: ResponseEntity<PostDto> = postController.getPostById(1L)

        assert(response.statusCode == HttpStatus.OK)
        assert(response.body == postDto)
    }

    @Test
    fun `should return not found when post does not exist`() {
        `when`(postService.getPostById(99L)).thenReturn(null)

        val response: ResponseEntity<PostDto> = postController.getPostById(99L)

        assert(response.statusCode == HttpStatus.NOT_FOUND)
        assert(response.body == null)
    }

    @Test
    fun `should retrieve all posts`() {
        val posts = listOf(
            PostDto(1L, 1L, 1L, "Title1", "Description1", "image1.png", "https://example.com/1", 10.toBigDecimal()),
            PostDto(2L, 1L, 1L, "Title2", "Description2", "image2.png", "https://example.com/2", 20.toBigDecimal())
        )

        `when`(postService.getAllPosts()).thenReturn(posts)

        val response: ResponseEntity<List<PostDto>> = postController.getAllPosts()

        assert(response.statusCode == HttpStatus.OK)
        assert(response.body?.size == 2)
    }

    @Test
    fun `should return no content when there are no posts`() {
        `when`(postService.getAllPosts()).thenReturn(emptyList())

        val response: ResponseEntity<List<PostDto>> = postController.getAllPosts()

        assert(response.statusCode == HttpStatus.NO_CONTENT)
    }

    @Test
    fun `should retrieve posts by publisher`() {
        val posts = listOf(
            PostDto(1L, 2L, 1L, "Title1", "Description1", "image1.png", "https://example.com/1", 10.toBigDecimal())
        )

        `when`(postService.getPostsByPublisherId(2L)).thenReturn(posts)

        val response: ResponseEntity<List<PostDto>> = postController.getPostsByPublisher(2L)

        assert(response.statusCode == HttpStatus.OK)
        assert(response.body?.size == 1)
    }

    @Test
    fun `should return not found when publisher has no posts`() {
        `when`(postService.getPostsByPublisherId(99L)).thenReturn(emptyList())

        val response: ResponseEntity<List<PostDto>> = postController.getPostsByPublisher(99L)

        assert(response.statusCode == HttpStatus.NOT_FOUND)
    }

    @Test
    fun `should update post successfully`() {
        val updatedPostDto = PostDto(1L, 1L, 1L, "Updated Title", "Updated Description", "updated.png", "https://updated.com", 15.toBigDecimal())

        `when`(postService.updatePost(1L, updatedPostDto)).thenReturn(updatedPostDto)

        val response: ResponseEntity<PostDto> = postController.updatePost(1L, updatedPostDto)

        assert(response.statusCode == HttpStatus.OK)
        assert(response.body == updatedPostDto)
    }

    @Test
    fun `should return not found when updating non-existent post`() {
        val updatedPostDto = PostDto(1L, 1L, 1L, "Updated Title", "Updated Description", "updated.png", "https://updated.com", 15.toBigDecimal())

        `when`(postService.updatePost(99L, updatedPostDto)).thenReturn(null)

        val response: ResponseEntity<PostDto> = postController.updatePost(99L, updatedPostDto)

        assert(response.statusCode == HttpStatus.NOT_FOUND)
        assert(response.body == null)
    }

    @Test
    fun `should delete post successfully`() {
        doNothing().`when`(postService).deletePostById(1L)

        val response: ResponseEntity<Map<String, String>> = postController.deletePost(1L)

        assert(response.statusCode == HttpStatus.OK)
        assert(response.body?.get("message") == "Post with ID 1 was deleted successfully.")
    }
}
