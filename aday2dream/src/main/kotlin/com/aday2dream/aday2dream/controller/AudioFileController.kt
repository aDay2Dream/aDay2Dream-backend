package com.aday2dream.aday2dream.controller

import com.aday2dream.aday2dream.service.AudioFileService
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.FileNotFoundException


@RestController
@RequestMapping("/audiofiles")
class AudioFileController(
    private val audioFileService: AudioFileService,
) {
    @PostMapping("/upload")
    fun uploadAudioFile(
        @RequestParam("file") file: MultipartFile,
        @RequestParam("title") title: String,
        @RequestParam("duration") duration: Int
    ): ResponseEntity<Any> {
        return try {
            val uploadedFile = audioFileService.uploadAudioFile(file, title, duration)
            ResponseEntity.ok(uploadedFile)
        }catch (ex: FileSizeLimitExceededException) {
            ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body(mapOf("error" to "File size exceeds the maximum allowed limit."))
        } catch (ex: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(mapOf("error" to "An error occurred while uploading the file."))
        }
    }

    @GetMapping("/{id}")
    fun getAudioFileById(@PathVariable id: Long): ResponseEntity<Any> {
        return try {
            val audioFile = audioFileService.getAudioFileById(id)
            ResponseEntity.ok(audioFile)
        } catch (ex: FileNotFoundException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(mapOf("error" to "File not found: ${ex.message}"))
        } catch (ex: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(mapOf("error" to "An error occurred while retrieving the file."))
        }
    }

    @GetMapping
    fun getAllAudioFiles(): ResponseEntity<Any> {
        try {
            val audioFiles = audioFileService.getAllAudioFiles()
            return ResponseEntity.ok(audioFiles)
        } catch(ex: Exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(mapOf("error" to "An error occured while listing the files"))
        }
    }

    @DeleteMapping("/{id}")
    fun deleteAudiofile(@PathVariable("id") audiofileId: Long): ResponseEntity<Any> {
        return try {
            audioFileService.deleteAudioFile(audiofileId)
            ResponseEntity.noContent().build()
        } catch (ex: FileNotFoundException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(mapOf("error" to "File not found: ${ex.message}"))
        } catch (ex: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(mapOf("error" to "An error occurred while deleting the file."))
        }
    }
}
