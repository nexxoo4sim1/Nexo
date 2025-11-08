package com.example.damandroid

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import com.example.damandroid.ui.theme.DamAndroidTheme

data class WeeklyStats(
    val workouts: Int,
    val goal: Int,
    val calories: Int,
    val minutes: Int,
    val streak: Int
)

data class Suggestion(
    val id: String,
    val title: String,
    val description: String,
    val icon: String,
    val time: String,
    val participants: Int,
    val matchScore: Int
)

data class WorkoutTip(
    val id: String,
    val title: String,
    val description: String,
    val icon: String,
    val category: String
)

data class Challenge(
    val id: String,
    val title: String,
    val description: String,
    val progress: Int,
    val total: Int,
    val reward: String
)

@Composable
fun AICoach(
    onBack: () -> Unit,
    onStartChallenge: () -> Unit = {},
    onFindPartner: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf("overview") } // "overview", "suggestions", "tips"

    val weeklyStats = WeeklyStats(
        workouts = 3,
        goal = 5,
        calories = 1200,
        minutes = 180,
        streak = 7
    )

    val suggestions = listOf(
        Suggestion(
            id = "1",
            title = "Try a morning swim",
            description = "4 swimmers nearby are free tomorrow 7AM",
            icon = "🏊",
            time = "Tomorrow 7AM",
            participants = 4,
            matchScore = 95
        ),
        Suggestion(
            id = "2",
            title = "Join evening yoga session",
            description = "Perfect for recovery after your runs",
            icon = "🧘",
            time = "Today 6PM",
            participants = 8,
            matchScore = 88
        ),
        Suggestion(
            id = "3",
            title = "Weekend cycling group",
            description = "Explore new routes with local cyclists",
            icon = "🚴",
            time = "Saturday 8AM",
            participants = 12,
            matchScore = 82
        )
    )

    val workoutTips = listOf(
        WorkoutTip(
            id = "1",
            title = "Warm-up is essential",
            description = "Spend 5-10 minutes warming up to prevent injuries and improve performance.",
            icon = "🔥",
            category = "Basics"
        ),
        WorkoutTip(
            id = "2",
            title = "Stay hydrated",
            description = "Drink water before, during, and after your workout for optimal performance.",
            icon = "💧",
            category = "Health"
        ),
        WorkoutTip(
            id = "3",
            title = "Progressive overload",
            description = "Gradually increase intensity to continue seeing improvements.",
            icon = "📈",
            category = "Training"
        )
    )

    val challenges = listOf(
        Challenge(
            id = "1",
            title = "30-Day Running Streak",
            description = "Run at least 1 mile every day for 30 days",
            progress = 7,
            total = 30,
            reward = "🏆 Marathon Badge"
        ),
        Challenge(
            id = "2",
            title = "Weekly Variety Challenge",
            description = "Try 3 different sports this week",
            progress = 1,
            total = 3,
            reward = "⭐ Explorer Badge"
        )
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFE8D5F2), // from-[#E8D5F2]
                        Color(0xFFFFE4F1), // via-[#FFE4F1]
                        Color(0xFFE5E5F0)  // to-[#E5E5F0]
                    )
                )
            )
    ) {
        // Floating Orbs Background
        FloatingAICoachOrbs()

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
                color = Color.White.copy(alpha = 0.4f),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.6f))
            ) {
                Column {
                    // Top Bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .windowInsetsPadding(WindowInsets.statusBars)
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = onBack,
                                modifier = Modifier.size(48.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Back",
                                    tint = Color(0xFF2D3748),
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = Color(0xFFA855F7),
                                    modifier = Modifier.size(24.dp)
                                )
                                Text(
                                    text = "AI Coach",
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1A202C),
                                    letterSpacing = (-0.5).sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(40.dp)) // Balance spacing
                    }

                    // Motivational Quote
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 16.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(
                                        Color(0xFFA855F7),
                                        Color(0xFFEC4899)
                                    )
                                )
                            )
                            .padding(12.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "\"Progress starts with small steps\"",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Keep going! You're doing great! 💪",
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.9f),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            // Tab Navigation
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                shape = RoundedCornerShape(16.dp),
                color = Color.White.copy(alpha = 0.4f),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.6f))
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TabButton(
                        text = "Overview",
                        isSelected = selectedTab == "overview",
                        onClick = { selectedTab = "overview" },
                        modifier = Modifier.weight(1f)
                    )
                    TabButton(
                        text = "For You",
                        isSelected = selectedTab == "suggestions",
                        onClick = { selectedTab = "suggestions" },
                        modifier = Modifier.weight(1f)
                    )
                    TabButton(
                        text = "Tips",
                        isSelected = selectedTab == "tips",
                        onClick = { selectedTab = "tips" },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Content
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    top = 12.dp,
                    end = 16.dp,
                    bottom = 100.dp
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                when (selectedTab) {
                    "overview" -> {
                        // Weekly Stats
                        item {
                            WeeklyStatsCard(stats = weeklyStats)
                        }

                        // Weather & Best Time
                        item {
                            WeatherCard()
                        }

                        // Active Challenges
                        item {
                            Text(
                                text = "Active Challenges",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF111827),
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }

                        items(challenges) { challenge ->
                            ChallengeCard(challenge = challenge)
                        }

                        // Quick Actions
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                QuickActionButton(
                                    icon = Icons.Default.Star,
                                    text = "Start Challenge",
                                    onClick = onStartChallenge,
                                    gradient = Brush.linearGradient(
                                        colors = listOf(
                                            Color(0xFFA855F7),
                                            Color(0xFFEC4899)
                                        )
                                    ),
                                    modifier = Modifier.weight(1f)
                                )
                                QuickActionButton(
                                    icon = Icons.Default.Group,
                                    text = "Find Partner",
                                    onClick = onFindPartner,
                                    gradient = Brush.linearGradient(
                                        colors = listOf(
                                            Color(0xFF86EFAC),
                                            Color(0xFF6EE7B7)
                                        )
                                    ),
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                    "suggestions" -> {
                        // Personalized Banner
                        item {
                            PersonalizedBanner()
                        }

                        items(suggestions) { suggestion ->
                            SuggestionCard(
                                suggestion = suggestion,
                                onJoinClick = { /* Handle join */ }
                            )
                        }
                    }
                    "tips" -> {
                        // Video Library Card
                        item {
                            VideoLibraryCard()
                        }

                        items(workoutTips) { tip ->
                            TipCard(tip = tip)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TabButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = if (isSelected) Color(0xFFA855F7) else Color(0xFF6B7280)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth(if (isSelected) 1f else 0f)
                .height(2.dp)
                .background(
                    if (isSelected) Color(0xFFA855F7) else Color.Transparent,
                    RoundedCornerShape(1.dp)
                )
        )
    }
}

@Composable
private fun WeeklyStatsCard(stats: WeeklyStats) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.9f)
        ),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.6f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "This Week",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF111827)
                )
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFF86EFAC)
                ) {
                    Text(
                        text = "${stats.streak} day streak 🔥",
                        fontSize = 11.sp,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Stats Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(
                    value = stats.workouts.toString(),
                    label = "Workouts",
                    color = Color(0xFFA855F7)
                )
                StatItem(
                    value = stats.calories.toString(),
                    label = "Calories",
                    color = Color(0xFFF97316)
                )
                StatItem(
                    value = stats.minutes.toString(),
                    label = "Minutes",
                    color = Color(0xFF2563EB)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Progress Bar
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Weekly goal",
                        fontSize = 12.sp,
                        color = Color(0xFF4B5563)
                    )
                    Text(
                        text = "${stats.workouts}/${stats.goal}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF111827)
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                LinearProgressIndicator(
                    progress = { stats.workouts.toFloat() / stats.goal.toFloat() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = Color(0xFFA855F7),
                    trackColor = Color(0xFFE5E7EB)
                )
            }
        }
    }
}

