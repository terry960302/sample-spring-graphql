package com.ritier.samplespringgraphql.datafetcher

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsData
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment
import com.ritier.samplespringgraphql.DgsConstants
import com.ritier.samplespringgraphql.dataloader.CommentDataLoader
import com.ritier.samplespringgraphql.entity.Comment
import com.ritier.samplespringgraphql.entity.Posting
import com.ritier.samplespringgraphql.service.CommentService
import graphql.schema.DataFetchingEnvironment
import kotlinx.coroutines.runBlocking
import org.dataloader.DataLoader
import org.springframework.beans.factory.annotation.Autowired
import java.util.concurrent.CompletableFuture

@DgsComponent
class CommentFetcher {
    @Autowired
    private lateinit var commentService: CommentService

    @DgsData(parentType = DgsConstants.POSTING.TYPE_NAME, field = DgsConstants.POSTING.Comments)
    fun comments(dfe: DgsDataFetchingEnvironment): CompletableFuture<List<Comment>> {

        val commentDataLoader: DataLoader<Long, List<Comment>> = dfe.getDataLoader(CommentDataLoader::class.java)
        val posting = dfe.getSource<Posting>()

        return commentDataLoader.load(posting.id)
    }

    @DgsData(parentType = DgsConstants.MUTATION.TYPE_NAME, field = DgsConstants.MUTATION.CreateMockComments)
    fun createMockComments() = runBlocking {
        commentService.createMockComments()
    }
}