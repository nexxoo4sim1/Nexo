package com.example.damandroid.presentation.ai.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.damandroid.domain.usecase.GetAICoachOverview
import com.example.damandroid.presentation.ai.model.AICoachTab
import com.example.damandroid.presentation.ai.model.AICoachUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AICoachViewModel(
    private val getAICoachOverview: GetAICoachOverview
) : ViewModel() {

    private val _uiState = MutableStateFlow(AICoachUiState(isLoading = true))
    val uiState: StateFlow<AICoachUiState> = _uiState

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            runCatching { getAICoachOverview() }
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
                            error = throwable.message ?: "Unknown error"
                        )
                    }
                }
        }
    }

    fun onTabSelected(tab: AICoachTab) {
        _uiState.update { it.copy(selectedTab = tab) }
    }
}

