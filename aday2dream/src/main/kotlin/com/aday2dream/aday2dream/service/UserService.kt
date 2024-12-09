package com.aday2dream.aday2dream.service

import com.aday2dream.aday2dream.model.User
import com.aday2dream.aday2dream.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) {

    fun getAllUsers(): MutableList<User> = userRepository.findAll()

    fun getUserById(id: Long): User? = userRepository.findById(id).orElse(null)

    fun saveUser(user: User): User = userRepository.save(user)

    fun deleteUserById(id: Long) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id)
        } else {
            throw RuntimeException("User not found with id: $id")
        }
    }

    fun updateUser(id: Long, userDetails: User): User {
        val existingUser = userRepository.findById(id).orElseThrow {
            RuntimeException("User not found with id: $id")
        }
        existingUser.apply {
            firstName = userDetails.firstName
            lastName = userDetails.lastName
            email = userDetails.email
            password = userDetails.password
        }
        return userRepository.save(existingUser)
    }
}

