package com.example.damandroid.presentation.homefeed.model

import com.example.damandroid.domain.model.HomeActivity
import com.example.damandroid.domain.model.SportCategory

data class HomeFeedUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val activities: List<HomeActivity> = emptyList(),
    val sportCategories: List<SportCategory> = emptyList(),
    val searchQuery: String = "",
    val filterSport: String = "all",
    val filterDistance: Float = 5f,
    val showFilterSheet: Boolean = false
) {
    val filteredActivities: List<HomeActivity>
        get() = activities.filter { activity ->
            val matchesSearch = searchQuery.isBlank() ||
                activity.title.contains(searchQuery, ignoreCase = true) ||
                activity.sportType.contains(searchQuery, ignoreCase = true)
            val matchesSport = filterSport == "all" || activity.sportType.equals(filterSport, ignoreCase = true)
            matchesSearch && matchesSport
        }
}

