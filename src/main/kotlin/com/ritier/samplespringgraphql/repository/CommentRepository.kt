package com.ritier.samplespringgraphql.repository

import com.ritier.samplespringgraphql.entity.Comment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CommentRepository : JpaRepository<Comment, Long>{
}