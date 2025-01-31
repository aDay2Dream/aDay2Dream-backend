package com.aday2dream.aday2dream.controller

import com.aday2dream.aday2dream.service.EmailService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.nio.file.Files

@RestController
@RequestMapping("/email")
class EmailController(private val emailService: EmailService) {

    @PostMapping("/send")
    fun sendEmailWithAttachment(
        @RequestParam("to") to: String,
        @RequestParam("subject") subject: String,
        @RequestParam("text") text: String,
        @RequestParam("audioFile") audioFile: MultipartFile
    ): ResponseEntity<String> {

        val tempFile = File.createTempFile("audio", ".mp3")
        audioFile.inputStream.use { input ->
            Files.copy(input, tempFile.toPath())
        }

        return try {
            emailService.sendEmailWithAttachment(to, subject, text, tempFile.absolutePath)
            tempFile.delete()
            ResponseEntity.ok("Email sent successfully!")
        } catch (e: Exception) {
            tempFile.delete()
            ResponseEntity.status(500).body("Failed to send email: ${e.message}")
        }
    }
}
