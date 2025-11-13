package com.example.damandroid.presentation.homefeed.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.damandroid.domain.model.HomeActivity
import com.example.damandroid.presentation.homefeed.model.HomeFeedUiState
import com.example.damandroid.presentation.homefeed.ui.components.HomeFeedContent
import com.example.damandroid.presentation.homefeed.viewmodel.HomeFeedViewModel
import kotlinx.coroutines.launch

@Composable
fun HomeFeedRoute(
    viewModel: HomeFeedViewModel,
    onActivityClick: (HomeActivity) -> Unit,
    onSearchClick: (() -> Unit)?,
    onAISuggestionsClick: (() -> Unit)?,
    onQuickMatchClick: (() -> Unit)?,
    onAIMatchmakerClick: (() -> Unit)?,
    onEventDetailsClick: ((String) -> Unit)?,
    onCreateClick: (() -> Unit)?,
    onNotificationsClick: (() -> Unit)?,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(uiState.error) {
        uiState.error?.let { message ->
            coroutineScope.launch {
                snackbarHostState.showSnackbar(message)
            }
        }
    }

    HomeFeedScreen(
        state = uiState,
        snackbarHostState = snackbarHostState,
        onRetry = viewModel::loadFeed,
        onSearchQueryChange = viewModel::onSearchQueryChange,
        onFilterSportChange = viewModel::onFilterSportChange,
        onFilterDistanceChange = viewModel::onFilterDistanceChange,
        onFilterSheetToggle = viewModel::onFilterSheetToggle,
        onToggleSaved = viewModel::onToggleSaved,
        onActivityClick = onActivityClick,
        onSearchClick = onSearchClick,
        onAISuggestionsClick = onAISuggestionsClick,
        onQuickMatchClick = onQuickMatchClick,
        onAIMatchmakerClick = onAIMatchmakerClick,
        onEventDetailsClick = onEventDetailsClick,
        onCreateClick = onCreateClick,
        onNotificationsClick = onNotificationsClick,
        modifier = modifier
    )
}

@Composable
fun HomeFeedScreen(
    state: HomeFeedUiState,
    snackbarHostState: SnackbarHostState,
    onRetry: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onFilterSportChange: (String) -> Unit,
    onFilterDistanceChange: (Float) -> Unit,
    onFilterSheetToggle: (Boolean) -> Unit,
    onToggleSaved: (String) -> Unit,
    onActivityClick: (HomeActivity) -> Unit,
    onSearchClick: (() -> Unit)?,
    onAISuggestionsClick: (() -> Unit)?,
    onQuickMatchClick: (() -> Unit)?,
    onAIMatchmakerClick: (() -> Unit)?,
    onEventDetailsClick: ((String) -> Unit)?,
    onCreateClick: (() -> Unit)?,
    onNotificationsClick: (() -> Unit)?,
    modifier: Modifier = Modifier
) {
    when {
        state.isLoading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        state.error != null && state.activities.isEmpty() -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = state.error)
            }
        }

        else -> {
            HomeFeedContent(
                state = state,
                onSearchQueryChange = onSearchQueryChange,
                onFilterSportChange = onFilterSportChange,
                onFilterDistanceChange = onFilterDistanceChange,
                onFilterSheetToggle = onFilterSheetToggle,
                onToggleSaved = onToggleSaved,
                onActivityClick = onActivityClick,
                onSearchClick = onSearchClick,
                onAISuggestionsClick = onAISuggestionsClick,
                onQuickMatchClick = onQuickMatchClick,
                onAIMatchmakerClick = onAIMatchmakerClick,
                onEventDetailsClick = onEventDetailsClick,
                onCreateClick = onCreateClick,
                onNotificationsClick = onNotificationsClick,
                modifier = modifier,
                snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
            )
        }
    }
}

