package com.aday2dream.aday2dream.service

import com.aday2dream.aday2dream.model.User
import com.aday2dream.aday2dream.repository.UserRepository
import org.springframework.stereotype.Service


@Service
class UserService(private val userRepository: UserRepository) {

    fun getAllUsers(): List<User> = userRepository.findAll()

    fun getUserById(id: Long): User = userRepository.findById(id).orElseThrow {
        RuntimeException("User with ID $id not found.")
    }

    fun saveUser(user: User): User {
        if (userRepository.existsByEmail(user.email)) {
            throw RuntimeException("A user with email ${user.email} already exists.")
        }
        return userRepository.save(user)
    }

    fun updateUser(id: Long, userDetails: User): User {
        val existingUser = getUserById(id)
        existingUser.apply {
            firstName = userDetails.firstName
            lastName = userDetails.lastName
            email = userDetails.email
            credentialsId = userDetails.credentialsId
        }
        return userRepository.save(existingUser)
    }

    fun deleteUserById(id: Long) {
        if (!userRepository.existsById(id)) {
            throw RuntimeException("User with ID $id does not exist.")
        }
        userRepository.deleteById(id)
    }
}
