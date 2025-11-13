package com.example.damandroid.data.mapper

import com.example.damandroid.data.model.AchievementDto
import com.example.damandroid.data.model.ProfileActivityDto
import com.example.damandroid.data.model.ProfileMedalDto
import com.example.damandroid.data.model.UserProfileDto
import com.example.damandroid.data.model.UserStatsDto
import com.example.damandroid.domain.model.Achievement
import com.example.damandroid.domain.model.ActivityStatus
import com.example.damandroid.domain.model.MedalRarity
import com.example.damandroid.domain.model.ProfileActivity
import com.example.damandroid.domain.model.ProfileMedal
import com.example.damandroid.domain.model.UserProfile
import com.example.damandroid.domain.model.UserStatsOverview

fun UserProfileDto.toDomain(): UserProfile = UserProfile(
    id = id,
    name = name,
    avatarUrl = avatarUrl,
    bio = bio,
    location = location,
    isVerified = isVerified,
    stats = stats.toDomain(),
    achievements = achievements.map(AchievementDto::toDomain),
    activities = activities.map(ProfileActivityDto::toDomain),
    medals = medals.map(ProfileMedalDto::toDomain)
)

private fun UserStatsDto.toDomain(): UserStatsOverview = UserStatsOverview(
    sessionsJoined = sessionsJoined,
    sessionsHosted = sessionsHosted,
    followers = followers,
    following = following,
    favoriteSports = favoriteSports
)

private fun AchievementDto.toDomain(): Achievement = Achievement(
    id = id,
    title = title,
    description = description,
    icon = icon
)

private fun ProfileActivityDto.toDomain(): ProfileActivity = ProfileActivity(
    id = id,
    title = title,
    sportIcon = sportIcon,
    date = date,
    time = time,
    location = location,
    status = ActivityStatus.valueOf(status.uppercase())
)

private fun ProfileMedalDto.toDomain(): ProfileMedal = ProfileMedal(
    id = id,
    title = title,
    description = description,
    icon = icon,
    rarity = MedalRarity.valueOf(rarity.uppercase())
)

