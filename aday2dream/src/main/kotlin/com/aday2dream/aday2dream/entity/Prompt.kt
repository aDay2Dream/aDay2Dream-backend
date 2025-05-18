package com.aday2dream.aday2dream.entity

import jakarta.persistence.*
import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor
import java.time.LocalDateTime

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "prompt")
data class Prompt(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prompt_id")
    val promptId: Long? = null,

    @ManyToOne
    @JoinColumn(name = "post_id", referencedColumnName = "post_id", nullable = false)
    val post: Post,

    @ManyToOne
    @JoinColumn(name = "buyer_id", referencedColumnName = "account_id", nullable = false)
    val buyer: Account,

    @Column(name = "prompt_title", nullable = false)
    val promptTitle: String,

    @Column(name = "prompt_description", nullable = false)
    val promptDescription: String,

    @Column(name = "prompt_links")
    val promptLinks: String? = null,

    @Column(name = "start_date")
    val startDate: LocalDateTime = LocalDateTime.now(),

    @Column(name = "end_date")
    val endDate: LocalDateTime? = null,

    @Column(name = "created_at", updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at")
    var updatedAt: LocalDateTime = LocalDateTime.now()
)
