package com.aday2dream.aday2dream.service

object JwtBlacklistService {
    private val blacklist = mutableSetOf<String>()

    fun addToBlacklist(token: String) {
        blacklist.add(token)
    }

    fun isBlacklisted(token: String): Boolean {
        return blacklist.contains(token)
    }
}
