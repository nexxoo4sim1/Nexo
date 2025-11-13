package com.example.damandroid.data.repository

import com.example.damandroid.data.datasource.AchievementsRemoteDataSource
import com.example.damandroid.data.mapper.toDomain
import com.example.damandroid.domain.model.AchievementsOverview
import com.example.damandroid.domain.repository.AchievementsRepository

class AchievementsRepositoryImpl(
    private val remoteDataSource: AchievementsRemoteDataSource
) : AchievementsRepository {
    override suspend fun getOverview(): AchievementsOverview =
        remoteDataSource.fetchOverview().toDomain()
}

