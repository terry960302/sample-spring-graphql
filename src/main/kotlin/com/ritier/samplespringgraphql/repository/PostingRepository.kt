package com.ritier.samplespringgraphql.repository
import com.ritier.samplespringgraphql.entity.Posting
import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

@Repository
interface PostingRepository : JpaRepository<Posting, Long>, JpaSpecificationExecutor<Posting> {
}