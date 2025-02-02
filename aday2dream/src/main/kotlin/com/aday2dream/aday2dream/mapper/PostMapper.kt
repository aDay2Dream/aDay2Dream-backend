package com.aday2dream.aday2dream.mapper

import com.aday2dream.aday2dream.dto.PostDto
import com.aday2dream.aday2dream.entity.Account
import com.aday2dream.aday2dream.entity.AudioFile
import com.aday2dream.aday2dream.entity.Post
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.factory.Mappers
import java.util.Optional

@Mapper(componentModel = "spring")
abstract class PostMapper {

    companion object{
        val INSTANCE: PostMapper = Mappers.getMapper(PostMapper::class.java)
    }

    fun toEntity(dto: PostDto, publisher: Account, audiofile: AudioFile) : Post {
            return Post(
                account = publisher,
                audiofile = audiofile,
                title = dto.title,
                description = dto.description,
                backgroundImage = dto.backgroundImage,
                hyperlinks = dto.hyperlinks,
                price = dto.price
            )
    }

    @Mapping(source = "account.accountId", target = "accountId")
    @Mapping(source = "audiofile.audiofileId", target = "audiofileId")
    abstract fun toDto(post: Post): PostDto
}