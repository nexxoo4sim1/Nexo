package com.example.damandroid.domain.model

data class AchievementUserStats(
    val level: Int,
    val xp: Int,
    val nextLevelXp: Int,
    val totalBadges: Int,
    val currentStreak: Int,
    val longestStreak: Int
)

data class AchievementBadge(
    val id: String,
    val icon: String,
    val title: String,
    val description: String,
    val category: String,
    val unlocked: Boolean,
    val unlockedDate: String? = null,
    val progress: Int? = null,
    val total: Int? = null,
    val rarity: String
)

data class AchievementChallenge(
    val id: String,
    val title: String,
    val description: String,
    val progress: Int,
    val total: Int,
    val reward: String,
    val deadline: String
)

data class LeaderboardEntry(
    val rank: Int,
    val name: String,
    val points: Int,
    val badge: String
)

data class AchievementsOverview(
    val stats: AchievementUserStats,
    val badges: List<AchievementBadge>,
    val challenges: List<AchievementChallenge>,
    val leaderboard: List<LeaderboardEntry>
)

