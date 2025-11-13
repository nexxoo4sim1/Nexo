package com.example.damandroid.domain.repository

import com.example.damandroid.domain.model.AchievementsOverview

interface AchievementsRepository {
    suspend fun getOverview(): AchievementsOverview
}

