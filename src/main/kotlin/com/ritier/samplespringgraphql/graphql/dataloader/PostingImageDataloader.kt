package com.ritier.samplespringgraphql.graphql.dataloader

import com.netflix.graphql.dgs.DgsDataLoader
import com.ritier.samplespringgraphql.entity.Image
import com.ritier.samplespringgraphql.service.ImageService
import org.dataloader.MappedBatchLoader
import org.springframework.beans.factory.annotation.Autowired
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage

@DgsDataLoader(name = "images", maxBatchSize = 100)
class PostingImageDataloader : MappedBatchLoader<Long, List<Image>>{
    @Autowired
    private lateinit var imageService : ImageService

    override fun load(keys: MutableSet<Long>?): CompletionStage<MutableMap<Long, List<Image>>> {
        return CompletableFuture.supplyAsync { imageService.getAllImagesByPostingIds(keys!!.stream().toList()) }

    }
}