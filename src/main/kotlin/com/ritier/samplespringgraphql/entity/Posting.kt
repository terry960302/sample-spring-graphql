package com.ritier.samplespringgraphql.entity

import java.time.LocalDateTime
import javax.persistence.*

// https://ict-nroo.tistory.com/127
@Entity
@Table(name = "postings")
data class Posting(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id") val id: Long = 0,

    @Column(name = "contents", length = 1000)
    val contents: String,

    @GeneratedValue
    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @ManyToOne(targetEntity = User::class)
    @JoinColumn(name = "user_id")
    val user: User,

    @OneToMany(mappedBy = "posting", fetch = FetchType.LAZY)
    val comments: List<Comment>? = null,

    @OneToMany(
        mappedBy = "posting",
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    val postingImages: List<PostingImage>? = null,
) {
}

