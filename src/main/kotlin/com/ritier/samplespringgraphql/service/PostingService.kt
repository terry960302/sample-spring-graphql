package com.ritier.samplespringgraphql.service

import com.ritier.samplespringgraphql.entity.*
import com.ritier.samplespringgraphql.repository.PostingImageRepository
import com.ritier.samplespringgraphql.repository.PostingRepository
import com.ritier.samplespringgraphql.repository.specification.PostingSpecifications
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.Optional
import javax.persistence.EntityManager
import org.springframework.data.domain.Pageable

// https://spring.io/blog/2011/04/26/advanced-spring-data-jpa-specifications-and-querydsl/ <- 복잡쿼리 실행용
@Service
class PostingService {
    @Autowired
    private lateinit var postingRepository: PostingRepository

    @Autowired
    private lateinit var postingImageRepository: PostingImageRepository

    @Autowired
    private lateinit var modelMapper: ModelMapper

    @Autowired
    private lateinit var entityManager: EntityManager

    fun countPostings() : Int{
        return postingRepository.count().toInt()
    }

    fun getAllPostings(pageable : Pageable?): List<Posting> {
        return if (pageable == null){
            postingRepository.findAll()
        }else{
            val page =  postingRepository.findAll(pageable)
            page.get().toList()
        }

    }

    fun getPosting(id: Long): Optional<Posting> = postingRepository.findById(id)

    @Transactional
    suspend fun createMockPostings(): List<Long> = withContext(Dispatchers.IO) {
        // posting
        val asyncPostings = (1..50).map {
            val charset = "ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz0123456789"
            val contents = (1..100)
                .map { charset.random() }
                .joinToString("")
            val user = entityManager.getReference(User::class.java, (1..4).random().toLong())
            val posting = Posting(
                contents = contents,
                user = user,
                postingImages = arrayListOf()
            )
            async { postingRepository.save(posting) }
        }
        val postings = asyncPostings.awaitAll()


        // postingImage (posting 개별당 500개의 이미지 생성)
        val postingIds = postings.map { it.id }
        postingIds.map {
            val asyncPostingImgs = postingIds.map { postingId->
                val postingImage = PostingImage(
                    posting = entityManager.getReference(Posting::class.java, postingId),
                    image = entityManager.getReference(Image::class.java, (1..1000).random().toLong()) // 이미지는 랜덤으로 배정
                )
                async { postingImageRepository.save(postingImage) }
            }

            asyncPostingImgs.awaitAll()
        }

        postingIds
    }
}