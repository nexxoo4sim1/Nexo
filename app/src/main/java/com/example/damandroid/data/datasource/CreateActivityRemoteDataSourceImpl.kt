package com.example.damandroid.data.datasource

import com.example.damandroid.data.model.CreateActivityRequestDto
import com.example.damandroid.data.model.CreateActivityResponseDto
import com.example.damandroid.data.model.SportCategoryDto
import java.util.UUID

class CreateActivityRemoteDataSourceImpl : CreateActivityRemoteDataSource {
    override suspend fun fetchSportCategories(): List<SportCategoryDto> = listOf(
        SportCategoryDto(id = "running", name = "Running", icon = "🏃"),
        SportCategoryDto(id = "swimming", name = "Swimming", icon = "🏊"),
        SportCategoryDto(id = "cycling", name = "Cycling", icon = "🚴"),
        SportCategoryDto(id = "yoga", name = "Yoga", icon = "🧘"),
        SportCategoryDto(id = "basketball", name = "Basketball", icon = "🏀"),
        SportCategoryDto(id = "dance", name = "Dance", icon = "💃")
    )

    override suspend fun createActivity(request: CreateActivityRequestDto): CreateActivityResponseDto {
        // Mocked response simulating backend creation
        val id = UUID.randomUUID().toString()
        return CreateActivityResponseDto(
            activityId = id,
            shareLink = "https://app.damandroid.com/activities/$id"
        )
    }
}

