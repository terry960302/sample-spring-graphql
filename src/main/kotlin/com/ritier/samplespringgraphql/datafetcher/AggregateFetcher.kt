package com.ritier.samplespringgraphql.datafetcher

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsData
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment
import com.ritier.samplespringgraphql.DgsConstants
import com.ritier.samplespringgraphql.service.PostingService
import com.ritier.samplespringgraphql.types.Aggregate
import org.springframework.beans.factory.annotation.Autowired

@DgsComponent
class AggregateFetcher {

    @Autowired
    private lateinit var postingService: PostingService

    @DgsData(parentType = DgsConstants.QUERY.TYPE_NAME, field = DgsConstants.QUERY.AggregatePosting)
    fun countPostings(dfe: DgsDataFetchingEnvironment): Aggregate {
        return Aggregate(count = postingService.countPostings())
    }
}