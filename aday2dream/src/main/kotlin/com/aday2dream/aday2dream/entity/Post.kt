package com.aday2dream.aday2dream.entity

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "Post")
data class Post(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    val postId: Long = 0,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id", referencedColumnName = "account_id", nullable = false)
    val account: Account,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "audiofile_id", nullable = false)
    val audiofile: AudioFile,

    @Column(name = "post_title", nullable = false)
    val title: String,

    @Column(name = "post_description", nullable = false)
    val description: String,

    @Column(name = "background_image")
    val backgroundImage: String? = null,

    @Column(name = "hyperlinks")
    val hyperlinks: String? = null,

    @Column(name = "price", nullable = false)
    val price: BigDecimal,

    @Column(name = "created_at", updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at")
    var updatedAt: LocalDateTime = LocalDateTime.now()
)
