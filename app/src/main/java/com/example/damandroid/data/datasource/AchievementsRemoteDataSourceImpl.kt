package com.example.damandroid.data.datasource

import com.example.damandroid.data.model.AchievementBadgeDto
import com.example.damandroid.data.model.AchievementChallengeDto
import com.example.damandroid.data.model.AchievementUserStatsDto
import com.example.damandroid.data.model.AchievementsOverviewDto
import com.example.damandroid.data.model.LeaderboardEntryDto

class AchievementsRemoteDataSourceImpl : AchievementsRemoteDataSource {
    override suspend fun fetchOverview(): AchievementsOverviewDto = AchievementsOverviewDto(
        stats = AchievementUserStatsDto(
            level = 12,
            xp = 2350,
            nextLevelXp = 3000,
            totalBadges = 18,
            currentStreak = 7,
            longestStreak = 21
        ),
        badges = listOf(
            AchievementBadgeDto(
                id = "1",
                icon = "🏃",
                title = "Marathon Runner",
                description = "Completed 5+ running events",
                category = "Running",
                unlocked = true,
                unlockedDate = "Oct 28, 2025",
                progress = null,
                total = null,
                rarity = "rare"
            ),
            AchievementBadgeDto(
                id = "5",
                icon = "🔥",
                title = "Consistency King",
                description = "Maintain a 30-day streak",
                category = "Consistency",
                unlocked = false,
                unlockedDate = null,
                progress = 7,
                total = 30,
                rarity = "epic"
            )
        ),
        challenges = listOf(
            AchievementChallengeDto(
                id = "1",
                title = "Weekend Warrior",
                description = "Complete 4 activities this weekend",
                progress = 2,
                total = 4,
                reward = "100 XP + Weekend Badge",
                deadline = "2 days left"
            )
        ),
        leaderboard = listOf(
            LeaderboardEntryDto(rank = 1, name = "You", points = 2350, badge = "🥇"),
            LeaderboardEntryDto(rank = 2, name = "Sarah M.", points = 2280, badge = "🥈"),
            LeaderboardEntryDto(rank = 3, name = "Mike R.", points = 2150, badge = "🥉")
        )
    )
}

