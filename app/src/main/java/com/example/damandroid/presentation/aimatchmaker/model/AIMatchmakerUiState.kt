package com.example.damandroid.presentation.aimatchmaker.model

import com.example.damandroid.domain.model.MatchmakerProfile

data class AIMatchmakerUiState(
    val isLoading: Boolean = false,
    val profiles: List<MatchmakerProfile> = emptyList(),
    val totalMatches: Int = 0,
    val newMatchesToday: Int = 0,
    val error: String? = null
)

