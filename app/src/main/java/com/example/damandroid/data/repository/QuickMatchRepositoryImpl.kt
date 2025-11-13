package com.example.damandroid.data.repository

import com.example.damandroid.data.datasource.QuickMatchRemoteDataSource
import com.example.damandroid.data.mapper.toDomain
import com.example.damandroid.domain.model.MatchUserProfile
import com.example.damandroid.domain.repository.QuickMatchRepository

class QuickMatchRepositoryImpl(
    private val remoteDataSource: QuickMatchRemoteDataSource
) : QuickMatchRepository {
    override suspend fun getProfiles(): List<MatchUserProfile> =
        remoteDataSource.fetchProfiles().map { it.toDomain() }
}