@Composable
private fun StatItem(
    value: String,
    label: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            text = label,
            fontSize = 11.sp,
            color = Color(0xFF6B7280)
        )
    }
}

@Composable
private fun WeatherCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFDBEAFE).copy(alpha = 0.8f)
        ),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.6f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(44.dp),
                shape = RoundedCornerShape(12.dp),
                color = Color.White.copy(alpha = 0.6f)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "☀️",
                        fontSize = 24.sp
                    )
                }
            }
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Perfect weather today!",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF111827)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "72°F, sunny — ideal for outdoor training",
                    fontSize = 12.sp,
                    color = Color(0xFF4B5563)
                )
            }
        }
    }
}

@Composable
private fun ChallengeCard(challenge: Challenge) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.9f)
        ),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.6f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    modifier = Modifier.size(40.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = Color.Transparent
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(
                                        Color(0xFFA855F7),
                                        Color(0xFFEC4899)
                                    )
                                ),
                                RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = challenge.title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF111827)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = challenge.description,
                        fontSize = 12.sp,
                        color = Color(0xFF4B5563)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Progress
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Progress",
                        fontSize = 12.sp,
                        color = Color(0xFF4B5563)
                    )
                    Text(
                        text = "${challenge.progress}/${challenge.total}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF111827)
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                LinearProgressIndicator(
                    progress = { challenge.progress.toFloat() / challenge.total.toFloat() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = Color(0xFFA855F7),
                    trackColor = Color(0xFFE5E7EB)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Reward: ${challenge.reward}",
                fontSize = 11.sp,
                color = Color(0xFF6B7280)
            )
        }
    }
}

