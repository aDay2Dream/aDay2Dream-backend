package com.aday2dream.aday2dream.mapper

import com.aday2dream.aday2dream.dto.AccountDto
import com.aday2dream.aday2dream.dto.AccountRegisterDto
import com.aday2dream.aday2dream.entity.Account
import org.mapstruct.Mapper
import org.springframework.stereotype.Component

@Component
@Mapper
interface AccountMapper {
    fun toDto(account: Account): AccountDto
    fun toEntity(accountDto: AccountDto): Account
    fun toEntity(accountRegisterDto: AccountRegisterDto) : Account
}