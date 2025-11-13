package com.example.damandroid.domain.repository

import com.example.damandroid.domain.model.NotificationsOverview

interface NotificationsRepository {
    suspend fun getNotifications(): NotificationsOverview
    suspend fun markAsRead(notificationId: String)
    suspend fun markAllAsRead()
}

