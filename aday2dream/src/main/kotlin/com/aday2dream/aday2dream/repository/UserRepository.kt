package com.aday2dream.aday2dream.repository

import com.aday2dream.aday2dream.model.User
import org.springframework.data.jpa.repository.JpaRepository


interface UserRepository : JpaRepository<User, Long>

