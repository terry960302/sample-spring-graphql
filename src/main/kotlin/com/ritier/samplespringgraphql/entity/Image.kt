package com.ritier.samplespringgraphql.entity

import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name="images")
data class Image(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id") val id: Long = 0,

    @Column(name="url") val url: String,

    @Column(name="width") val width: Int,

    @Column(name="height") val height: Int,

    @Column(name="created_at") val createdAt: LocalDateTime = LocalDateTime.now(),

    @OneToOne(mappedBy = "profileImg", cascade = [CascadeType.ALL], orphanRemoval = true)
    val user: User? = null,

    @OneToMany(
        mappedBy = "image",
        cascade = [CascadeType.ALL],
        orphanRemoval = true)
    val postingImages : List<PostingImage>? = null,
) {
    override fun toString(): String {
        return "Image { id : $id, url : $url, created_at : $createdAt}"
    }
}
