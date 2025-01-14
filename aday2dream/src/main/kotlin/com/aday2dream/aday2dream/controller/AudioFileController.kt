package com.aday2dream.aday2dream.controller
import com.aday2dream.aday2dream.dto.AudioFileDTO
import com.aday2dream.aday2dream.mapper.AudioFileMapper
import com.aday2dream.aday2dream.model.AudioFile
import com.aday2dream.aday2dream.service.AudioFileService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/audiofiles")
class AudioFileController(
    private val audioFileService: AudioFileService,
    private val audioFileMapper: AudioFileMapper
) {
    @PostMapping("/upload")
    fun uploadAudioFile(
        @RequestParam("file") file: MultipartFile,
        @RequestParam("title") title: String,
        @RequestParam("duration") duration: Int
    ): ResponseEntity<AudioFileDTO> {
        val uploadedFile = audioFileService.uploadAudioFile(file, title, duration)
        return ResponseEntity.ok(audioFileMapper.toDTO(uploadedFile))
    }

    @GetMapping("/{id}")
    fun getAudioFileById(@PathVariable id: Long): ResponseEntity<AudioFileDTO> {
        val audioFile = audioFileService.getAudioFileById(id)
        return ResponseEntity.ok(audioFileMapper.toDTO(audioFile))
    }

    @GetMapping
    fun getAllAudioFiles(): ResponseEntity<List<AudioFileDTO>> {
        val audioFiles = audioFileService.getAllAudioFiles()
        val audioFileDTOs = audioFiles.map { audioFileMapper.toDTO(it) }
        return ResponseEntity.ok(audioFileDTOs)
    }

    @DeleteMapping("/{id}")
    fun deleteAudiofile(@PathVariable("id") audiofileId: Long): ResponseEntity<Void> {
        audioFileService.deleteAudioFile(audiofileId)
        return ResponseEntity.noContent().build()
    }
}
