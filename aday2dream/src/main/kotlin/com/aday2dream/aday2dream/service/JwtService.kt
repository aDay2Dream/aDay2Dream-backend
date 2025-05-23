package com.aday2dream.aday2dream.service

import com.aday2dream.aday2dream.controller.AccountController
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SignatureException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import java.security.Key
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Date

object JwtService {
    private val logger: Logger = LoggerFactory.getLogger(AccountController::class.java)


    private var SECRET_KEY: Key = Keys.secretKeyFor(SignatureAlgorithm.HS256)

    @Value("\${jwt.minutes}")
    private val minutes: Long = 60

    fun generateToken(email: String): String {
        logger.info("Generating token from email...")
        val now = Instant.now()
        return Jwts.builder()
            .setSubject(email)
            .setIssuedAt(Date.from(now))
            .setExpiration(Date.from(now.plus(minutes, ChronoUnit.MINUTES)))
            .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
            .compact()
    }

    fun extractUsername(token: String): String {
        logger.info("Extracting username from token...")
        return getTokenBody(token).subject
    }

    fun validateToken(token: String, userDetails: UserDetails): Boolean {
        val username = extractUsername(token)
        return username == userDetails.username && !isTokenExpired(token)
    }

    private fun getTokenBody(token: String): Claims {
        return try {
            Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .body
        } catch (e: SignatureException) {
            println("Access denied: Invalid signature")
            throw IllegalArgumentException("Invalid token signature", e)  // Throw a proper exception to propagate the error
        } catch (e: ExpiredJwtException) {
            println("Access denied: Token expired")
            throw IllegalArgumentException("Expired token", e)  // Throw a proper exception
        }
    }


    private fun isTokenExpired(token: String): Boolean {
        logger.info("Checking whether token has expired...")
        val claims = getTokenBody(token)
        logger.info("Token: $token")
        return claims.expiration.before(Date())
    }
}

