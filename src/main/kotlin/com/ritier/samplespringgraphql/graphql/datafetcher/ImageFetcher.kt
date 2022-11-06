package com.ritier.samplespringgraphql.graphql.datafetcher

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsData
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment
import com.netflix.graphql.dgs.InputArgument
import com.ritier.samplespringgraphql.DgsConstants
import com.ritier.samplespringgraphql.graphql.dataloader.PostingImageDataloader
//import com.ritier.samplespringgraphql.dataloader.ProfileImageDataLoader
import com.ritier.samplespringgraphql.entity.Image
import com.ritier.samplespringgraphql.entity.Posting
import com.ritier.samplespringgraphql.service.ImageService
import com.ritier.samplespringgraphql.types.ImageFilter
import kotlinx.coroutines.runBlocking
import org.dataloader.DataLoader
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.annotation.Secured
import org.springframework.security.access.prepost.PreAuthorize
import java.util.concurrent.CompletableFuture

@DgsComponent
class ImageFetcher {
    @Autowired
    private lateinit var imageService: ImageService

    @DgsData(parentType = DgsConstants.QUERY.TYPE_NAME, field = DgsConstants.QUERY.GetAllImages)
    fun getAllImages(@InputArgument filter: ImageFilter?): List<Image> {

        return if (filter == null) {
            imageService.getAllImages()
        } else {
            imageService.getAllFilteredImages(filter.width, filter.height)
        }

    }

    @DgsData(parentType = DgsConstants.POSTING.TYPE_NAME, field = DgsConstants.POSTING.Images)
    fun images(dfe: DgsDataFetchingEnvironment): CompletableFuture<List<Image>> {
        val imageDataLoader: DataLoader<Long, List<Image>> = dfe.getDataLoader(PostingImageDataloader::class.java)
        val posting: Posting = dfe.getSource()
        return imageDataLoader.load(posting.id)
    }

    @DgsData(parentType = DgsConstants.MUTATION.TYPE_NAME, field = DgsConstants.MUTATION.CreateMockImages)
    fun createMockImages(): List<Long> = runBlocking {
        imageService.createMockImages()
    }
}