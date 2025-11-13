package com.example.damandroid.data.datasource

import com.example.damandroid.data.model.NotificationsOverviewDto

interface NotificationsRemoteDataSource {
    suspend fun fetchNotifications(): NotificationsOverviewDto
    suspend fun markAsRead(notificationId: String)
    suspend fun markAllAsRead()
}

