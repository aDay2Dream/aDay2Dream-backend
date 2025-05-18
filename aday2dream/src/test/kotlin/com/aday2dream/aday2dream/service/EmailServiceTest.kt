package com.aday2dream.aday2dream.service

import jakarta.mail.internet.MimeMessage
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.mail.javamail.JavaMailSender
import java.io.File
import java.nio.charset.StandardCharsets

@ExtendWith(MockitoExtension::class)
class EmailServiceTest {

    @Mock
    private lateinit var mailSender: JavaMailSender

    private lateinit var emailService: EmailService

    private val senderEmail = "test@example.com"

    @BeforeEach
    fun setUp() {
            mailSender = mock(JavaMailSender::class.java)
            emailService = EmailService(mailSender, senderEmail)
    }

    @Test
    fun `should create zipped file successfully`() {

        val tempFile = File.createTempFile("test-audio", ".mp3")
        tempFile.writeText("Test audio content", StandardCharsets.UTF_8)

        val zippedFileBytes = emailService.createZippedFile(tempFile.absolutePath)

        assertNotNull(zippedFileBytes)
        assertTrue(zippedFileBytes.isNotEmpty())

        tempFile.delete()
    }

    @Test
    fun `should throw exception for invalid file path`() {
        val invalidFilePath = "non_existent_file.mp3"

        assertThrows<IllegalArgumentException> {
            emailService.createZippedFile(invalidFilePath)
        }
    }

    @Test
    fun `should send email with attachment`() {

        val mimeMessage = mock(MimeMessage::class.java)
        `when`(mailSender.createMimeMessage()).thenReturn(mimeMessage)

        val to = "receiver@example.com"
        val subject = "Test Subject"
        val text = "Test Email Content"
        val tempFile = File.createTempFile("test-audio", ".mp3")
        tempFile.writeText("Test audio content", StandardCharsets.UTF_8)

        val zippedFileBytes = emailService.createZippedFile(tempFile.absolutePath)

        emailService.sendEmailWithAttachment(to, subject, text, tempFile.absolutePath)

        verify(mailSender).send(mimeMessage)

        tempFile.delete()
    }
}
