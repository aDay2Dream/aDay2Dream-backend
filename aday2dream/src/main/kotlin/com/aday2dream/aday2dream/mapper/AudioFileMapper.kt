package com.aday2dream.aday2dream.mapper
import com.aday2dream.aday2dream.dto.AudioFileDTO
import com.aday2dream.aday2dream.entity.AudioFile
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class AudioFileMapper {
    fun toDTO(audioFile: AudioFile): AudioFileDTO = AudioFileDTO(
        audiofileId = audioFile.audiofileId,
        title = audioFile.title,
        uri = audioFile.uri,
        duration = audioFile.duration,
        uploadedAt = LocalDateTime.now().toString()
    )

    fun toEntity(audioFileDTO: AudioFileDTO): AudioFile = AudioFile(
        audiofileId = audioFileDTO.audiofileId,
        title = audioFileDTO.title,
        uri = audioFileDTO.uri,
        duration = audioFileDTO.duration,
    )
}
