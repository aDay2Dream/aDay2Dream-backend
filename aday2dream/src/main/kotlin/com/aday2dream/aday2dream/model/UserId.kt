package com.aday2dream.aday2dream.model

import java.io.Serializable

data class UserId(
    val id: Long,
    val credentialsId: Long
) : Serializable
