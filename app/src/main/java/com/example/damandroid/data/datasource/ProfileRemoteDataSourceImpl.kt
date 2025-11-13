package com.example.damandroid.data.datasource

import com.example.damandroid.api.RetrofitClient
import com.example.damandroid.api.UpdateProfileRequestDto
import com.example.damandroid.api.UserApiService
import com.example.damandroid.api.UserResponseDto
import com.example.damandroid.api.ChangePasswordRequestDto
import com.example.damandroid.api.MessageResponse
import com.example.damandroid.auth.UserSession
import com.example.damandroid.data.model.AchievementDto
import com.example.damandroid.data.model.ProfileActivityDto
import com.example.damandroid.data.model.ProfileMedalDto
import com.example.damandroid.data.model.UserProfileDto
import com.example.damandroid.data.model.UserStatsDto
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException

class ProfileRemoteDataSourceImpl(
    private val userApiService: UserApiService = RetrofitClient.userApiService
) : ProfileRemoteDataSource {

    override suspend fun fetchCurrentUser(): UserProfileDto {
        val token = UserSession.token
        val networkProfile = if (!token.isNullOrBlank()) {
            runCatching {
                val response = userApiService.getProfile("Bearer $token")
                if (response.isSuccessful) response.body() else null
            }.getOrNull()
        } else {
            null
        }

        return networkProfile.toDomainProfile()
    }

    override suspend fun updateProfile(
        userId: String,
        body: UpdateProfileRequestDto
    ): UserProfileDto {
        val token = UserSession.token
            ?: throw IllegalStateException("Cannot update profile without authentication token")

        val response = userApiService.updateProfile(
            bearerToken = "Bearer $token",
            userId = userId,
            body = body
        )

        if (!response.isSuccessful) {
            throw HttpException(response)
        }

        val updated = response.body()
        return updated.toDomainProfile()
    }

    override suspend fun uploadProfileImage(
        userId: String,
        fileName: String,
        mimeType: String,
        bytes: ByteArray
    ): UserProfileDto {
        val token = UserSession.token
            ?: throw IllegalStateException("Cannot upload profile image without authentication token")

        val requestBody = bytes.toRequestBody(mimeType.toMediaType())
        val multipart = MultipartBody.Part.createFormData("image", fileName, requestBody)

        val response = userApiService.uploadProfileImage(
            bearerToken = "Bearer $token",
            userId = userId,
            image = multipart
        )

        if (!response.isSuccessful) {
            throw HttpException(response)
        }

        val updated = response.body()
        return updated.toDomainProfile()
    }

    override suspend fun changePassword(
        userId: String,
        request: ChangePasswordRequestDto
    ): MessageResponse {
        val token = UserSession.token
            ?: throw IllegalStateException("Cannot change password without authentication token")

        val response = userApiService.changePassword(
            bearerToken = "Bearer $token",
            userId = userId,
            body = request
        )

        if (!response.isSuccessful) {
            throw HttpException(response)
        }

        return response.body() ?: MessageResponse(message = "Password updated successfully")
    }

    private fun UserResponseDto?.toDomainProfile(): UserProfileDto {
        val sessionUser = UserSession.user
        val resolvedId = this?.id ?: this?._id ?: sessionUser?.id ?: "user_123"
        val resolvedName = this?.name ?: sessionUser?.name ?: "Alex Thompson"
        val resolvedLocation = this?.location ?: sessionUser?.location ?: "Los Angeles, CA"
        val resolvedBio = this?.about ?: "Fitness enthusiast and outdoor adventurer"
        val resolvedSports = this?.sportsInterests.takeUnless { it.isNullOrEmpty() }
            ?: listOf("Running", "Swimming", "Hiking", "Cycling")

        return UserProfileDto(
            id = resolvedId,
            name = resolvedName,
            avatarUrl = this?.profileImageUrl
                ?: "https://api.dicebear.com/7.x/avataaars/svg?seed=${resolvedName.replace(" ", "")}",
            bio = resolvedBio,
            location = resolvedLocation,
            isVerified = this?.isEmailVerified ?: (sessionUser != null),
            stats = UserStatsDto(
                sessionsJoined = 24,
                sessionsHosted = 8,
                followers = 342,
                following = 180,
                favoriteSports = resolvedSports
            ),
            achievements = listOf(
                AchievementDto(
                    id = "achv_1",
                    title = "Consistency Champion",
                    description = "Completed 10 sessions in a row",
                    icon = "🔥"
                ),
                AchievementDto(
                    id = "achv_2",
                    title = "Trail Explorer",
                    description = "Hosted 5 outdoor activities",
                    icon = "🥾"
                )
            ),
            activities = listOf(
                ProfileActivityDto(
                    id = "act_1",
                    title = "Morning Run",
                    sportIcon = "🏃",
                    date = "Nov 5, 2025",
                    time = "7:00 AM",
                    location = "City Park",
                    status = "upcoming"
                ),
                ProfileActivityDto(
                    id = "act_2",
                    title = "Swimming Session",
                    sportIcon = "🏊",
                    date = "Nov 6, 2025",
                    time = "6:00 PM",
                    location = "Community Pool",
                    status = "completed"
                )
            ),
            medals = listOf(
                ProfileMedalDto(
                    id = "medal_1",
                    title = "Marathon Starter",
                    description = "Completed first marathon",
                    icon = "🏅",
                    rarity = "rare"
                ),
                ProfileMedalDto(
                    id = "medal_2",
                    title = "Community Leader",
                    description = "Hosted 20+ sessions",
                    icon = "👑",
                    rarity = "legendary"
                )
            )
        )
    }
}

