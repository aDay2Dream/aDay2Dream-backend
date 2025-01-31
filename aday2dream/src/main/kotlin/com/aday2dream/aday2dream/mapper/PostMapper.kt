package com.aday2dream.aday2dream.mapper

import com.aday2dream.aday2dream.dto.PostDto
import com.aday2dream.aday2dream.entity.Account
import com.aday2dream.aday2dream.entity.AudioFile
import com.aday2dream.aday2dream.entity.Post


class PostMapper(){

    fun toEntity(dto: PostDto, publisher: Account, audiofile: AudioFile): Post {
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

    fun toDTO(post: Post): PostDto{
        return PostDto(
            postId = post.postId,
            accountId = post.account.accountId,
            audiofileId = post.audiofile.audiofileId,
            title = post.title,
            description = post.description,
        backgroundImage = post.backgroundImage,
        hyperlinks = post.hyperlinks,
        price = post.price
        )
    }


}