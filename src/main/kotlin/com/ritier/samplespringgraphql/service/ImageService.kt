package com.ritier.samplespringgraphql.service

import com.ritier.samplespringgraphql.dto.ImageOnlyUrlDto
import com.ritier.samplespringgraphql.entity.Image
import com.ritier.samplespringgraphql.entity.PostingImage
import com.ritier.samplespringgraphql.repository.ImageRepository
import com.ritier.samplespringgraphql.repository.PostingImageRepository
import com.ritier.samplespringgraphql.repository.specification.ImageSpecifications
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.Optional

@Service
class ImageService {
    @Autowired
    private lateinit var imageRepository: ImageRepository

    @Autowired
    private lateinit var postingImageRepository: PostingImageRepository

    companion object {
        //        val mockImageUrls = arrayListOf<String>(
//            "https://helpx.adobe.com/content/dam/help/en/photoshop/using/convert-color-image-black-white/jcr_content/main-pars/before_and_after/image-before/Landscape-Color.jpg",
//            "https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885__480.jpg",
//            "https://images.ctfassets.net/hrltx12pl8hq/a2hkMAaruSQ8haQZ4rBL9/8ff4a6f289b9ca3f4e6474f29793a74a/nature-image-for-website.jpg?fit=fill&w=480&h=320",
//            "https://img.freepik.com/premium-photo/astronaut-outer-open-space-planet-earth-stars-provide-background-erforming-space-planet-earth-sunrise-sunset-our-home-iss-elements-this-image-furnished-by-nasa_150455-16829.jpg?w=2000"
//        )
        const val MOCK_IMG_URL_PREFIX = "https://picsum.photos/200/300?random="
    }

    fun getImage(id: Long): Optional<Image> {
        return imageRepository.findById(id)
    }

    fun getAllImages(): List<Image> {
        return imageRepository.findAll()
    }

    fun getAllFilteredImages(width: Int?, height: Int?): List<Image> {
        if (width != null && height == null) {
            val spec = ImageSpecifications.filterWidth(width)
            return imageRepository.findAll(spec)
        }
        if (width == null && height != null) {
            val spec = ImageSpecifications.filterHeight(height)
            return imageRepository.findAll(spec)
        }
        if (width != null && height != null) {
            val spec = ImageSpecifications.filterWidth(width).and(ImageSpecifications.filterHeight(height))
            return imageRepository.findAll(spec)
        }else{
            return imageRepository.findAll()
        }

    }

    @Transactional
    fun getAllImagesByPostingIds(ids: List<Long>): MutableMap<Long, List<Image>> {
        val postingImgs: List<PostingImage> = postingImageRepository.getAllPostingImgsByPostingIds(ids)
        return postingImgs.groupBy { pi -> pi.posting.id }.map { it.key to it.value.map { pi -> pi.image } }.toMap()
            .toMutableMap()
    }

    fun createImage(image: Image) = imageRepository.save(image)

    fun countImages(): Int = imageRepository.countImages().getCount()

    fun getAllImageUrls(): List<ImageOnlyUrlDto> {
        val images = imageRepository.findAllOnlyUrlBy()

//        println(images)
//        return images.map {
//             mapOf("url" to it.getUrl(), "id" to it.getId())
//        }
        return images
    }

    suspend fun createMockImages() = withContext(Dispatchers.IO) {
        val mockImageUrls = arrayListOf<String>()
        for (i in 1..1000) {
            val url = MOCK_IMG_URL_PREFIX + i.toString()
            mockImageUrls.add(url)
        }
        val imagesAsync = mockImageUrls.map {
            val img = Image(url = it, width = 200, height = 300)
            async { imageRepository.saveAndFlush(img) }
        }
        val images = imagesAsync.awaitAll()
        images.map { it.id }
    }
}