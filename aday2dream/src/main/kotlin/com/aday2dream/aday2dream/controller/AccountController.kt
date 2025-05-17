package com.aday2dream.aday2dream.controller

import com.aday2dream.aday2dream.dto.AccountDto
import com.aday2dream.aday2dream.dto.AccountLoginDto
import com.aday2dream.aday2dream.dto.AccountRegisterDto
import com.aday2dream.aday2dream.entity.Account
import com.aday2dream.aday2dream.service.AccountService
import com.aday2dream.aday2dream.service.JwtBlacklistService
import com.aday2dream.aday2dream.service.JwtService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler
import org.springframework.web.bind.annotation.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Slf4j
@RestController
@RequestMapping("/accounts")
class AccountController(@Autowired private val accountService: AccountService,
                        private val authenticationManager: AuthenticationManager,
) {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(AccountController::class.java)
    }

    @PostMapping("/register")
    fun register(@RequestBody accountRegisterDto: AccountRegisterDto): ResponseEntity<Account> {
        val newAccount = accountService.register(accountRegisterDto)
        logger.info("New user registered: ${newAccount.toString()}")
        return ResponseEntity.ok(newAccount)
    }

    @PostMapping("/login")
    fun login(@RequestBody accountLoginDto: AccountLoginDto): ResponseEntity<Map<String, String>?> {
        logger.info("Received login request for username: ${accountLoginDto.username}")
        return try {
            val authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(accountLoginDto.username, accountLoginDto.password)
            )
            val user = authentication.principal as UserDetails
            val token = JwtService.generateToken(accountLoginDto.username)
            logger.info("Login successful for username: ${accountLoginDto.username}")
            return ResponseEntity.ok(mapOf("token" to token))
        } catch (e: BadCredentialsException) {

            logger.warn("Login failed due to bad credentials for username: ${accountLoginDto.username}")
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(mapOf("error" to "Invalid credentials"))
        } catch (e: Exception) {
            logger.error("Unexpected error during login", e)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mapOf("error" to "An error occurred"))
        }
    }

    @GetMapping
    fun getAllAccounts(): ResponseEntity<List<AccountDto>> {
        val users = accountService.getAllAccounts()
        logger.info("Received users: ${users.toString()}")
        return ResponseEntity.ok(users)
    }

    @GetMapping("/{id}")
    fun getAccountById(@PathVariable("id") accountId: Long): ResponseEntity<AccountDto> {
        val account = accountService.getAccountById(accountId)
        logger.info("Received user: ${account.toString()}")
        return ResponseEntity.ok(account)
    }

    @PutMapping("/{id}")
    fun updateAccount(
        @PathVariable("id") accountId: Long,
        @RequestBody updatedAccount: AccountDto,
    ): ResponseEntity<AccountDto> {
        return try {
            val user = accountService.updateAccount(accountId, updatedAccount)
            logger.info("Updated account: ${user.toString()}")
            ResponseEntity.ok(user)
        } catch(e: Exception){
            logger.error("Error updating the account.")
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Problem updating account")
        } as ResponseEntity<AccountDto>
    }

    @DeleteMapping("/{id}")
    fun deleteAccount(@PathVariable("id") accountId: Long): ResponseEntity<String?> {
        return try{
            accountService.deleteAccount(accountId)
            logger.info("Account successfully deleted.")
            ResponseEntity.status(HttpStatus.CREATED).body("Account succesfully deleted.")
        } catch(e: Exception){
            logger.error("Error deleting the account.")
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("There was a problem with the deletion of the account.")
        }
    }

    @GetMapping("/profile")
    fun getProfile(): ResponseEntity<AccountDto> {
        return try
        {

        val authentication = SecurityContextHolder.getContext().authentication
        val username = authentication?.name

        if (username.isNullOrEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }

            val account = accountService.getAccountByUsername(username)
            logger.info("Username not found.")
            ResponseEntity.ok(account)
        } catch (e: UsernameNotFoundException) {
            logger.error("Username not found.")
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        } catch (e: Exception) {
            logger.error("Could not find profile, associated with the provided username.")
         ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }

    @PostMapping("/logout")
    fun logout(
        request: HttpServletRequest,
        response: HttpServletResponse
    ): ResponseEntity<String> {
        return try{
            val authentication = SecurityContextHolder.getContext().authentication
            if (authentication != null) {
                val token = request.getHeader("Authorization")?.replace("Bearer ", "")
                if (!token.isNullOrEmpty()) {
                    JwtBlacklistService.addToBlacklist(token)
                }
                SecurityContextLogoutHandler().logout(request, response, authentication)
            }
            logger.info("Logged out successfully.")
            return ResponseEntity.ok("Logged out successfully.")
        } catch(e: Exception) {
            logger.error("Failed to log out.")
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to log out.")
        }
    }
}
