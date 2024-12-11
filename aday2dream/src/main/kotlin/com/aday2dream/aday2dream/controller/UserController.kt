package com.aday2dream.aday2dream.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import com.aday2dream.aday2dream.model.User
import com.aday2dream.aday2dream.service.UserService

@RestController
@RequestMapping("/api/users")
class UserController(private val userService: UserService) {

    @GetMapping
    fun getAllUsers(): List<User> = userService.getAllUsers()

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: Long): ResponseEntity<User> {
        val user = userService.getUserById(id)
        return ResponseEntity.ok(user)
    }

    @PostMapping
    fun createUser(@RequestBody user: User): User = userService.saveUser(user)

    @PutMapping("/{id}")
    fun updateUser(@PathVariable id: Long, @RequestBody userDetails: User): ResponseEntity<User> {
        return try {
            ResponseEntity.ok(userService.updateUser(id, userDetails))
        } catch (e: RuntimeException) {
            ResponseEntity.notFound().build()
        }
    }

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: Long): ResponseEntity<Void> {
        return try {
            userService.deleteUserById(id)
            ResponseEntity.noContent().build()
        } catch (e: RuntimeException) {
            ResponseEntity.notFound().build()
        }
    }
}
