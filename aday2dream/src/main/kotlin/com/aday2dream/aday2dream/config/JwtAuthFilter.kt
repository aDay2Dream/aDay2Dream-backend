package com.aday2dream.aday2dream.config

import com.aday2dream.aday2dream.service.JwtBlacklistService
import com.aday2dream.aday2dream.service.JwtService
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthFilter(
    private val userDetailsService: AccountDetailsService,
    private val objectMapper: ObjectMapper
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            val authHeader = request.getHeader("Authorization")
            val token: String? = authHeader?.takeIf { it.startsWith("Bearer ") }?.substring(7)
            val username: String? = token?.let { JwtService.extractUsername(it) }


            if (token == null) {
                filterChain.doFilter(request, response)
                return
            }

            if (authHeader.startsWith("Bearer ")) {
                val token = authHeader.replace("Bearer ", "")
                if (JwtBlacklistService.isBlacklisted(token)) {
                    response.status = HttpServletResponse.SC_UNAUTHORIZED
                    response.writer.write("Token is invalid or has been logged out")
                    return
                }
            }

            if (username != null && SecurityContextHolder.getContext().authentication == null) {
                val userDetails: UserDetails = userDetailsService.loadUserByUsername(username)
                if (JwtService.validateToken(token, userDetails)) {
                    val authenticationToken = UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.authorities
                    )
                    authenticationToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                    SecurityContextHolder.getContext().authentication = authenticationToken
                }
            }

            filterChain.doFilter(request, response)
        } catch (e: AccessDeniedException) {
            handleAccessDeniedException(response, e)
        }
    }

    private fun handleAccessDeniedException(response: HttpServletResponse, e: AccessDeniedException) {
        response.status = HttpServletResponse.SC_FORBIDDEN
        response.contentType = "application/json"
        val errorResponse = mapOf(
            "status" to HttpServletResponse.SC_FORBIDDEN,
            "message" to (e.message ?: "Access denied"),
            "timestamp" to System.currentTimeMillis()
        )
        response.writer.write(toJson(errorResponse))
    }

    private fun toJson(response: Any): String {
        return try {
            objectMapper.writeValueAsString(response)
        } catch (e: Exception) {
            ""
        }
    }
}
