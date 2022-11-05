package com.ritier.samplespringgraphql.dto

data class UserDto(val id: Long = 0, val nickname: String = "", val age: Int = 0, val image: ImageDto? = null) { // 필드명이 같아서 mapping을 수월하게 처리 가능
}