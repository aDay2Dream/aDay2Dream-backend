package com.aday2dream.aday2dream.service

import com.aday2dream.aday2dream.model.AudioFile
import com.aday2dream.aday2dream.repository.AudioFileRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import kotlin.io.path.exists

@Service
class AudioFileService(
    private val audioFileRepository: AudioFileRepository,
    @Value("\${audiofile.storage.directory}")private val uploadDir: String
) {

    private val storageDirectory: Path = Paths.get(uploadDir)



    init {
            // Ensure the directory exists
        Files.createDirectories(storageDirectory)
    }
    /*
    fun saveFile(file: MultipartFile, title: String, duration: Int): AudioFile {
        val filePath = Paths.get("uploads/${file.originalFilename}")
        Files.createDirectories(filePath.parent) // Create directories if they don't exist
        file.inputStream.use { inputStream ->
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING)
        }

        val audioFile = AudioFile(
            uri = filePath.toString(),
            title = title,
            duration = duration
        )
        return audioFileRepository.save(audioFile)
    }
    */

    fun uploadAudioFile(file: MultipartFile, title: String, duration: Int): AudioFile {
        if (file.isEmpty) throw IllegalArgumentException("File cannot be empty")

        val filePath = Paths.get(uploadDir, file.originalFilename ?: throw IllegalArgumentException("Invalid file name"))
        Files.copy(file.inputStream, filePath)

        val audioFile = AudioFile(
            uri = filePath.toString(),
            title = title,
            duration = duration
        )
        return audioFileRepository.save(audioFile)
    }

    fun getAudioFileById(id: Long): AudioFile =
        audioFileRepository.findById(id).orElseThrow { IllegalArgumentException("Audio file not found") }

    fun deleteAudioFile(id: Long) {
        val audioFile = getAudioFileById(id)
        val filePath = Paths.get(audioFile.uri)
        if (filePath.exists()) {
            Files.delete(filePath)
        }
        audioFileRepository.deleteById(id)
    }

    fun getAllAudioFiles(): List<AudioFile> = audioFileRepository.findAll()


}
