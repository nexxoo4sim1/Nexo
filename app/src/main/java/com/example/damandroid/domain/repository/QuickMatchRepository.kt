package com.example.damandroid.domain.repository

import com.example.damandroid.domain.model.MatchUserProfile

interface QuickMatchRepository {
    suspend fun getProfiles(): List<MatchUserProfile>
}

