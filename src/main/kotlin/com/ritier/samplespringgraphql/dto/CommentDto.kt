package com.ritier.samplespringgraphql.dto

import java.time.LocalDateTime

data class CommentDto(
    val id: Long = 0,
    val contents: String = "",
    val user: UserDto = UserDto(),
    val createdAt: LocalDateTime = LocalDateTime.now()
) {
}