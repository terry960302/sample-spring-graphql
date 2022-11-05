package com.ritier.samplespringgraphql.repository.specification

import com.ritier.samplespringgraphql.entity.Comment
import com.ritier.samplespringgraphql.entity.Image
import com.ritier.samplespringgraphql.entity.Posting
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Component
import javax.persistence.criteria.*

@Component
class PostingSpecifications{
    companion object{
        fun contentsLike(keyword : String) : Specification<Posting>{
            return Specification<Posting> { root, query, builder ->
                builder.like(root.get("contents"), "%${keyword}%")
            };
        }


        fun fetchComments() : Specification<Posting>{
            return Specification<Posting> { root, query, builder ->
                val fetch = root.fetch<Posting, Comment>("comments", JoinType.INNER)
                val join = fetch as Join<Posting, Comment>
                return@Specification join.on
            };
        }

        fun fetchImages() : Specification<Posting>{
            return Specification<Posting> { root, query, builder ->
                val fetch = root.fetch<Posting, Image>("images", JoinType.INNER)
                val join = fetch as Join<Posting, Image>
                return@Specification join.on
            };
        }
    }
}
