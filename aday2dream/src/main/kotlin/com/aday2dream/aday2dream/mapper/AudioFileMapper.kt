package com.aday2dream.aday2dream.mapper
import com.aday2dream.aday2dream.dto.AudioFileDto
import com.aday2dream.aday2dream.entity.AudioFile
import org.mapstruct.Mapper
import org.springframework.stereotype.Component

@Component
@Mapper(componentModel = "spring")
interface AudioFileMapper {
    fun toEntity(audiofileDto: AudioFileDto) : AudioFile
    fun toDto(audiofile: AudioFile) : AudioFileDto
    fun toDtoList(audiofiles: List<AudioFile>): List<AudioFileDto>
}
