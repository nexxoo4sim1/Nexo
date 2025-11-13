package com.example.damandroid.presentation.aimatchmaker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.damandroid.domain.usecase.GetMatchmakerRecommendations
import com.example.damandroid.presentation.aimatchmaker.model.AIMatchmakerUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AIMatchmakerViewModel(
    private val getRecommendations: GetMatchmakerRecommendations
) : ViewModel() {

    private val _uiState = MutableStateFlow(AIMatchmakerUiState(isLoading = true))
    val uiState: StateFlow<AIMatchmakerUiState> = _uiState

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            runCatching { getRecommendations() }
                .onSuccess { recommendation ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            profiles = recommendation.profiles,
                            totalMatches = recommendation.totalMatches,
                            newMatchesToday = recommendation.newMatchesToday,
                            error = null
                        )
                    }
                }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = throwable.message ?: "Unable to load recommendations"
                        )
                    }
                }
        }
    }
}

