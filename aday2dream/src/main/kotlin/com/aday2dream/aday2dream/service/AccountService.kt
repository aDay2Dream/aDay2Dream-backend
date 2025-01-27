package com.aday2dream.aday2dream.service

import com.aday2dream.aday2dream.dto.AccountDto
import com.aday2dream.aday2dream.entity.Account
import com.aday2dream.aday2dream.repository.AccountRepository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AccountService(
                    @Autowired
                     private val accountRepository: AccountRepository,
                     private val passwordEncoder: PasswordEncoder,
) {
    private fun mapToDto(account: Account): AccountDto {
        return AccountDto(
            accountId = account.accountId,
            username = account.username,
            password = account.password,
            email = account.email,
            firstName = account.firstName,
            lastName = account.lastName,
            profilePicture = account.profilePicture,
            description = account.description,
            links = account.links
        )
    }

    private fun mapToEntity(userDto: AccountDto, hashedPassword: String): Account {
        return Account(
            accountId = userDto.accountId,
            username = userDto.username,
            password = hashedPassword,
            email = userDto.email,
            firstName = userDto.firstName,
            lastName = userDto.lastName,
            profilePicture = userDto.profilePicture,
            description = userDto.description,
            links = userDto.links
        )
    }

    fun createAccount(account: AccountDto, rawPassword: String): AccountDto {
        val hashedPassword = passwordEncoder.encode(rawPassword)

        val account = mapToEntity(account, hashedPassword)
        val savedAccount = accountRepository.save(account)
        return mapToDto(savedAccount)
    }

    fun getAllAccounts(): List<AccountDto> {
        return accountRepository.findAll().map { mapToDto(it) }
    }

    fun getAccountById(accountId: Long): AccountDto {
        val account = accountRepository.findById(accountId).orElseThrow { RuntimeException("User not found") }
        return mapToDto(account)
    }

    fun updateAccount(accountId: Long, accountDto: AccountDto, rawPassword: String?): AccountDto {

        val existingAccount = accountRepository.findById(accountId).orElseThrow { RuntimeException("User not found") }
        val updatedAccount = existingAccount.copy(
            username = accountDto.username,
            email = accountDto.email,
            password = rawPassword?.let { passwordEncoder.encode(it) } ?: existingAccount.password,
            firstName = accountDto.firstName,
            lastName = accountDto.lastName,
            profilePicture = accountDto.profilePicture,
            description = accountDto.description,
            links = accountDto.links
        )
        val savedAccount = accountRepository.save(updatedAccount)
        return mapToDto(savedAccount)
    }

    fun deleteAccount(accountId: Long) {
        if (!accountRepository.existsById(accountId)) {
            throw RuntimeException("User not found")
        }
        accountRepository.deleteById(accountId)
    }

    fun verifyPassword(username: String, rawPassword: String): Boolean {
        val account = accountRepository.findByUsername(username)
            ?: throw RuntimeException("User not found")
        return passwordEncoder.matches(rawPassword, account.password)
    }

    fun register(accountRegistrationDTO: AccountDto, rawPassword: String): Account {


        val account = Account(
            username = accountRegistrationDTO.username,
            email = accountRegistrationDTO.email,
            password = passwordEncoder.encode(rawPassword),
            firstName = accountRegistrationDTO.firstName,
            lastName = accountRegistrationDTO.lastName
        )
        var existingAccount = accountRepository.findByUsername(account.username)

        if (existingAccount == null) {
            throw IllegalArgumentException("Username already exists")
        }
        existingAccount = accountRepository.findByEmail(account.email)
        if (existingAccount == null) {
            throw IllegalArgumentException("Email already exists")
        }
        return accountRepository.save(account)
    }

    fun getAccountByUsername(username: String): AccountDto {
        val account = accountRepository.findByUsername(username)
            ?: throw UsernameNotFoundException("Account not found for username: $username") as Throwable

        return mapToDto(account)
    }
}



