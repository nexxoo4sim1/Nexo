package com.example.damandroid.data.datasource

import com.example.damandroid.api.LikeProfileRequest
import com.example.damandroid.api.LikeProfileResponse
import com.example.damandroid.api.LikesReceivedResponse
import com.example.damandroid.api.QuickMatchApiService
import com.example.damandroid.api.RetrofitClient
import com.example.damandroid.data.model.NotificationDto
import com.example.damandroid.data.model.NotificationsOverviewDto
import java.time.Instant

class NotificationsRemoteDataSourceImpl(
    private val quickMatchApiService: QuickMatchApiService = RetrofitClient.quickMatchApiService
) : NotificationsRemoteDataSource {

    private val notifications = mutableListOf<NotificationDto>()

    override suspend fun fetchNotifications(): NotificationsOverviewDto {
        // Récupérer les likes reçus depuis l'API
        val likesReceived = try {
            val response = quickMatchApiService.getLikesReceived()
            if (response.isSuccessful) {
                response.body()?.likes ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            android.util.Log.e("NotificationsDataSource", "Error fetching likes received: ${e.message}")
            emptyList()
        }

        // Convertir les likes reçus en notifications
        val likeNotifications = likesReceived.map { like ->
            NotificationDto(
                id = "like_${like.likeId}",
                type = "like",
                title = if (like.isMatch) "It's a Match! 🎉" else "New Like",
                message = "${like.fromUser.name} ${if (like.isMatch) "matched with you!" else "liked your profile"}",
                timestampIso = like.createdAt,
                isRead = false, // Les likes reçus sont considérés comme non lus
                metadata = mapOf(
                    "fromUserId" to like.fromUser.getUserId(),
                    "fromUserName" to like.fromUser.name,
                    "fromUserAvatar" to (like.fromUser.getAvatar() ?: ""),
                    "isMatch" to like.isMatch.toString(),
                    "matchId" to (like.matchId ?: "")
                )
            )
        }

        // Combiner avec les autres notifications (pour l'instant, on garde juste les likes)
        // TODO: Récupérer les autres notifications depuis l'API si nécessaire
        val allNotifications = likeNotifications

        val unread = allNotifications.count { !it.isRead }
        return NotificationsOverviewDto(
            unreadCount = unread,
            notifications = allNotifications.sortedByDescending { it.timestampIso }
        )
    }

    override suspend fun markAsRead(notificationId: String) {
        // Pour les notifications de likes, on peut les marquer comme lues localement
        // ou appeler une API si nécessaire
        val index = notifications.indexOfFirst { it.id == notificationId }
        if (index != -1) {
            notifications[index] = notifications[index].copy(isRead = true)
        }
    }

    override suspend fun markAllAsRead() {
        notifications.replaceAll { it.copy(isRead = true) }
    }

    override suspend fun likeBack(profileId: String): Boolean {
        return try {
            val response = quickMatchApiService.likeProfile(LikeProfileRequest(profileId))
            if (response.isSuccessful) {
                val likeResponse = response.body()
                likeResponse?.isMatch ?: false
            } else {
                false
            }
        } catch (e: Exception) {
            android.util.Log.e("NotificationsDataSource", "Error liking back: ${e.message}")
            false
        }
    }
}
