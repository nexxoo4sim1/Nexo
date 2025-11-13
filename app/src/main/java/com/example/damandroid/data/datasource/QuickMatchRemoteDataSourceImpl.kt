package com.example.damandroid.data.datasource

import com.example.damandroid.data.model.MatchUserProfileDto
import com.example.damandroid.data.model.SportDto

class QuickMatchRemoteDataSourceImpl : QuickMatchRemoteDataSource {
    override suspend fun fetchProfiles(): List<MatchUserProfileDto> {
        // Temporary stub implementation
        return emptyList()
    }

    companion object {
        fun mockProfile(id: String): MatchUserProfileDto = MatchUserProfileDto(
            id = id,
            name = "Alex Thompson",
            age = 28,
            avatarUrl = "https://api.dicebear.com/7.x/avataaars/svg?seed=$id",
            coverImageUrl = "https://picsum.photos/seed/$id/400/300",
            location = "Los Angeles, CA",
            distance = "5 km away",
            bio = "Outdoor enthusiast and fitness coach",
            sports = listOf(
                SportDto(name = "Running", icon = "🏃", level = "Advanced"),
                SportDto(name = "Swimming", icon = "🏊", level = "Intermediate")
            ),
            interests = listOf("Hiking", "Yoga", "Nutrition"),
            rating = 4.9,
            activitiesJoined = 24
        )
    }
}

