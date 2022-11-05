package com.ritier.samplespringgraphql.entity

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
class PostingImageId(
    val posting: Long = 0,
    val image: Long = 0
) : Serializable {

}