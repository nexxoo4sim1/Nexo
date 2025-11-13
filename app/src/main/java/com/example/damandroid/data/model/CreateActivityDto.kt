package com.example.damandroid.data.model

data class CreateActivityCategoryDto(
    val id: String,
    val name: String,
    val icon: String
)

data class CreateActivityRequestDto(
    val sportCategoryId: String,
    val title: String,
    val description: String,
    val location: String,
    val date: String,
    val time: String,
    val participants: Int,
    val level: String,
    val visibility: String
)

data class CreateActivityResponseDto(
    val activityId: String,
    val shareLink: String
)

