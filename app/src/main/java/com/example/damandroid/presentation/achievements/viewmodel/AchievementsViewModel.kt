package com.example.damandroid.presentation.achievements.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.damandroid.domain.usecase.GetAchievementsOverview
import com.example.damandroid.presentation.achievements.model.AchievementsTab
import com.example.damandroid.presentation.achievements.model.AchievementsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AchievementsViewModel(
    private val getAchievementsOverview: GetAchievementsOverview
) : ViewModel() {

    private val _uiState = MutableStateFlow(AchievementsUiState(isLoading = true))
    val uiState: StateFlow<AchievementsUiState> = _uiState

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            runCatching { getAchievementsOverview() }
                .onSuccess { overview ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            overview = overview,
                            error = null
                        )
                    }
                }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = throwable.message ?: "Unable to load achievements"
                        )
                    }
                }
        }
    }

    fun onTabSelected(tab: AchievementsTab) {
        _uiState.update { it.copy(selectedTab = tab) }
    }
}

