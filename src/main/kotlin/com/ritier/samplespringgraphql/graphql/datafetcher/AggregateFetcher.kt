package com.ritier.samplespringgraphql.graphql.datafetcher

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsData
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment
import com.ritier.samplespringgraphql.DgsConstants
import com.ritier.samplespringgraphql.service.CommentService
import com.ritier.samplespringgraphql.service.ImageService
import com.ritier.samplespringgraphql.service.PostingService
import com.ritier.samplespringgraphql.types.Aggregate
import org.springframework.beans.factory.annotation.Autowired

@DgsComponent
class AggregateFetcher {

    @Autowired
    private lateinit var postingService: PostingService
    @Autowired
    private lateinit var imageService: ImageService
    @Autowired
    private lateinit var commentService : CommentService

    @DgsData(parentType = DgsConstants.QUERY.TYPE_NAME, field = DgsConstants.QUERY.AggregatePosting)
    fun countPostings(): Aggregate {
        return Aggregate(count = postingService.countPostings())
    }

    @DgsData(parentType = DgsConstants.QUERY.TYPE_NAME, field = DgsConstants.QUERY.AggregateImage)
    fun countImages(): Aggregate {
        return Aggregate(count = imageService.countImages())
    }

    @DgsData(parentType = DgsConstants.QUERY.TYPE_NAME, field = DgsConstants.QUERY.AggregateComment)
    fun countComments(): Aggregate {
        return Aggregate(count = commentService.countComments())
    }
}