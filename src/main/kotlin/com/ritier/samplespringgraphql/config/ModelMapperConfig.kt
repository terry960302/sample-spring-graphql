package com.ritier.samplespringgraphql.config

import org.modelmapper.ModelMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

// no-arg error can resolved by https://javachoi.tistory.com/444?category=1267711
@Configuration
class ModelMapperConfig {

    @Bean
    fun modelMapper(): ModelMapper {
        val modelMapper = ModelMapper()
        modelMapper.configuration.isFieldMatchingEnabled = true // 필드 이름이 같은 것끼리 매칭
        modelMapper.configuration.fieldAccessLevel = org.modelmapper.config.Configuration.AccessLevel.PRIVATE // private 필드여도 접근
        return modelMapper
    }
}