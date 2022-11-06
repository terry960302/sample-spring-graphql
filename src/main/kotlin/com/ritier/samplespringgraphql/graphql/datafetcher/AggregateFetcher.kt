package com.ritier.samplespringgraphql.graphql.datafetcher

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsData
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment
import com.ritier.samplespringgraphql.DgsConstants
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

    @DgsData(parentType = DgsConstants.QUERY.TYPE_NAME, field = DgsConstants.QUERY.AggregatePosting)
    fun countPostings(dfe: DgsDataFetchingEnvironment): Aggregate {
        return Aggregate(count = postingService.countPostings())
    }

    @DgsData(parentType = DgsConstants.QUERY.TYPE_NAME, field = DgsConstants.QUERY.AggregateImage)
    fun countImages(dfe: DgsDataFetchingEnvironment): Aggregate {
        return Aggregate(count = imageService.countImages())
    }
}