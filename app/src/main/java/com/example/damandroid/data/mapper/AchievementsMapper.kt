package com.example.damandroid.data.mapper

import com.example.damandroid.data.model.AchievementBadgeDto
import com.example.damandroid.data.model.AchievementChallengeDto
import com.example.damandroid.data.model.AchievementUserStatsDto
import com.example.damandroid.data.model.AchievementsOverviewDto
import com.example.damandroid.data.model.LeaderboardEntryDto
import com.example.damandroid.domain.model.AchievementBadge
import com.example.damandroid.domain.model.AchievementChallenge
import com.example.damandroid.domain.model.AchievementUserStats
import com.example.damandroid.domain.model.AchievementsOverview
import com.example.damandroid.domain.model.LeaderboardEntry

fun AchievementsOverviewDto.toDomain(): AchievementsOverview = AchievementsOverview(
    stats = stats.toDomain(),
    badges = badges.map(AchievementBadgeDto::toDomain),
    challenges = challenges.map(AchievementChallengeDto::toDomain),
    leaderboard = leaderboard.map(LeaderboardEntryDto::toDomain)
)

private fun AchievementUserStatsDto.toDomain(): AchievementUserStats = AchievementUserStats(
    level = level,
    xp = xp,
    nextLevelXp = nextLevelXp,
    totalBadges = totalBadges,
    currentStreak = currentStreak,
    longestStreak = longestStreak
)

private fun AchievementBadgeDto.toDomain(): AchievementBadge = AchievementBadge(
    id = id,
    icon = icon,
    title = title,
    description = description,
    category = category,
    unlocked = unlocked,
    unlockedDate = unlockedDate,
    progress = progress,
    total = total,
    rarity = rarity
)

private fun AchievementChallengeDto.toDomain(): AchievementChallenge = AchievementChallenge(
    id = id,
    title = title,
    description = description,
    progress = progress,
    total = total,
    reward = reward,
    deadline = deadline
)

private fun LeaderboardEntryDto.toDomain(): LeaderboardEntry = LeaderboardEntry(
    rank = rank,
    name = name,
    points = points,
    badge = badge
)

