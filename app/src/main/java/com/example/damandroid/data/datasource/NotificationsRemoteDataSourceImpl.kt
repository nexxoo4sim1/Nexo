package com.example.damandroid.data.datasource

import com.example.damandroid.data.model.NotificationDto
import com.example.damandroid.data.model.NotificationsOverviewDto
import java.time.Instant

class NotificationsRemoteDataSourceImpl : NotificationsRemoteDataSource {

    private val notifications = mutableListOf(
        NotificationDto(
            id = "notif_1",
            type = "session_invite",
            title = "You're invited!",
            message = "Alex invited you to Morning Run Club",
            timestampIso = Instant.now().toString(),
            isRead = false,
            metadata = mapOf(
                "sessionId" to "session_1",
                "hostName" to "Alex Thompson",
                "sessionTime" to "Tomorrow 7:00 AM"
            )
        ),
        NotificationDto(
            id = "notif_2",
            type = "achievement",
            title = "Badge Unlocked",
            message = "You earned the Marathon Starter badge",
            timestampIso = Instant.now().minusSeconds(60 * 60).toString(),
            isRead = false,
            metadata = mapOf(
                "badgeName" to "Marathon Starter",
                "badgeIcon" to "🏅"
            )
        ),
        NotificationDto(
            id = "notif_3",
            type = "activity_reminder",
            title = "Activity Reminder",
            message = "Beach Volleyball starts in 2 hours",
            timestampIso = Instant.now().minusSeconds(2 * 60 * 60).toString(),
            isRead = true,
            metadata = mapOf(
                "activityId" to "activity_42",
                "activityName" to "Beach Volleyball",
                "activityTime" to "Today 5:30 PM"
            )
        )
    )

    override suspend fun fetchNotifications(): NotificationsOverviewDto {
        val unread = notifications.count { !it.isRead }
        return NotificationsOverviewDto(
            unreadCount = unread,
            notifications = notifications.sortedByDescending { it.timestampIso }
        )
    }

    override suspend fun markAsRead(notificationId: String) {
        val index = notifications.indexOfFirst { it.id == notificationId }
        if (index != -1) {
            notifications[index] = notifications[index].copy(isRead = true)
        }
    }

    override suspend fun markAllAsRead() {
        notifications.replaceAll { it.copy(isRead = true) }
    }
}
