package com.aday2dream.aday2dream.service

import com.aday2dream.aday2dream.controller.AccountController
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object JwtBlacklistService {
    private val blacklist = mutableSetOf<String>()
    private val logger: Logger = LoggerFactory.getLogger(AccountController::class.java)


    fun addToBlacklist(token: String) {
        blacklist.add(token)
        logger.info("Token added to blacklist.")
    }

    fun isBlacklisted(token: String): Boolean {
        logger.info("Checking whether $token is blacklisted...")
        return blacklist.contains(token)
    }
}
