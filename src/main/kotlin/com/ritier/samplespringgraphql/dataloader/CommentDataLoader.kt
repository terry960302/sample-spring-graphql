package com.ritier.samplespringgraphql.dataloader

import com.netflix.graphql.dgs.DgsDataLoader
import com.ritier.samplespringgraphql.entity.Comment
import com.ritier.samplespringgraphql.service.CommentService
import org.dataloader.BatchLoader
import org.dataloader.MappedBatchLoader
import org.springframework.beans.factory.annotation.Autowired
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage

@DgsDataLoader(name = "comments")
class CommentDataLoader : MappedBatchLoader<Long, List<Comment>> {

    @Autowired
    private lateinit var commentStage: CommentService

    override fun load(keys: MutableSet<Long>?): CompletionStage<MutableMap<Long, List<Comment>>> {
        return CompletableFuture.supplyAsync { commentStage.getAllCommentsByPostingIds(keys!!.stream().toList()) }
    }
}