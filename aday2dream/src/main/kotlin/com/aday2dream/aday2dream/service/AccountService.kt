package com.aday2dream.aday2dream.service
import com.aday2dream.aday2dream.dto.AccountDto
import com.aday2dream.aday2dream.model.Account
import com.aday2dream.aday2dream.repository.AccountRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AccountService(@Autowired
                     private val accountRepository: AccountRepository) {

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

    private fun mapToEntity(userDto: AccountDto): Account {
        return Account(
            accountId = userDto.accountId,
            username = userDto.username,
            password = userDto.password,
            email = userDto.email,
            firstName = userDto.firstName,
            lastName = userDto.lastName,
            profilePicture = userDto.profilePicture,
            description = userDto.description,
            links = userDto.links
        )
    }

    fun createAccount(account: AccountDto): AccountDto {
        val account = mapToEntity(account)
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


    fun updateAccount(accountId: Long, accountDto: AccountDto): AccountDto {
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
        return mapToDto(savedAccount)
    }


    fun deleteAccount(accountId: Long) {
        if (!accountRepository.existsById(accountId)) {
            throw RuntimeException("User not found")
        }
        accountRepository.deleteById(accountId)
    }
}


