package com.example.damandroid.data.datasource

import com.example.damandroid.data.model.AchievementsOverviewDto

interface AchievementsRemoteDataSource {
    suspend fun fetchOverview(): AchievementsOverviewDto
}

