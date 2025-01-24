package com.aday2dream.aday2dream.mapper

import com.aday2dream.aday2dream.dto.AccountDto
import com.aday2dream.aday2dream.entity.Account
import org.mapstruct.Mapper
import org.springframework.stereotype.Component

@Component
@Mapper(componentModel = "spring")
interface AccountMapper {
    fun toDto(account: Account): AccountDto
    fun toBean(accountDto: AccountDto): Account
}