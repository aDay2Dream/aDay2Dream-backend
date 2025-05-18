package com.aday2dream.aday2dream.service

import com.aday2dream.aday2dream.controller.AccountController
import com.aday2dream.aday2dream.dto.AccountDto
import com.aday2dream.aday2dream.dto.AccountRegisterDto
import com.aday2dream.aday2dream.mapper.AccountMapper
import com.aday2dream.aday2dream.entity.Account
import com.aday2dream.aday2dream.repository.AccountRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory

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

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(AccountController::class.java)
    }

    fun getAllAccounts(): List<AccountDto> {
        logger.info("Finding all accounts...")
        return accountRepository.findAll().map { accountMapper.toDto(it) }
    }

    fun getAccountById(accountId: Long): AccountDto {
        logger.info("Retrieving account by id: $accountId")
        val account = accountRepository.findById(accountId).orElseThrow {
            RuntimeException("User not found")
        }
        logger.info("Retrieving account: ${account.toString()}")
        return accountMapper.toDto(account)
    }

    fun updateAccount(accountId: Long, accountDto: AccountDto): AccountDto {
        logger.info("Updating account: ${accountDto.toString()}")
        val existingAccount = accountRepository.findById(accountId).orElseThrow { RuntimeException("User not found") }
        val updatedAccount = existingAccount.copy(
            username = accountDto.username,
            email = accountDto.email,
            firstName = accountDto.firstName,
            lastName = accountDto.lastName,
            profilePicture = accountDto.profilePicture,
            description = accountDto.description,
            links = accountDto.links
        )
        val savedAccount = accountRepository.save(updatedAccount)
        logger.info("Updated account: ${savedAccount.toString()}")
        return accountMapper.toDto(savedAccount)
    }

    fun deleteAccount(accountId: Long) {
        logger.info("Deleting account by id: $accountId")
        if (!accountRepository.existsById(accountId)) {
            throw RuntimeException("User not found")
        }
        accountRepository.deleteById(accountId)
    }

    fun register(accountDto: AccountRegisterDto): Account {
        val hashedPassword = passwordEncoder.encode(accountDto.password)
        var account = accountRepository.findByUsername(accountDto.username)

        if (account != null) {
            logger.warn("Username already exists.")
            throw IllegalArgumentException("Username already exists")
        }
        account = accountRepository.findByEmail(accountDto.email)
        if (account != null) {
            logger.warn("Email already exists.")
            throw IllegalArgumentException("Email already exists")
        }

        account = accountMapper.toRegisterEntity(accountDto)
        logger.info("Registered account: ${account.toString()}")
        account.password = hashedPassword
        logger.info("Hashed password: $hashedPassword")
        return accountRepository.save(account)
    }

    fun getAccountByUsername(username: String): AccountDto {
        val account = accountRepository.findByUsername(username)
            ?: throw UsernameNotFoundException("Account not found for username: $username")
        logger.info("Retrieved account: ${account.toString()}")
        return accountMapper.toDto(account)
    }
}



