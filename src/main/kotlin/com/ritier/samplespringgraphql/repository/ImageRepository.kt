package com.ritier.samplespringgraphql.repository

import com.ritier.samplespringgraphql.dto.ImageOnlyUrlDto
import com.ritier.samplespringgraphql.dto.common.CountDto
import com.ritier.samplespringgraphql.entity.Image
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ImageRepository : JpaRepository<Image, Long>, JpaSpecificationExecutor<Image> {
    @Query("SELECT COUNT(*) as count FROM images", nativeQuery = true)
    fun countImages() : CountDto

    // 리스트형 projection 적용하기 위함
    // 'By'라는 철자가 함수에 포함되어야함(보통 부분만 가져오는 경우는 ById나 ByContents 등 특정 필드에 where문 사용하는 경우가 대부분이어서 그러함)
    // https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#projections
    fun findAllOnlyUrlBy() : List<ImageOnlyUrlDto> // 컬럼 중 부분만 가져오기 위해선 projection 사용(쿼리상 실제로 불러오는 데이터양이 줄음)

    @Query("SELECT i.* FROM images i INNER JOIN posting_images pi ON i.id = pi.image_id WHERE pi.posting_id IN :ids", nativeQuery = true)
    fun getAllImagesByPostingIds(ids : List<Long>) : List<Image>

    @Query("SELECT i.* FROM images i LEFT JOIN users u ON u.profile_img_id = i.id WHERE u.id IN :ids", nativeQuery = true)
    fun getAllImagesByUserIds(ids : List<Long>) : List<Image>

}