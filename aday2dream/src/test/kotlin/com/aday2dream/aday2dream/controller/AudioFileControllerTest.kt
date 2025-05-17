package com.aday2dream.aday2dream.controller

import com.aday2dream.aday2dream.dto.AudioFileDto
import com.aday2dream.aday2dream.service.AudioFileService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.http.HttpStatus
import org.springframework.mock.web.MockMultipartFile

@ExtendWith(MockitoExtension::class)
class AudioFileControllerTest {

    @Mock
    private lateinit var audioFileService: AudioFileService

    @InjectMocks
    private lateinit var audioFileController: AudioFileController

    @BeforeEach
    fun setUp() {
        audioFileController = AudioFileController(audioFileService)
    }

    @Test
    fun `should upload audio file successfully`() {
        val file = MockMultipartFile("file", "test.mp3", "audio/mpeg", "audio content".toByteArray())
        val dto = AudioFileDto(1L, "/path/test.mp3", "Test Title", 180)

        `when`(audioFileService.uploadAudioFile(file, "Test Title", 180)).thenReturn(dto)

        val response = audioFileController.uploadAudioFile(file, "Test Title", 180)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(dto, response.body)
    }

    @Test
    fun `should handle file size exceeded exception`() {
        val file = MockMultipartFile("file", "large.mp3", "audio/mpeg", ByteArray(1024 * 1024 * 10)) // Large file

        `when`(audioFileService.uploadAudioFile(file, "Large File", 180))
            .thenThrow(RuntimeException("File size exceeds the maximum allowed limit."))

        val response = audioFileController.uploadAudioFile(file, "Large File", 180)

        assertEquals(HttpStatus.PAYLOAD_TOO_LARGE, response.statusCode)
        val responseBody = response.body as Map<*, *>
        assertEquals("File exceeds maximum limit", responseBody["error"])
    }

    @Test
    fun `should retrieve audio file by ID`() {
        val dto = AudioFileDto(1L, "/path/test.mp3", "Test Title", 180)

        `when`(audioFileService.getAudioFileById(1L)).thenReturn(dto)

        val response = audioFileController.getAudioFileById(1L)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(dto, response.body)
    }

    @Test
    fun `should handle file not found exception`() {
        doThrow(RuntimeException("File not found")).`when`(audioFileService).getAudioFileById(99L)

        val response = audioFileController.getAudioFileById(99L)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        val responseBody = response.body as Map<*, *>
        assertEquals("File not found: File not found", responseBody["error"])
    }

    @Test
    fun `should delete audio file successfully`() {
        doNothing().`when`(audioFileService).deleteAudioFile(1L)

        val response = audioFileController.deleteAudiofile(1L)

        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
    }

    @Test
    fun `should handle file not found exception on delete`() {
        doThrow(RuntimeException("File not found")).`when`(audioFileService).deleteAudioFile(99L)

        val response = audioFileController.deleteAudiofile(99L)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        val responseBody = response.body as Map<*, *>
        assertEquals("File not found: File not found", responseBody["error"])
    }

    @Test
    fun `should return all audio files`() {
        val audioFiles = listOf(
            AudioFileDto(1L, "/file1", "Track1", 100),
            AudioFileDto(2L, "/file2", "Track2", 200)
        )

        `when`(audioFileService.getAllAudioFiles()).thenReturn(audioFiles)

        val response = audioFileController.getAllAudioFiles()

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(audioFiles, response.body)
    }
}
