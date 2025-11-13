package com.example.damandroid.presentation.homefeed.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.damandroid.domain.usecase.GetHomeFeed
import com.example.damandroid.domain.usecase.ToggleActivitySaved
import com.example.damandroid.presentation.homefeed.model.HomeFeedUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeFeedViewModel(
    private val getHomeFeed: GetHomeFeed,
    private val toggleActivitySaved: ToggleActivitySaved
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeFeedUiState(isLoading = true))
    val uiState: StateFlow<HomeFeedUiState> = _uiState.asStateFlow()

    init {
        loadFeed()
    }

    fun loadFeed() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            runCatching { getHomeFeed() }
                .onSuccess { feed ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            activities = feed.activities,
                            sportCategories = feed.sportCategories
                        )
                    }
                }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = throwable.message ?: "Unable to load home feed"
                        )
                    }
                }
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun onFilterSportChange(sport: String) {
        _uiState.update { it.copy(filterSport = sport) }
    }

    fun onFilterDistanceChange(distance: Float) {
        _uiState.update { it.copy(filterDistance = distance) }
    }

    fun onFilterSheetToggle(show: Boolean) {
        _uiState.update { it.copy(showFilterSheet = show) }
    }

    fun onToggleSaved(activityId: String) {
        viewModelScope.launch {
            runCatching { toggleActivitySaved(activityId) }
                .onSuccess { feed ->
                    _uiState.update {
                        it.copy(
                            activities = feed.activities,
                            sportCategories = feed.sportCategories
                        )
                    }
                }
                .onFailure { throwable ->
                    _uiState.update { it.copy(error = throwable.message ?: "Unable to update activity") }
                }
        }
    }
}