@Composable
private fun QuickActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    onClick: () -> Unit,
    gradient: Brush,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(80.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        ),
        contentPadding = PaddingValues(0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = text,
                    fontSize = 13.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun PersonalizedBanner() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFA855F7),
                        Color(0xFFEC4899)
                    )
                )
            )
            .padding(12.dp)
    ) {
        Column {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "Personalized for you",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Based on your activity history and preferences",
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.9f)
            )
        }
    }
}

@Composable
private fun SuggestionCard(
    suggestion: Suggestion,
    onJoinClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.9f)
        ),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.6f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    modifier = Modifier.size(44.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFF86EFAC).copy(alpha = 0.2f)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = suggestion.icon,
                            fontSize = 24.sp
                        )
                    }
                }
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = suggestion.title,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF111827)
                        )
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color.Transparent
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(
                                        Brush.linearGradient(
                                            colors = listOf(
                                                Color(0xFFA855F7),
                                                Color(0xFFEC4899)
                                            )
                                        ),
                                        RoundedCornerShape(8.dp)
                                    )
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "${suggestion.matchScore}% match",
                                    fontSize = 10.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = suggestion.description,
                        fontSize = 12.sp,
                        color = Color(0xFF4B5563)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = null,
                                tint = Color(0xFF6B7280),
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = suggestion.time,
                                fontSize = 12.sp,
                                color = Color(0xFF6B7280)
                            )
                        }
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Group,
                                contentDescription = null,
                                tint = Color(0xFF6B7280),
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "${suggestion.participants} interested",
                                fontSize = 12.sp,
                                color = Color(0xFF6B7280)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onJoinClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF86EFAC)
                ),
                contentPadding = PaddingValues(0.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.FlashOn,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Join Now",
                        fontSize = 13.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun VideoLibraryCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFF7ED).copy(alpha = 0.8f)
        ),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.6f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.PlayCircle,
                    contentDescription = null,
                    tint = Color(0xFFF97316),
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "Workout Video Library",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF111827)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Watch expert-led tutorials and form guides",
                fontSize = 12.sp,
                color = Color(0xFF4B5563)
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedButton(
                onClick = { /* Browse videos */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFF111827)
                ),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.6f))
            ) {
                Text(
                    text = "Browse Videos",
                    fontSize = 13.sp
                )
            }
        }
    }
}

@Composable
private fun TipCard(tip: WorkoutTip) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.9f)
        ),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.6f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Surface(
                modifier = Modifier.size(40.dp),
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFDBEAFE)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = tip.icon,
                        fontSize = 20.sp
                    )
                }
            }
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = tip.title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF111827)
                    )
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color.White.copy(alpha = 0.6f),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.6f))
                    ) {
                        Text(
                            text = tip.category,
                            fontSize = 10.sp,
                            color = Color(0xFF111827),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = tip.description,
                    fontSize = 12.sp,
                    color = Color(0xFF4B5563)
                )
            }
        }
    }
}

@Composable
private fun FloatingAICoachOrbs() {
    Box(modifier = Modifier.fillMaxSize()) {
        // Orb 1
        val infiniteTransition1 = rememberInfiniteTransition(label = "orb1")
        val pulseAlpha1 by infiniteTransition1.animateFloat(
            initialValue = 0.4f,
            targetValue = 0.2f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 3000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "pulse1"
        )

        Box(
            modifier = Modifier
                .offset(x = (-5).dp, y = (-10).dp)
                .size(288.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color(0xFFC084FC).copy(alpha = pulseAlpha1),
                            Color.Transparent
                        )
                    )
                )
                .blur(48.dp)
                .align(Alignment.TopStart)
        )

        // Orb 2
        val infiniteTransition2 = rememberInfiniteTransition(label = "orb2")
        val pulseAlpha2 by infiniteTransition2.animateFloat(
            initialValue = 0.3f,
            targetValue = 0.15f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 3000, delayMillis = 1000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "pulse2"
        )

        Box(
            modifier = Modifier
                .offset(x = (-10).dp, y = (-15).dp)
                .size(384.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF93C5FD).copy(alpha = pulseAlpha2),
                            Color.Transparent
                        )
                    )
                )
                .blur(48.dp)
                .align(Alignment.BottomEnd)
        )

        // Orb 3
        val infiniteTransition3 = rememberInfiniteTransition(label = "orb3")
        val pulseAlpha3 by infiniteTransition3.animateFloat(
            initialValue = 0.3f,
            targetValue = 0.15f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 3000, delayMillis = 2000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "pulse3"
        )

        Box(
            modifier = Modifier
                .offset(x = (-5).dp)
                .size(256.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color(0xFFFBCFE8).copy(alpha = pulseAlpha3),
                            Color.Transparent
                        )
                    )
                )
                .blur(48.dp)
                .align(Alignment.TopEnd)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AICoachPreview() {
    DamAndroidTheme {
        AICoach(
            onBack = { },
            onStartChallenge = { },
            onFindPartner = { }
        )
    }
}

