package com.ritier.samplespringgraphql.datafetcher

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsData
import com.netflix.graphql.dgs.InputArgument
import com.ritier.samplespringgraphql.DgsConstants
import com.ritier.samplespringgraphql.dataloader.ImageDataLoader
import com.ritier.samplespringgraphql.dto.PostingDto
import com.ritier.samplespringgraphql.entity.Posting
import com.ritier.samplespringgraphql.entity.User
import com.ritier.samplespringgraphql.service.PostingService
import com.ritier.samplespringgraphql.types.PaginationInput
import graphql.schema.DataFetchingEnvironment
import kotlinx.coroutines.runBlocking
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import java.util.Optional

@DgsComponent
class PostingFetcher {

    @Autowired
    private lateinit var postingService: PostingService

    @DgsData(parentType = DgsConstants.QUERY.TYPE_NAME, field = DgsConstants.QUERY.GetAllPostings)
    fun getAllPostings(dfe: DataFetchingEnvironment, @InputArgument input: Optional<PaginationInput>): List<Posting> {
        val selections = dfe.selectionSet
        selections.contains("totalCount")

        return if (input.isEmpty) {
            postingService.getAllPostings(null)
        } else {
            val pageable = PageRequest.of(input.get().page!!, input.get().size!!, Sort.Direction.ASC, "id")
            postingService.getAllPostings(pageable)
        }

    }

    @DgsData(parentType = DgsConstants.QUERY.TYPE_NAME, field = DgsConstants.QUERY.GetPosting)
    fun getPosting(@InputArgument id: Long): Optional<Posting> {
        return postingService.getPosting(id)
    }

    @DgsData(parentType = DgsConstants.MUTATION.TYPE_NAME, field = DgsConstants.MUTATION.CreateMockPostings)
    fun createMockPostings(): List<Long> = runBlocking {
        postingService.createMockPostings()
    }

}