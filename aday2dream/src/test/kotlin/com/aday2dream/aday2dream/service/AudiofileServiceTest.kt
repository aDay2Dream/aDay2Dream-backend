package com.aday2dream.aday2dream.service

import com.aday2dream.aday2dream.dto.AudioFileDto
import com.aday2dream.aday2dream.entity.AudioFile
import com.aday2dream.aday2dream.mapper.AudioFileMapper
import com.aday2dream.aday2dream.repository.AudioFileRepository
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever
import org.springframework.mock.web.MockMultipartFile
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*

@ExtendWith(MockitoExtension::class)
class AudioFileServiceTest {

    @Mock
    private lateinit var repository: AudioFileRepository

    @Mock
    private lateinit var mapper: AudioFileMapper

    private lateinit var service: AudioFileService
    private lateinit var tempDir: Path

    @BeforeEach
    fun setUp() {
        tempDir = Files.createTempDirectory("test_audio")
        service = AudioFileService(repository, mapper, tempDir.toString())
    }

    @Test
    fun `should upload audio file`() {
        val file = MockMultipartFile("file", "test.mp3", "audio/mpeg", "audio content".toByteArray())

        val savedEntity = AudioFile(1L, tempDir.resolve("test.mp3").toString(), "Test Title", 180)
        val dto = AudioFileDto(1L, savedEntity.uri, savedEntity.title, savedEntity.duration)

        whenever(repository.save(checkNotNull(any()))).thenReturn(savedEntity)
        whenever(mapper.toDto(any<AudioFile>())).thenReturn(dto)

        val result = service.uploadAudioFile(file, "Test Title", 180)

        assert(result.title == "Test Title")
        assert(Files.exists(Paths.get(result.uri)))
    }

    @Test
    fun `should throw exception when file is empty`() {
        val file = MockMultipartFile("file", "empty.mp3", "audio/mpeg", ByteArray(0))

        val exception = assertThrows<IllegalArgumentException> {
            service.uploadAudioFile(file, "Empty", 0)
        }

        assert(exception.message == "File cannot be empty")
    }

    @Test
    fun `should return audio file by id`() {
        val entity = AudioFile(1L, "/path/test.mp3", "Test", 120)
        val dto = AudioFileDto(1L, "/path/test.mp3", "Test", 120)

        `when`(repository.findById(1L)).thenReturn(Optional.of(entity))
        `when`(mapper.toDto(entity)).thenReturn(dto)

        val result = service.getAudioFileById(1L)

        assert(result.audiofileId == 1L)
        assert(result.title == "Test")
    }

    @Test
    fun `should throw exception when audio file not found`() {
        `when`(repository.findById(99L)).thenReturn(Optional.empty())

        val exception = assertThrows<RuntimeException> {
            service.getAudioFileById(99L)
        }

        assert(exception.message == "AudioFile not found")
    }

    @Test
    fun `should delete audio file if it exists`() {

        val tempFile = tempDir.resolve("to_delete.mp3").toFile()
        tempFile.writeText("dummy audio")

        val entity = AudioFile(1L, tempFile.absolutePath, "ToDelete", 100)
        val dto = AudioFileDto(1L, tempFile.absolutePath, "ToDelete", 100)

        `when`(repository.findById(1L)).thenReturn(Optional.of(entity))
        `when`(mapper.toDto(entity)).thenReturn(dto)
        doNothing().`when`(repository).deleteById(1L)

        service.deleteAudioFile(1L)

        assert(!tempFile.exists())
        verify(repository).deleteById(1L)
    }

    @Test
    fun `should return all audio files`() {
        val entityList = listOf(
            AudioFile(1L, "/file1", "Track1", 100),
            AudioFile(2L, "/file2", "Track2", 200)
        )
        val dtoList = listOf(
            AudioFileDto(1L, "/file1", "Track1", 100),
            AudioFileDto(2L, "/file2", "Track2", 200)
        )

        `when`(repository.findAll()).thenReturn(entityList)
        `when`(mapper.toDtoList(entityList)).thenReturn(dtoList)

        val result = service.getAllAudioFiles()

        assert(result.size == 2)
        assert(result[0].title == "Track1")
        assert(result[1].title == "Track2")
    }
}
