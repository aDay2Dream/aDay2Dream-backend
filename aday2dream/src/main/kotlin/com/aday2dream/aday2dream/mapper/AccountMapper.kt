package com.aday2dream.aday2dream.mapper

import com.aday2dream.aday2dream.dto.AccountDto
import com.aday2dream.aday2dream.dto.AccountRegisterDto
import com.aday2dream.aday2dream.entity.Account
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.factory.Mappers
import org.springframework.stereotype.Component

@Component
@Mapper(componentModel = "spring")
interface AccountMapper {

    companion object{
        val INSTANCE: AccountMapper = Mappers.getMapper(AccountMapper::class.java)
    }

    fun toDto(account: Account): AccountDto
    fun toEntity(accountDto: AccountDto): Account

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    fun toRegisterEntity(accountRegisterDto: AccountRegisterDto) : Account
}