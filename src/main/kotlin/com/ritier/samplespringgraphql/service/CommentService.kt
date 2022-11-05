package com.ritier.samplespringgraphql.service

import com.ritier.samplespringgraphql.entity.Comment
import com.ritier.samplespringgraphql.entity.Posting
import com.ritier.samplespringgraphql.entity.User
import com.ritier.samplespringgraphql.repository.CommentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.persistence.EntityManager

@Service
class CommentService {
    @Autowired
    private lateinit var commentRepository: CommentRepository

    @Autowired
    private lateinit var entityManager: EntityManager

    fun createComment(comment: Comment) = commentRepository.save(comment)

    suspend fun createMockComments() = withContext(Dispatchers.IO) {
        val comment1 =
            Comment(
                user = entityManager.getReference(User::class.java, 1.toLong()),
                contents = "alskdjalskdj",
                posting = entityManager.getReference(Posting::class.java, 1.toLong())
            )
        val comment2 =
            Comment(
                user = entityManager.getReference(User::class.java, 2.toLong()),
                contents = "미ㅓㄴㅁㅇㄴ",
                posting = entityManager.getReference(Posting::class.java, 2.toLong())
            )
        val comment3 =
            Comment(
                user = entityManager.getReference(User::class.java, 3.toLong()),
                contents = "ㅁ니ㅓㅏ머너애ㅑㅓㅁㄴㅇ",
                posting = entityManager.getReference(Posting::class.java, 3.toLong())
            )
        val comment4 =
            Comment(
                user = entityManager.getReference(User::class.java, 4.toLong()),
                contents = "ㅔㅐㅔㅑㅂㅈㅇㅁ;ㅏㄴㅇㅁㄴㅁㄴㅇㅁㄴㅇ213123",
                posting = entityManager.getReference(Posting::class.java, 4.toLong())
            )

        val mockComments = arrayListOf(comment1, comment2, comment3, comment4)

        val commentsAsync = mockComments.map {
            async { commentRepository.save(it) }
        }
        val comments = commentsAsync.awaitAll()
        comments.map { it.id }
    }
}