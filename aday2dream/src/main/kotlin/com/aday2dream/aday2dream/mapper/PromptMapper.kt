package com.aday2dream.aday2dream.mapper

import com.aday2dream.aday2dream.dto.PromptDto
import com.aday2dream.aday2dream.entity.Account
import com.aday2dream.aday2dream.entity.Post
import com.aday2dream.aday2dream.entity.Prompt
import org.mapstruct.Mapper

import org.mapstruct.Mapping
import org.mapstruct.factory.Mappers

@Mapper(componentModel = "spring")
abstract class PromptMapper {
    companion object {
        val INSTANCE: PromptMapper = Mappers.getMapper(PromptMapper::class.java)
    }

    @Mapping(source = "post.postId", target = "postId")
    @Mapping(source = "buyer.accountId", target = "buyerId")
    abstract fun toDto(prompt: Prompt): PromptDto

    fun toEntity(promptDto: PromptDto, post: Post, buyer: Account): Prompt {
        return Prompt(
            promptId = promptDto.promptId,
            post = post,
            buyer = buyer,
            promptTitle = promptDto.promptTitle,
            promptDescription = promptDto.promptDescription,
            promptLinks = promptDto.promptLinks,
            startDate = promptDto.startDate,
            endDate = promptDto.endDate,
            createdAt = promptDto.createdAt,
            updatedAt = promptDto.updatedAt
        )
    }

    abstract fun toDtoList(prompts: List<Prompt>): List<PromptDto>

}


