package com.ritier.samplespringgraphql.repository

import com.ritier.samplespringgraphql.entity.Image
import com.ritier.samplespringgraphql.entity.PostingImage
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(readOnly = true)
interface PostingImageRepository : JpaRepository<PostingImage, Long> {
    @Query("SELECT * FROM posting_images pi WHERE pi.posting_id IN :ids", nativeQuery = true)
    fun getAllPostingImgsByPostingIds(ids : List<Long>) : List<PostingImage>
}