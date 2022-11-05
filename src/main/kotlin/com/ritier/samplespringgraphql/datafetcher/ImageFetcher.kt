package com.ritier.samplespringgraphql.datafetcher

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsData
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment
import com.netflix.graphql.dgs.InputArgument
import com.ritier.samplespringgraphql.DgsConstants
import com.ritier.samplespringgraphql.dataloader.ImageDataLoader
import com.ritier.samplespringgraphql.dto.ImageDto
import com.ritier.samplespringgraphql.entity.Image
import com.ritier.samplespringgraphql.entity.Posting
import com.ritier.samplespringgraphql.entity.User
import com.ritier.samplespringgraphql.repository.ImageRepository
import com.ritier.samplespringgraphql.service.ImageService
import com.ritier.samplespringgraphql.types.ImageFilter
import graphql.schema.DataFetchingEnvironment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.dataloader.DataLoader
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import java.util.*
import java.util.concurrent.CompletableFuture

@DgsComponent
class ImageFetcher {
    @Autowired
    private lateinit var imageService: ImageService

    @DgsData(parentType = DgsConstants.QUERY.TYPE_NAME, field = DgsConstants.QUERY.GetAllImages)
    fun getAllImages(@InputArgument filter : ImageFilter?): List<Image> {

        return if(filter == null){
            imageService.getAllImages()
        }else{
            imageService.getAllFilteredImages(filter.width, filter.height)
        }

    }

    @DgsData(parentType = DgsConstants.POSTING.TYPE_NAME, field = DgsConstants.POSTING.Images)
    fun images(dfe: DgsDataFetchingEnvironment): CompletableFuture<List<Image>> {
        val imageDataLoader: DataLoader<Long, List<Image>> = dfe.getDataLoader(ImageDataLoader::class.java)
        val posting: Posting = dfe.getSource<Posting>()
        return imageDataLoader.load(posting.id)
    }

    @DgsData(parentType = DgsConstants.POSTING.TYPE_NAME, field = DgsConstants.USER.ProfileImg)
    fun profileImg(dfe: DataFetchingEnvironment): Optional<Image>? {
        val user = dfe.getSource<User>()
        if (user.profileImg?.id == null) {
            return null
        } else {
            val image: Optional<Image> = imageService.getImage(user.id)
            return image
        }

    }

    @DgsData(parentType = DgsConstants.MUTATION.TYPE_NAME, field = DgsConstants.MUTATION.CreateMockImages)
    fun createMockImages(): List<Long> = runBlocking {
        imageService.createMockImages()
    }
}