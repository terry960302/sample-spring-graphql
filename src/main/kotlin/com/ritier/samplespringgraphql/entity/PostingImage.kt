package com.ritier.samplespringgraphql.entity

import javax.persistence.*

@Entity
@Table(name = "posting_images")
@IdClass(PostingImageId::class)
data class PostingImage(
    @Id
    @ManyToOne
    @JoinColumn(name = "posting_id")
    val posting: Posting,

    @Id
    @ManyToOne
    @JoinColumn(name = "image_id")
    val image: Image,

    ) {
}
