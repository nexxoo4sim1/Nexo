package com.example.damandroid.presentation.quickmatch.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.damandroid.domain.usecase.GetQuickMatchProfiles
import com.example.damandroid.presentation.quickmatch.model.QuickMatchUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class QuickMatchViewModel(
    private val getQuickMatchProfiles: GetQuickMatchProfiles
) : ViewModel() {

    private val _uiState = MutableStateFlow(QuickMatchUiState(isLoading = true))
    val uiState: StateFlow<QuickMatchUiState> = _uiState

    init {
        loadProfiles()
    }

    fun loadProfiles() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            runCatching { getQuickMatchProfiles() }
                .onSuccess { profiles ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            profiles = profiles,
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
}

