package com.ritier.samplespringgraphql.dto

data class PostingDto(
    val id : Long = 0,
    val contents : String = "",
    val user : UserDto = UserDto(),
    val images : List<ImageDto> = arrayListOf(),
    val comments : List<CommentDto> = arrayListOf(),
) {
}