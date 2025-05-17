package com.aday2dream.aday2dream.service

import com.aday2dream.aday2dream.controller.AccountController
import com.aday2dream.aday2dream.dto.AudioFileDto
import com.aday2dream.aday2dream.entity.AudioFile
import com.aday2dream.aday2dream.mapper.AudioFileMapper
import com.aday2dream.aday2dream.repository.AudioFileRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.exists

@Service
class AudioFileService(
    private val audioFileRepository: AudioFileRepository,
    private val audioFileMapper: AudioFileMapper,
    @Value("\${audiofile.storage.directory}")private val uploadDir: String
) {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(AccountController::class.java)
    }

    private val storageDirectory: Path = Paths.get(uploadDir)

    init {
        Files.createDirectories(storageDirectory)
    }

    fun uploadAudioFile(file: MultipartFile, title: String, duration: Int): AudioFileDto {
        if (file.isEmpty) throw IllegalArgumentException("File cannot be empty")

        val filePath = Paths.get(uploadDir, file.originalFilename ?: throw IllegalArgumentException("Invalid file name"))
        Files.copy(file.inputStream, filePath)

        val audioFile = AudioFile(
            uri = filePath.toString(),
            title = title,
            duration = duration
        )
        audioFileRepository.save(audioFile)
        return audioFileMapper.toDto(audioFile)
    }

    fun getAudioFileById(id: Long): AudioFileDto{
        return audioFileMapper.toDto(audioFileRepository.findById(id).orElseThrow { RuntimeException("AudioFile not found") })
    }


    fun deleteAudioFile(id: Long) {
        val audioFile = getAudioFileById(id)
        val filePath = Paths.get(audioFile.uri)
        if (filePath.exists()) {
            logger.info("File path exists, deleting...")
            Files.delete(filePath)
        }
        logger.info("Deleting by id: $id")
        audioFileRepository.deleteById(id)
    }

    fun getAllAudioFiles(): List<AudioFileDto> = audioFileMapper.toDtoList(audioFileRepository.findAll())
}
