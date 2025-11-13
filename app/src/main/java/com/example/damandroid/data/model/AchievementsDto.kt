package com.example.damandroid.data.model

data class AchievementUserStatsDto(
    val level: Int,
    val xp: Int,
    val nextLevelXp: Int,
    val totalBadges: Int,
    val currentStreak: Int,
    val longestStreak: Int
)

data class AchievementBadgeDto(
    val id: String,
    val icon: String,
    val title: String,
    val description: String,
    val category: String,
    val unlocked: Boolean,
    val unlockedDate: String?,
    val progress: Int?,
    val total: Int?,
    val rarity: String
)

data class AchievementChallengeDto(
    val id: String,
    val title: String,
    val description: String,
    val progress: Int,
    val total: Int,
    val reward: String,
    val deadline: String
)

data class LeaderboardEntryDto(
    val rank: Int,
    val name: String,
    val points: Int,
    val badge: String
)

data class AchievementsOverviewDto(
    val stats: AchievementUserStatsDto,
    val badges: List<AchievementBadgeDto>,
    val challenges: List<AchievementChallengeDto>,
    val leaderboard: List<LeaderboardEntryDto>
)

