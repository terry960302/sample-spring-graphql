package com.ritier.samplespringgraphql.repository

import com.ritier.samplespringgraphql.entity.Image
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ImageRepository : JpaRepository<Image, Long>, JpaSpecificationExecutor<Image> {
    @Query("SELECT i.* FROM images i INNER JOIN posting_images pi ON i.id = pi.image_id WHERE pi.posting_id IN :ids", nativeQuery = true)
    fun getAllImagesByPostingIds(ids : List<Long>) : List<Image>

    @Query("SELECT i.* FROM images i LEFT JOIN users u ON u.profile_img_id = i.id WHERE u.id IN :ids", nativeQuery = true)
    fun getAllImagesByUserIds(ids : List<Long>) : List<Image>

}