package com.aday2dream.aday2dream.controller

import io.mockk.every
import io.mockk.mockkObject
import com.aday2dream.aday2dream.dto.AccountDto
import com.aday2dream.aday2dream.dto.AccountLoginDto
import com.aday2dream.aday2dream.dto.AccountRegisterDto
import com.aday2dream.aday2dream.entity.Account
import com.aday2dream.aday2dream.service.AccountService
import com.aday2dream.aday2dream.service.JwtService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken

@ExtendWith(MockitoExtension::class)
class AccountControllerTest {

    @Mock
    private lateinit var accountService: AccountService

    @Mock
    private lateinit var authenticationManager: AuthenticationManager

    @InjectMocks
    private lateinit var accountController: AccountController


    @BeforeEach
    fun setUp() {
        accountController = AccountController(accountService, authenticationManager)
    }

    @Test
    fun `should register new account successfully`() {
        val registerDto = AccountRegisterDto(1L, "username", "password", "email@example.com", "First", "Last")
        val account = Account(1L, "username", "password", "email@example.com", "First", "Last")

        `when`(accountService.register(registerDto)).thenReturn(account)

        val response = accountController.register(registerDto)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(account, response.body)
    }

    @Test
    fun `should login successfully and return token`() {
        mockkObject(JwtService)

        val loginDto = AccountLoginDto("username", "password")
        every { JwtService.generateToken("username") } returns "mock-token"

        val response = accountController.login(loginDto)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals("mock-token", response.body?.get("token"))
    }

    @Test
    fun `should return unauthorized on invalid login credentials`() {
        val loginDto = AccountLoginDto("wrongUser", "wrongPassword")

        `when`(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken::class.java)))
            .thenThrow(RuntimeException("Invalid credentials"))

        val response = accountController.login(loginDto)

        assertEquals(HttpStatus.UNAUTHORIZED, response.statusCode)
        assertEquals("Invalid credentials", response.body?.get("error"))
    }

    @Test
    fun `should return all accounts`() {
        val accounts = listOf(AccountDto(1L, "username", "email@example.com", "First", "Last"))

        `when`(accountService.getAllAccounts()).thenReturn(accounts)

        val response = accountController.getAllAccounts()

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(accounts, response.body)
    }

    @Test
    fun `should retrieve account by ID`() {
        val accountDto = AccountDto(1L, "username", "email@example.com", "First", "Last")

        `when`(accountService.getAccountById(1L)).thenReturn(accountDto)

        val response = accountController.getAccountById(1L)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(accountDto, response.body)
    }

    @Test
    fun `should update account successfully`() {
        val updatedAccountDto = AccountDto(1L, "updatedUsername", "updated@example.com", "updatedFirst", "updatedLast")

        `when`(accountService.updateAccount(1L, updatedAccountDto)).thenReturn(updatedAccountDto)

        val response = accountController.updateAccount(1L, updatedAccountDto)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(updatedAccountDto, response.body)
    }

    @Test
    fun `should delete account successfully`() {
        doNothing().`when`(accountService).deleteAccount(1L)

        val response = accountController.deleteAccount(1L)

        assertEquals(HttpStatus.CREATED, response.statusCode)
        assertEquals("Account succesfully deleted.", response.body)
    }
}
