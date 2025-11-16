package com.example.damandroid.presentation.quickmatch.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.damandroid.domain.model.MatchUserProfile
import com.example.damandroid.domain.usecase.GetQuickMatchProfiles
import com.example.damandroid.domain.usecase.LikeProfile
import com.example.damandroid.domain.usecase.PassProfile
import com.example.damandroid.presentation.quickmatch.model.QuickMatchUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class QuickMatchViewModel(
    private val getQuickMatchProfiles: GetQuickMatchProfiles,
    private val likeProfileUseCase: LikeProfile,
    private val passProfileUseCase: PassProfile
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

    /**
     * Enregistre un like pour un profil
     * Retire le profil de la liste et vérifie si c'est un match
     */
    fun likeProfile(profileId: String, onMatch: (MatchUserProfile) -> Unit) {
        viewModelScope.launch {
            runCatching { likeProfileUseCase(profileId) }
                .onSuccess { result ->
                    // Retirer le profil liké de la liste
                    _uiState.update { state ->
                        state.copy(
                            profiles = state.profiles.filter { it.id != profileId }
                        )
                    }
                    
                    // Si c'est un match, appeler le callback
                    result.matchedProfile?.let { matchedProfile ->
                        onMatch(matchedProfile)
                    }
                }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(error = throwable.message ?: "Failed to like profile")
                    }
                }
        }
    }

    /**
     * Enregistre un pass pour un profil
     * Retire le profil de la liste
     */
    fun passProfile(profileId: String) {
        viewModelScope.launch {
            runCatching { passProfileUseCase(profileId) }
                .onSuccess {
                    // Retirer le profil passé de la liste
                    _uiState.update { state ->
                        state.copy(
                            profiles = state.profiles.filter { it.id != profileId }
                        )
                    }
                }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(error = throwable.message ?: "Failed to pass profile")
                    }
                }
        }
    }
}

