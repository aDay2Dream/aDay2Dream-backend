package com.aday2dream.aday2dream.controller

import com.aday2dream.aday2dream.service.EmailService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.http.HttpStatus
import org.springframework.mock.web.MockMultipartFile

@ExtendWith(MockitoExtension::class)
class EmailControllerTest {

    @Mock
    private lateinit var emailService: EmailService

    @InjectMocks
    private lateinit var emailController: EmailController

    @Test
    fun `should send email with attachment successfully`() {
        val file = MockMultipartFile("audioFile", "test.mp3", "audio/mpeg", "audio content".toByteArray())

        `when`(emailService.sendEmailWithAttachment(anyString(), anyString(), anyString(), anyString()))
            .thenAnswer {}

        val response = emailController.sendEmailWithAttachment(
            "test@example.com", "Subject", "Message", file
        )

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals("Email sent successfully!", response.body)
    }

    @Test
    fun `should return error when file is invalid`() {
        val file = MockMultipartFile("audioFile", "empty.mp3", "audio/mpeg", ByteArray(0))

        val response = emailController.sendEmailWithAttachment(
            "test@example.com", "Subject", "Message", file
        )

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.statusCode)
        assertEquals("Failed to create temp file", response.body)
    }

    @Test
    fun `should handle exception when sending email fails`() {
        val file = MockMultipartFile("audioFile", "test.mp3", "audio/mpeg", "audio content".toByteArray())

        doThrow(RuntimeException("SMTP Error")).`when`(emailService)
            .sendEmailWithAttachment(anyString(), anyString(), anyString(), anyString())

        val response = emailController.sendEmailWithAttachment(
            "test@example.com", "Subject", "Message", file
        )

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.statusCode)
        assertEquals("Failed to send email: SMTP Error", response.body)
    }
}
