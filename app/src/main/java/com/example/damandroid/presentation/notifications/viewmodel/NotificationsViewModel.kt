package com.example.damandroid.presentation.notifications.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.damandroid.domain.usecase.GetNotifications
import com.example.damandroid.domain.usecase.MarkAllNotificationsAsRead
import com.example.damandroid.domain.usecase.MarkNotificationAsRead
import com.example.damandroid.presentation.notifications.model.NotificationsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NotificationsViewModel(
    private val getNotifications: GetNotifications,
    private val markNotificationAsRead: MarkNotificationAsRead,
    private val markAllNotificationsAsRead: MarkAllNotificationsAsRead
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationsUiState(isLoading = true))
    val uiState: StateFlow<NotificationsUiState> = _uiState

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            runCatching { getNotifications() }
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
                            error = throwable.message ?: "Unable to load notifications"
                        )
                    }
                }
        }
    }

    fun onNotificationRead(notificationId: String) {
        viewModelScope.launch {
            runCatching { markNotificationAsRead(notificationId) }
            refresh()
        }
    }

    fun onMarkAllRead() {
        viewModelScope.launch {
            runCatching { markAllNotificationsAsRead() }
            refresh()
        }
    }
}

