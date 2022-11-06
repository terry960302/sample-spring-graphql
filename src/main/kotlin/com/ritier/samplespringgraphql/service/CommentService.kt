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

    fun getAllCommentsByPostingIds(ids: List<Long>): MutableMap<Long, List<Comment>> {
        print("getAllCommentsByPostingIds@@@@@@@@@@@")
        val comments = commentRepository.getAllCommentsByPostingIds(ids)
        print(comments.map { it.id })
        return comments.groupBy { it.posting.id }.toMutableMap()
    }

    suspend fun createMockComments() = withContext(Dispatchers.IO) {
        val asyncComments = (1..100).map {
            val charset = "ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz0123456789"
            val contents = (1..100)
                .map { charset.random() }
                .joinToString("")
            val comment = Comment(
                user = entityManager.getReference(User::class.java, (1..4).random().toLong()),
                contents = contents,
                posting = entityManager.getReference(Posting::class.java, (1..50).random().toLong())
            )
            async { commentRepository.save(comment) }
        }
        val comments = asyncComments.awaitAll()
        comments.map { it.id }
    }
}