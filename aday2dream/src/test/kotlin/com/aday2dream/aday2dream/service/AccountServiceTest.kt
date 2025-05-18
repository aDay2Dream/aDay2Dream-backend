package com.aday2dream.aday2dream.service

import com.aday2dream.aday2dream.dto.AccountDto
import com.aday2dream.aday2dream.dto.AccountRegisterDto
import com.aday2dream.aday2dream.entity.Account
import com.aday2dream.aday2dream.mapper.AccountMapper
import com.aday2dream.aday2dream.repository.AccountRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.Optional

@ExtendWith(MockitoExtension::class)
class AccountServiceTest {

    @Mock
    private lateinit var accountRepository: AccountRepository

    @Mock
    private lateinit var passwordEncoder: PasswordEncoder

    @Mock
    private lateinit var accountMapper: AccountMapper

    @InjectMocks
    private lateinit var accountService: AccountService

    private val testAccount = Account(
        username = "newuser",
        email = "newuser@example.com",
        password = "plain",
        firstName = "hello",
        lastName = "hi",
        profilePicture = null,
        description = null,
        links = null
    )

    private val testAccountDto = AccountDto(
        username = "newuser",
        email = "newuser@example.com",
        firstName = "hello",
        lastName = "hi",
        profilePicture = null,
        description = null,
        links = null
    )

    @Test
    fun `getAllAccounts returns mapped list`() {
        whenever(accountRepository.findAll()).thenReturn(listOf(testAccount))
        whenever(accountMapper.toDto(testAccount)).thenReturn(testAccountDto)

        val result = accountService.getAllAccounts()

        assertEquals(1, result.size)
        assertEquals("newuser", result[0].username)
    }

    @Test
    fun `getAccountById returns accountDto if found`() {
        whenever(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount))
        whenever(accountMapper.toDto(testAccount)).thenReturn(testAccountDto)

        val result = accountService.getAccountById(1L)

        assertEquals("newuser", result.username)
    }

    @Test
    fun `getAccountById throws if account not found`() {
        whenever(accountRepository.findById(1L)).thenReturn(Optional.empty())

        assertThrows<RuntimeException> {
            accountService.getAccountById(1L)
        }
    }

    @Test
    fun `updateAccount saves and returns updated account`() {
        val updatedDto = testAccountDto.copy(username = "updatedUser")
        val updatedEntity = testAccount.copy(username = "updatedUser")

        whenever(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount))
        whenever(accountRepository.save(any())).thenReturn(updatedEntity)
        whenever(accountMapper.toDto(updatedEntity)).thenReturn(updatedDto)

        val result = accountService.updateAccount(1L, updatedDto)

        assertEquals("updatedUser", result.username)
    }

    @Test
    fun `deleteAccount deletes if exists`() {
        whenever(accountRepository.existsById(1L)).thenReturn(true)

        accountService.deleteAccount(1L)

        verify(accountRepository).deleteById(1L)
    }

    @Test
    fun `deleteAccount throws if account does not exist`() {
        whenever(accountRepository.existsById(1L)).thenReturn(false)

        assertThrows<RuntimeException> {
            accountService.deleteAccount(1L)
        }
    }

    @Test
    fun `register creates account with hashed password`() {
        val registerDto = AccountRegisterDto(
            accountId = 1L,
            username = "newuser",
            password = "plain",
            email = "newuser@example.com",
            firstName = "hello",
            lastName = "hi"
        )

        val newAccount = testAccount.copy(username = "newuser", email = "new@example.com")

        whenever(accountRepository.findByUsername("newuser")).thenReturn(null)
        whenever(accountRepository.findByEmail("newuser@example.com")).thenReturn(null)
        whenever(accountMapper.toRegisterEntity(registerDto)).thenReturn(newAccount)
        whenever(passwordEncoder.encode("plain")).thenReturn("hashed")
        whenever(accountRepository.save(any())).thenAnswer { it.arguments[0] }

        val result = accountService.register(registerDto)

        assertEquals("newuser", result.username)
        assertEquals("hashed", result.password)
    }

    @Test
    fun `register throws if username exists`() {
        val dto = AccountRegisterDto(1L, "newuser", "plain", "newuser@example.com", firstName = "hello", lastName = "hi")

        whenever(accountRepository.findByUsername("newuser")).thenReturn(testAccount)

        assertThrows<IllegalArgumentException> {
            accountService.register(dto)
        }
    }

    @Test
    fun `register throws if email exists`() {
        val dto = AccountRegisterDto(1L, "newuser", "plain", "newuser@example.com", firstName = "hello", lastName = "hi")

        whenever(accountRepository.findByUsername("newuser")).thenReturn(null)
        whenever(accountRepository.findByEmail("newuser@example.com")).thenReturn(testAccount)

        assertThrows<IllegalArgumentException> {
            accountService.register(dto)
        }
    }

    @Test
    fun `getAccountByUsername returns dto if found`() {
        whenever(accountRepository.findByUsername("newuser")).thenReturn(testAccount)
        whenever(accountMapper.toDto(testAccount)).thenReturn(testAccountDto)

        val result = accountService.getAccountByUsername("newuser")

        assertEquals("newuser", result.username)
    }

    @Test
    fun `getAccountByUsername throws if not found`() {
        whenever(accountRepository.findByUsername("missing")).thenReturn(null)

        assertThrows<UsernameNotFoundException> {
            accountService.getAccountByUsername("missing")
        }
    }
}
