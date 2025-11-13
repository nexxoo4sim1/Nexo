package com.example.damandroid.data.datasource

import com.example.damandroid.data.model.MatchUserProfileDto

interface QuickMatchRemoteDataSource {
    suspend fun fetchProfiles(): List<MatchUserProfileDto>
}

