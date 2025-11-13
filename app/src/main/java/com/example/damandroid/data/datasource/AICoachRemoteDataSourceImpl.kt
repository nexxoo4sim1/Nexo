package com.example.damandroid.data.datasource

import com.example.damandroid.data.model.AICoachOverviewDto
import com.example.damandroid.data.model.ChallengeDto
import com.example.damandroid.data.model.SuggestionDto
import com.example.damandroid.data.model.WeeklyStatsDto
import com.example.damandroid.data.model.WorkoutTipDto

class AICoachRemoteDataSourceImpl : AICoachRemoteDataSource {
    override suspend fun fetchOverview(): AICoachOverviewDto = AICoachOverviewDto(
        weeklyStats = WeeklyStatsDto(
            workouts = 3,
            goal = 5,
            calories = 1200,
            minutes = 180,
            streak = 7
        ),
        suggestions = listOf(
            SuggestionDto(
                id = "1",
                title = "Try a morning swim",
                description = "4 swimmers nearby are free tomorrow 7AM",
                icon = "🏊",
                time = "Tomorrow 7AM",
                participants = 4,
                matchScore = 95
            ),
            SuggestionDto(
                id = "2",
                title = "Join evening yoga session",
                description = "Perfect for recovery after your runs",
                icon = "🧘",
                time = "Today 6PM",
                participants = 8,
                matchScore = 88
            ),
            SuggestionDto(
                id = "3",
                title = "Weekend cycling group",
                description = "Explore new routes with local cyclists",
                icon = "🚴",
                time = "Saturday 8AM",
                participants = 12,
                matchScore = 82
            )
        ),
        workoutTips = listOf(
            WorkoutTipDto(
                id = "1",
                title = "Warm-up is essential",
                description = "Spend 5-10 minutes warming up to prevent injuries and improve performance.",
                icon = "🔥",
                category = "Basics"
            ),
            WorkoutTipDto(
                id = "2",
                title = "Stay hydrated",
                description = "Drink water before, during, and after your workout for optimal performance.",
                icon = "💧",
                category = "Health"
            ),
            WorkoutTipDto(
                id = "3",
                title = "Progressive overload",
                description = "Gradually increase intensity to continue seeing improvements.",
                icon = "📈",
                category = "Training"
            )
        ),
        challenges = listOf(
            ChallengeDto(
                id = "1",
                title = "30-Day Running Streak",
                description = "Run at least 1 mile every day for 30 days",
                progress = 7,
                total = 30,
                reward = "🏆 Marathon Badge"
            ),
            ChallengeDto(
                id = "2",
                title = "Weekly Variety Challenge",
                description = "Try 3 different sports this week",
                progress = 1,
                total = 3,
                reward = "⭐ Explorer Badge"
            )
        )
    )
}

