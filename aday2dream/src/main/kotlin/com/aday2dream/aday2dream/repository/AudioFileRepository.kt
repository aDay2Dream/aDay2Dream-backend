package com.aday2dream.aday2dream.repository
import com.aday2dream.aday2dream.model.AudioFile
import org.springframework.data.jpa.repository.JpaRepository

interface AudioFileRepository : JpaRepository<AudioFile, Long>
