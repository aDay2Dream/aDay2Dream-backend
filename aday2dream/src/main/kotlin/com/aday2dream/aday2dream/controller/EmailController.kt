package com.aday2dream.aday2dream.controller

import com.aday2dream.aday2dream.service.EmailService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileOutputStream

@RestController
@RequestMapping("/email")
class EmailController(private val emailService: EmailService) {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(AccountController::class.java)
    }

    @PostMapping("/send")
    fun sendEmailWithAttachment(
        @RequestParam("to") to: String,
        @RequestParam("subject") subject: String,
        @RequestParam("text") text: String,
        @RequestParam("audioFile") audioFile: MultipartFile
    ): ResponseEntity<String> {

        val tempFile = File.createTempFile("audio", ".mp3")
        logger.info("Created temporary file")
        audioFile.inputStream.use { input ->
            FileOutputStream(tempFile).use { output ->
                input.copyTo(output)
            }
        }
        logger.info("Copied data from original to temporary file.")
        if (!tempFile.exists() || tempFile.length() == 0L) {
            logger.error("Failed to create temporary file.")
            return ResponseEntity.status(500).body("Failed to create temp file")
        }

        try {
            emailService.sendEmailWithAttachment(to, subject, text, tempFile.absolutePath)
            tempFile.delete()
            logger.info("Temporary file deleted.")
            logger.info("Email sent successfully.")
            return ResponseEntity.ok("Email sent successfully!")
        } catch (e: Exception) {
            tempFile.delete()
            logger.info("Temporary file deleted.")
            logger.info("Failed to send email: ${e.message}")
            return ResponseEntity.status(500).body("Failed to send email: ${e.message}")
        }
    }
}
