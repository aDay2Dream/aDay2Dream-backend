package com.aday2dream.aday2dream.service

import com.aday2dream.aday2dream.controller.AccountController
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ByteArrayResource
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream

@Service
class EmailService(private val mailSender: JavaMailSender,
@Value("\${spring.mail.username}") private val senderEmail: String
) {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(AccountController::class.java)
    }


    fun sendEmailWithAttachment(
        to: String,
        subject: String,
        text: String,
        audioFilePath: String
    ) {
        val mimeMessage = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(mimeMessage, true)

        helper.setFrom(senderEmail)
        helper.setTo(to)
        helper.setSubject(subject)
        helper.setText(text)
        logger.info("Mime message: ${helper.toString()}")
        val zippedFile = createZippedFile(audioFilePath)

        helper.addAttachment("audio.zip", ByteArrayResource(zippedFile), "application/zip")
        logger.info("Add attachment.")
        mailSender.send(mimeMessage)
        logger.info("Email sent.")
    }

    private fun createZippedFile(audioFilePath: String): ByteArray {
        val byteArrayOutputStream = ByteArrayOutputStream()
        ZipArchiveOutputStream(byteArrayOutputStream).use { zipOut ->
            val audioFile = File(audioFilePath)

            if (!audioFile.exists() || !audioFile.isFile) {
                logger.info("Invalid audio file path: $audioFilePath")
                throw IllegalArgumentException("Invalid audio file path: $audioFilePath")
            }

            val zipEntry = ZipArchiveEntry(audioFile, audioFile.name)
            zipOut.putArchiveEntry(zipEntry)
            logger.info("Zip entry created.")
            FileInputStream(audioFile).use { inputStream ->
                inputStream.copyTo(zipOut)
            }
            logger.info("Copied input stream...")
            zipOut.closeArchiveEntry()
            logger.info("Closed archive entry.")
        }
        logger.info("Returning byte array...")
        return byteArrayOutputStream.toByteArray()
    }
}
