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

@RestController
@RequestMapping("/accounts")
class AccountController(@Autowired private val accountService: AccountService,
                        private val authenticationManager: AuthenticationManager,
) {
    @PostMapping("/register")
    fun register(@RequestBody accountRegisterDto: AccountRegisterDto): ResponseEntity<Account> {
        val newAccount = accountService.register(accountRegisterDto)
        return ResponseEntity.ok(newAccount)
    }

    @PostMapping("/login")
    fun login(@RequestBody accountLoginDto: AccountLoginDto): ResponseEntity<Map<String, String>?> {
        println("Received login request: ${accountLoginDto.username}")
         return try {
            val authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(accountLoginDto.username, accountLoginDto.password)
            )
            val user = authentication.principal as UserDetails
            val token = JwtService.generateToken(accountLoginDto.username)
            return ResponseEntity.ok(mapOf("token" to token))
        } catch (e: BadCredentialsException) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(mapOf("error" to "Invalid credentials"))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mapOf("error" to "An error occurred"))
        }
    }

    @GetMapping
    fun getAllAccounts(): ResponseEntity<List<AccountDto>> {
        val users = accountService.getAllAccounts()
        return ResponseEntity.ok(users)
    }

    @GetMapping("/{id}")
    fun getAccountById(@PathVariable("id") accountId: Long): ResponseEntity<AccountDto> {
        val account = accountService.getAccountById(accountId)
        return ResponseEntity.ok(account)
    }

    @PutMapping("/{id}")
    fun updateAccount(
        @PathVariable("id") accountId: Long,
        @RequestBody updatedAccount: AccountDto,
        @RequestParam("password") password: String
    ): ResponseEntity<AccountDto> {
        return try {
            val user = accountService.updateAccount(accountId, updatedAccount, password)
            ResponseEntity.ok(user)
        } catch(e: Exception){
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Problem updating account")
        } as ResponseEntity<AccountDto>
    }

    @DeleteMapping("/{id}")
    fun deleteAccount(@PathVariable("id") accountId: Long): ResponseEntity<String?> {
        return try{
            accountService.deleteAccount(accountId)
            ResponseEntity.status(HttpStatus.CREATED).body("Account succesfully deleted")
        } catch(e: Exception){

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
            ResponseEntity.ok(account)
        } catch (e: UsernameNotFoundException) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        } catch (e: Exception) {
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
            return ResponseEntity.ok("Logged out successfully.")
        } catch(e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to log out")
        }
    }
}
