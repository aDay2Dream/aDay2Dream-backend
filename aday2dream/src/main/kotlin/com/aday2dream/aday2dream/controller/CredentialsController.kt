package com.aday2dream.aday2dream.controller

import com.aday2dream.aday2dream.service.CredentialsService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class AuthController(private val userService: CredentialsService) {

    @PostMapping("/register")
    fun register(@RequestBody request: AuthRequest): ResponseEntity<String> {
        return try {
            userService.register(request.username, request.password)
            ResponseEntity.ok("User registered successfully")
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(e.message)
        }
    }

    @PostMapping("/login")
    fun login(@RequestBody request: AuthRequest): ResponseEntity<String> {
        return if (userService.login(request.username, request.password)) {
            ResponseEntity.ok("Login successful")
        } else {
            ResponseEntity.badRequest().body("Invalid username or password")
        }
    }
}

data class AuthRequest(
    val username: String,
    val password: String
)
