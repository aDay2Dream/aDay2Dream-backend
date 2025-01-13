package com.aday2dream.aday2dream.config

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
    private val userDetailsService: CustomUserDetailsService,
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

            // If the token is null, pass the request to the next filter
            if (token == null) {
                filterChain.doFilter(request, response)
                return
            }

            // If username is present and not authenticated, validate the token and set authentication
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
            "" // Return an empty string if serialization fails
        }
    }
}
