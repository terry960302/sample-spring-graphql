package com.ritier.samplespringgraphql.dto.common

//data class CountDto(val count : Int = 0) {
//}

interface CountDto{
    fun getCount() : Int
}