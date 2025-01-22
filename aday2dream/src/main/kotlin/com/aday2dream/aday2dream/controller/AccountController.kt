package com.aday2dream.aday2dream.controller

import com.aday2dream.aday2dream.dto.AccountDto
import com.aday2dream.aday2dream.dto.AccountLoginDto
import com.aday2dream.aday2dream.model.Account
import com.aday2dream.aday2dream.service.AccountService
import com.aday2dream.aday2dream.service.JwtBlacklistService
import com.aday2dream.aday2dream.service.JwtService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
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
    fun register(@RequestBody accountDto: AccountDto,
    @RequestParam("password") password: String): ResponseEntity<Account> {
        val newAccount = accountService.register(accountDto, password)
        return ResponseEntity.ok(newAccount)
    }
    

    @PostMapping("/login")
    fun login(@RequestBody accountLoginDTO: AccountLoginDto): ResponseEntity<Map<String, String>?> {
        println("Received login request: ${accountLoginDTO.username}")

        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(accountLoginDTO.username, accountLoginDTO.password)
        )
        val user = authentication.principal as UserDetails
        val token = JwtService.generateToken(accountLoginDTO.username)
        return ResponseEntity.ok(mapOf("token" to token))
    }


    @PostMapping
    fun createAccount(@RequestBody account: AccountDto,
                      @RequestParam("password") password : String): ResponseEntity<AccountDto> {
        val createdAccount = accountService.createAccount(account, password)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAccount)
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
        val user = accountService.updateAccount(accountId, updatedAccount, password)
        return ResponseEntity.ok(user)
    }


    @DeleteMapping("/{id}")
    fun deleteAccount(@PathVariable("id") accountId: Long): ResponseEntity<Void> {
        accountService.deleteAccount(accountId)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/logout")
    fun logout(
        request: HttpServletRequest,
        response: HttpServletResponse
    ): ResponseEntity<String> {
        return try{
            val authentication = SecurityContextHolder.getContext().authentication
            val token = request.getHeader("Authorization")?.replace("Bearer ", "")
            if (!token.isNullOrEmpty()) {
                JwtBlacklistService.addToBlacklist(token)
            }
            if (authentication != null) {
                SecurityContextLogoutHandler().logout(request, response, authentication)
            }

            return ResponseEntity.ok("Logged out successfully.")
        } catch(e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to log out")
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

}
