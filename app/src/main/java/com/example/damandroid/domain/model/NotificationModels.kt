package com.example.damandroid.domain.model

import java.time.OffsetDateTime

sealed class NotificationItem(
    open val id: String,
    open val title: String,
    open val message: String,
    open val timestamp: OffsetDateTime,
    open val isRead: Boolean
) {
    data class SessionInvite(
        override val id: String,
        override val title: String,
        override val message: String,
        override val timestamp: OffsetDateTime,
        override val isRead: Boolean,
        val sessionId: String,
        val hostName: String,
        val sessionTime: String
    ) : NotificationItem(id, title, message, timestamp, isRead)

    data class ActivityReminder(
        override val id: String,
        override val title: String,
        override val message: String,
        override val timestamp: OffsetDateTime,
        override val isRead: Boolean,
        val activityId: String,
        val activityName: String,
        val activityTime: String
    ) : NotificationItem(id, title, message, timestamp, isRead)

    data class AchievementUnlocked(
        override val id: String,
        override val title: String,
        override val message: String,
        override val timestamp: OffsetDateTime,
        override val isRead: Boolean,
        val badgeName: String,
        val badgeIcon: String
    ) : NotificationItem(id, title, message, timestamp, isRead)

    data class SystemMessage(
        override val id: String,
        override val title: String,
        override val message: String,
        override val timestamp: OffsetDateTime,
        override val isRead: Boolean
    ) : NotificationItem(id, title, message, timestamp, isRead)
}

data class NotificationsOverview(
    val unreadCount: Int,
    val notifications: List<NotificationItem>
)

