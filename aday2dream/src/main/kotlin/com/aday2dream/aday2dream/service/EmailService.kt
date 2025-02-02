package com.aday2dream.aday2dream.service

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream
import org.springframework.core.io.ByteArrayResource
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream

@Service
class EmailService(private val mailSender: JavaMailSender) {

    fun sendEmailWithAttachment(
        to: String,
        subject: String,
        text: String,
        audioFilePath: String
    ) {
        val mimeMessage = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(mimeMessage, true)

        helper.setFrom("aday2dreamapp@gmail.com")
        helper.setTo(to)
        helper.setSubject(subject)
        helper.setText(text)

        val zippedFile = createZippedFile(audioFilePath)

        helper.addAttachment("audio.zip", ByteArrayResource(zippedFile), "application/zip")

        mailSender.send(mimeMessage)
    }

    private fun createZippedFile(audioFilePath: String): ByteArray {
        val byteArrayOutputStream = ByteArrayOutputStream()
        ZipArchiveOutputStream(byteArrayOutputStream).use { zipOut ->
            val audioFile = File(audioFilePath)

            if (!audioFile.exists() || !audioFile.isFile) {
                throw IllegalArgumentException("Invalid audio file path: $audioFilePath")
            }

            val zipEntry = ZipArchiveEntry(audioFile, audioFile.name)
            zipOut.putArchiveEntry(zipEntry)

            FileInputStream(audioFile).use { inputStream ->
                inputStream.copyTo(zipOut)
            }

            zipOut.closeArchiveEntry()
        }

        return byteArrayOutputStream.toByteArray()
    }
}
