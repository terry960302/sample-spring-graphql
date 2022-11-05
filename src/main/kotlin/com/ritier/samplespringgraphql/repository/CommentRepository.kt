package com.ritier.samplespringgraphql.repository

import com.ritier.samplespringgraphql.entity.Comment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface CommentRepository : JpaRepository<Comment, Long>{
    @Query("SELECT * FROM comments WHERE posting_id IN :ids", nativeQuery = true)
    fun getAllCommentsByPostingIds(ids : List<Long>) : List<Comment>
}