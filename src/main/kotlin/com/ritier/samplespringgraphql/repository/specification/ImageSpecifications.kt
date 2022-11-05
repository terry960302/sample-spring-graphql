package com.ritier.samplespringgraphql.repository.specification

import com.ritier.samplespringgraphql.entity.Image
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Component

@Component
class ImageSpecifications {
    companion object{

        fun filterWidth(width : Int) : Specification<Image> {
            return Specification<Image> { root, query, builder ->
                builder.equal(root.get<Int>("width"), width)
            }
        }
        fun filterHeight(height : Int) : Specification<Image> {
            return Specification<Image> { root, query, builder ->
                builder.equal(root.get<Int>("height"), height)
            }
        }
    }
}