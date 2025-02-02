package com.aday2dream.aday2dream.service

import com.aday2dream.aday2dream.dto.AccountDto
import com.aday2dream.aday2dream.dto.AccountRegisterDto
import com.aday2dream.aday2dream.mapper.AccountMapper
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
                     private val accountMapper: AccountMapper,

) {

    fun getAllAccounts(): List<AccountDto> {
        return accountRepository.findAll().map { accountMapper.toDto(it) }
    }

    fun getAccountById(accountId: Long): AccountDto {
        val account = accountRepository.findById(accountId).orElseThrow { RuntimeException("User not found") }
        return accountMapper.toDto(account)
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
        return accountMapper.toDto(savedAccount)
    }

    fun deleteAccount(accountId: Long) {
        if (!accountRepository.existsById(accountId)) {
            throw RuntimeException("User not found")
        }
        accountRepository.deleteById(accountId)
    }

    fun register(accountDto: AccountRegisterDto): Account {
        val hashedPassword = passwordEncoder.encode(accountDto.password)
        var account = accountRepository.findByUsername(accountDto.username)
        println(account)
        if (account != null) {
            throw IllegalArgumentException("Username already exists")
        }
        account = accountRepository.findByEmail(accountDto.email)
        if (account != null) {
            throw IllegalArgumentException("Email already exists")
        }

        account = accountMapper.toRegisterEntity(accountDto)
        println(account)
        account.password = hashedPassword
        return accountRepository.save(account)
    }

    fun getAccountByUsername(username: String): AccountDto {
        val account = accountRepository.findByUsername(username)
            ?: throw UsernameNotFoundException("Account not found for username: $username")

        return accountMapper.toDto(account)
    }
}



