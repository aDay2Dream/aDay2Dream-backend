package com.aday2dream.aday2dream.controller

import com.aday2dream.aday2dream.dto.AccountDto
import com.aday2dream.aday2dream.dto.AccountLoginDto
import com.aday2dream.aday2dream.model.Account
import com.aday2dream.aday2dream.service.AccountService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/accounts")
class AccountController(@Autowired private val accountService: AccountService) {

    @PostMapping("/register")
    fun register(@RequestBody accountDto: AccountDto,
    @RequestParam("password") password: String): ResponseEntity<Account> {
        val newAccount = accountService.register(accountDto, password)
        return ResponseEntity.ok(newAccount)
    }

    @PostMapping("/login")
    fun login(@RequestBody accountLoginDto: AccountLoginDto): ResponseEntity<Map<String, String>?> {
        return try {
            val token = accountService.login(accountLoginDto)
            ResponseEntity.ok(mapOf("message" to token))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(mapOf("error" to (e.message ?: "Invalid credentials")))
        }
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
        val user = accountService.getAccountById(accountId)
        return ResponseEntity.ok(user)
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

    @PostMapping("/verify")
    fun verifyPassword(
        @RequestParam("username") username: String,
        @RequestParam("password") password: String
    ): ResponseEntity<Boolean> {
        val isValid = accountService.verifyPassword(username, password)
        return ResponseEntity.ok(isValid)
    }

}
