package com.example.damandroid

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
import com.example.damandroid.ui.theme.LocalThemeController
import com.example.damandroid.ui.theme.AppThemeColors
import com.example.damandroid.ui.theme.rememberAppThemeColors

data class AchievementUserStats(
    val level: Int,
    val xp: Int,
    val nextLevelXp: Int,
    val totalBadges: Int,
    val currentStreak: Int,
    val longestStreak: Int
)

data class Badge(
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

@Composable
fun AchievementsPage(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf("badges") } // "badges", "challenges", "leaderboard"

    val themeController = LocalThemeController.current
    val appColors = rememberAppThemeColors(themeController.isDarkMode)
    val palette = rememberAchievementsPalette(appColors)

    val userStats = AchievementUserStats(
        level = 12,
        xp = 2350,
        nextLevelXp = 3000,
        totalBadges = 18,
        currentStreak = 7,
        longestStreak = 21
    )

    val badges = listOf(
        Badge(
            id = "1",
            icon = "🏃",
            title = "Marathon Runner",
            description = "Completed 5+ running events",
            category = "Running",
            unlocked = true,
            unlockedDate = "Oct 28, 2025",
            rarity = "rare"
        ),
        Badge(
            id = "2",
            icon = "🏊",
            title = "Water Warrior",
            description = "Joined 10+ swimming sessions",
            category = "Swimming",
            unlocked = true,
            unlockedDate = "Oct 15, 2025",
            rarity = "common"
        ),
        Badge(
            id = "3",
            icon = "👥",
            title = "Social Butterfly",
            description = "Connected with 25+ athletes",
            category = "Social",
            unlocked = true,
            unlockedDate = "Oct 10, 2025",
            rarity = "uncommon"
        ),
        Badge(
            id = "4",
            icon = "⭐",
            title = "Top Host",
            description = "Hosted 10+ successful events",
            category = "Hosting",
            unlocked = true,
            unlockedDate = "Oct 5, 2025",
            rarity = "rare"
        ),
        Badge(
            id = "5",
            icon = "🔥",
            title = "Consistency King",
            description = "Maintain a 30-day streak",
            category = "Consistency",
            unlocked = false,
            progress = 7,
            total = 30,
            rarity = "epic"
        ),
        Badge(
            id = "6",
            icon = "🌟",
            title = "Early Bird",
            description = "Join 20 morning sessions",
            category = "Participation",
            unlocked = false,
            progress = 12,
            total = 20,
            rarity = "uncommon"
        )
    )

    val challenges = listOf(
        AchievementChallenge(
            id = "1",
            title = "Weekend Warrior",
            description = "Complete 4 activities this weekend",
            progress = 2,
            total = 4,
            reward = "100 XP + Weekend Badge",
            deadline = "2 days left"
        ),
        AchievementChallenge(
            id = "2",
            title = "Variety Seeker",
            description = "Try 3 different sports this week",
            progress = 1,
            total = 3,
            reward = "150 XP + Explorer Badge",
            deadline = "5 days left"
        ),
        AchievementChallenge(
            id = "3",
            title = "Social Sprint",
            description = "Connect with 5 new sport buddies",
            progress = 3,
            total = 5,
            reward = "75 XP",
            deadline = "7 days left"
        )
    )

    val leaderboard = listOf(
        LeaderboardEntry(rank = 1, name = "You", points = 2350, badge = "🥇"),
        LeaderboardEntry(rank = 2, name = "Sarah M.", points = 2280, badge = "🥈"),
        LeaderboardEntry(rank = 3, name = "Mike R.", points = 2150, badge = "🥉"),
        LeaderboardEntry(rank = 4, name = "Emma L.", points = 2020, badge = ""),
        LeaderboardEntry(rank = 5, name = "Alex T.", points = 1980, badge = "")
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(palette.background)
    ) {
        // Floating Orbs Background
        FloatingAchievementsOrbs(palette)

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
                color = palette.glassSurface,
                border = BorderStroke(1.dp, palette.glassBorder)
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
                                    tint = palette.secondaryText,
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "🏆",
                                    fontSize = 24.sp
                                )
                                Text(
                                    text = "Achievements",
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = palette.primaryText,
                                    letterSpacing = (-0.5).sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(40.dp)) // Balance spacing
                    }

                    // Level & XP
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 16.dp)
                    ) {
                        // Level Card
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(palette.levelGradient)
                                .border(1.dp, palette.progressTrack, RoundedCornerShape(16.dp))
                                .padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(
                                        text = "Your Level",
                                        fontSize = 12.sp,
                                        color = palette.levelLabel,
                                        modifier = Modifier.padding(bottom = 4.dp)
                                    )
                                    Text(
                                        text = "Level ${userStats.level}",
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = palette.levelValue
                                    )
                                }
                                Column(
                                    horizontalAlignment = Alignment.End
                                ) {
                                    Text(
                                        text = "XP Progress",
                                        fontSize = 12.sp,
                                        color = palette.levelLabel,
                                        modifier = Modifier.padding(bottom = 4.dp)
                                    )
                                    Text(
                                        text = "${userStats.xp} / ${userStats.nextLevelXp}",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = palette.levelValue
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            LinearProgressIndicator(
                                progress = { userStats.xp.toFloat() / userStats.nextLevelXp.toFloat() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                            color = palette.progressActive,
                            trackColor = palette.progressTrack
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Quick Stats
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            QuickStatCard(
                                icon = Icons.Default.Star,
                                value = userStats.totalBadges.toString(),
                                label = "Badges",
                                palette = palette,
                                modifier = Modifier.weight(1f)
                            )
                            QuickStatCard(
                                icon = Icons.Default.FlashOn,
                                value = userStats.currentStreak.toString(),
                                label = "Day Streak",
                                palette = palette,
                                modifier = Modifier.weight(1f)
                            )
                            QuickStatCard(
                                icon = Icons.Default.TrendingUp,
                                value = userStats.longestStreak.toString(),
                                label = "Best Streak",
                                palette = palette,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            // Tabs Content
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                shape = RoundedCornerShape(16.dp),
                color = Color.White.copy(alpha = 0.4f),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.6f))
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Tab Buttons
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(6.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        TabButton(
                            text = "Badges",
                            isSelected = selectedTab == "badges",
                            onClick = { selectedTab = "badges" },
                                palette = palette,
                            modifier = Modifier.weight(1f)
                        )
                        TabButton(
                            text = "Challenges",
                            isSelected = selectedTab == "challenges",
                            onClick = { selectedTab = "challenges" },
                                palette = palette,
                            modifier = Modifier.weight(1f)
                        )
                        TabButton(
                            text = "Leaderboard",
                            isSelected = selectedTab == "leaderboard",
                            onClick = { selectedTab = "leaderboard" },
                                palette = palette,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // Content
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        contentPadding = PaddingValues(
                            start = 14.dp,
                            top = 12.dp,
                            end = 14.dp,
                            bottom = 100.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        when (selectedTab) {
                            "badges" -> {
                                items(
                                    items = badges.chunked(2),
                                    key = { it.first().id }
                                ) { rowBadges ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        rowBadges.forEach { badge ->
                                            BadgeCard(
                                                badge = badge,
                                                palette = palette,
                                                modifier = Modifier.weight(1f)
                                            )
                                        }
                                        // Fill empty space if odd number
                                        if (rowBadges.size == 1) {
                                            Spacer(modifier = Modifier.weight(1f))
                                        }
                                    }
                                }
                            }
                            "challenges" -> {
                                items(challenges) { challenge ->
                                    ChallengeCard(challenge = challenge, palette = palette)
                                }
                            }
                            "leaderboard" -> {
                                item {
                                    LeaderboardHeader(palette)
                                }
                                items(leaderboard) { entry ->
                                    LeaderboardEntryCard(
                                        entry = entry,
                                        isCurrentUser = entry.name == "You",
                                        palette = palette
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun QuickStatCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    value: String,
    label: String,
    palette: AchievementsPalette,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = palette.quickStatSurface,
        border = BorderStroke(1.dp, palette.quickStatBorder),
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = palette.quickStatIcon,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = palette.primaryText
            )
            Text(
                text = label,
                fontSize = 10.sp,
                color = palette.mutedText
            )
        }
    }
}

@Composable
private fun TabButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    palette: AchievementsPalette,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(36.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Color.Transparent else Color.Transparent
        ),
        contentPadding = PaddingValues(0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    if (isSelected) {
                        palette.badgeGradient
                    } else {
                        Brush.linearGradient(
                            colors = listOf(Color.Transparent, Color.Transparent)
                        )
                    },
                    RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                fontSize = 13.sp,
                color = if (isSelected) palette.iconOnAccent else palette.mutedText,
                fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
            )
        }
    }
}

@Composable
private fun BadgeCard(
    badge: Badge,
    palette: AchievementsPalette,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (badge.unlocked) palette.cardSurface else palette.glassSurface
        ),
        border = BorderStroke(
            1.dp,
            if (badge.unlocked) palette.cardBorder else palette.glassBorder
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = badge.icon,
                fontSize = 48.sp,
                modifier = Modifier.alpha(if (badge.unlocked) 1f else 0.5f)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = badge.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = palette.primaryText,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = badge.description,
                fontSize = 11.sp,
                color = palette.mutedText,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (badge.unlocked) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color.Transparent
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                palette.badgeGradient,
                                RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = badge.rarity,
                            fontSize = 12.sp,
                            color = palette.iconOnAccent,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                badge.unlockedDate?.let {
                    Text(
                        text = it,
                        fontSize = 10.sp,
                        color = palette.mutedText
                    )
                }
            } else {
                Column {
                    LinearProgressIndicator(
                        progress = { (badge.progress?.toFloat() ?: 0f) / (badge.total?.toFloat() ?: 1f) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = palette.progressActive,
                        trackColor = palette.progressTrack
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${badge.progress}/${badge.total}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = palette.mutedText
                    )
                }
            }
        }
    }
}

@Composable
private fun ChallengeCard(challenge: AchievementChallenge, palette: AchievementsPalette) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = palette.cardSurface
        ),
        border = BorderStroke(1.dp, palette.cardBorder),
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
                                palette.challengeGradient,
                                RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = palette.iconOnAccent,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = challenge.title,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = palette.primaryText
                        )
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = palette.chipSurface,
                            border = BorderStroke(1.dp, palette.chipBorder)
                        ) {
                            Text(
                                text = challenge.deadline,
                                fontSize = 10.sp,
                                color = palette.chipText,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = challenge.description,
                        fontSize = 12.sp,
                        color = palette.mutedText
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
                        color = palette.mutedText
                    )
                    Text(
                        text = "${challenge.progress}/${challenge.total}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = palette.primaryText
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                LinearProgressIndicator(
                    progress = { challenge.progress.toFloat() / challenge.total.toFloat() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = palette.progressActive,
                    trackColor = palette.progressTrack
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🏆",
                    fontSize = 14.sp
                )
                Text(
                    text = "Reward: ${challenge.reward}",
                    fontSize = 11.sp,
                    color = palette.mutedText
                )
            }
        }
    }
}

@Composable
private fun LeaderboardHeader(palette: AchievementsPalette) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        border = BorderStroke(1.dp, palette.cardBorder)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(palette.leaderboardGradient, RoundedCornerShape(16.dp))
                .padding(12.dp)
        ) {
            Column {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = palette.levelLabel,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Weekly Leaderboard",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = palette.levelValue
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Top performers this week. Keep going to reach the top!",
                    fontSize = 12.sp,
                    color = palette.levelLabel
                )
            }
        }
    }
}

@Composable
private fun LeaderboardEntryCard(
    entry: LeaderboardEntry,
    isCurrentUser: Boolean,
    palette: AchievementsPalette
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isCurrentUser) Color.Transparent else palette.cardSurface
        ),
        border = BorderStroke(
            if (isCurrentUser) 2.dp else 1.dp,
            if (isCurrentUser) palette.accentGold else palette.cardBorder
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isCurrentUser) 2.dp else 1.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (isCurrentUser) {
                        Modifier.background(
                            palette.leaderboardGradient,
                            RoundedCornerShape(16.dp)
                        )
                    } else {
                        Modifier
                    }
                )
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.size(32.dp),
                    shape = CircleShape,
                    color = if (isCurrentUser) palette.glassSurface else palette.subtleSurface
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        if (entry.badge.isNotEmpty()) {
                            Text(
                                text = entry.badge,
                                fontSize = 16.sp
                            )
                        } else {
                            Text(
                                text = entry.rank.toString(),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = palette.primaryText
                            )
                        }
                    }
                }
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = entry.name,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = palette.primaryText
                    )
                    Text(
                        text = "${entry.points} XP",
                        fontSize = 12.sp,
                        color = palette.mutedText
                    )
                }
                if (isCurrentUser) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = palette.accentGold
                    ) {
                        Text(
                            text = "You",
                            fontSize = 11.sp,
                            color = palette.iconOnAccent,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FloatingAchievementsOrbs(palette: AchievementsPalette) {
    Box(modifier = Modifier.fillMaxSize()) {
        val infiniteTransition1 = rememberInfiniteTransition(label = "orb1")
        val pulseAlpha1 by infiniteTransition1.animateFloat(
            initialValue = 0.35f,
            targetValue = 0.15f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 2800, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "pulse1"
        )

        Box(
            modifier = Modifier
                .offset(x = (-8).dp, y = (-12).dp)
                .size(288.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            palette.accentPurple.copy(alpha = pulseAlpha1),
                            Color.Transparent
                        )
                    )
                )
                .blur(48.dp)
                .align(Alignment.TopStart)
        )

        val infiniteTransition2 = rememberInfiniteTransition(label = "orb2")
        val pulseAlpha2 by infiniteTransition2.animateFloat(
            initialValue = 0.28f,
            targetValue = 0.12f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 3200, delayMillis = 600, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "pulse2"
        )

        Box(
            modifier = Modifier
                .offset(x = (-12).dp, y = (-20).dp)
                .size(384.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            palette.accentBlue.copy(alpha = pulseAlpha2),
                            Color.Transparent
                        )
                    )
                )
                .blur(48.dp)
                .align(Alignment.BottomEnd)
        )

        val infiniteTransition3 = rememberInfiniteTransition(label = "orb3")
        val pulseAlpha3 by infiniteTransition3.animateFloat(
            initialValue = 0.25f,
            targetValue = 0.1f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 3000, delayMillis = 1500, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "pulse3"
        )

        Box(
            modifier = Modifier
                .offset(x = (-4).dp)
                .size(256.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            palette.accentPink.copy(alpha = pulseAlpha3),
                            Color.Transparent
                        )
                    )
                )
                .blur(48.dp)
                .align(Alignment.TopEnd)
        )
    }
}

private data class AchievementsPalette(
    val background: Brush,
    val glassSurface: Color,
    val glassBorder: Color,
    val cardSurface: Color,
    val cardBorder: Color,
    val quickStatSurface: Color,
    val quickStatBorder: Color,
    val quickStatIcon: Color,
    val subtleSurface: Color,
    val primaryText: Color,
    val secondaryText: Color,
    val mutedText: Color,
    val accentPurple: Color,
    val accentBlue: Color,
    val accentPink: Color,
    val accentGold: Color,
    val levelGradient: Brush,
    val levelLabel: Color,
    val levelValue: Color,
    val progressActive: Color,
    val progressTrack: Color,
    val chipSurface: Color,
    val chipBorder: Color,
    val chipText: Color,
    val cautionText: Color,
    val iconOnAccent: Color,
    val badgeGradient: Brush,
    val challengeGradient: Brush,
    val leaderboardGradient: Brush,
)

@Composable
private fun rememberAchievementsPalette(appColors: AppThemeColors): AchievementsPalette {
    val background = if (appColors.isDark) appColors.backgroundGradient else appColors.backgroundGradient

    val levelGradient = if (appColors.isDark) {
        Brush.linearGradient(
            listOf(
                appColors.accentGold.copy(alpha = 0.25f),
                appColors.accentOrange.copy(alpha = 0.25f)
            )
        )
    } else {
        Brush.linearGradient(
            listOf(
                Color(0xFFFEF3C7),
                Color(0xFFFDE68A)
            )
        )
    }

    val badgeGradient = Brush.linearGradient(
        listOf(appColors.accentPurple, appColors.accentPink)
    )

    val challengeGradient = Brush.linearGradient(
        listOf(appColors.accentGold, appColors.accentOrange)
    )

    val leaderboardGradient = Brush.linearGradient(
        listOf(appColors.accentGold.copy(alpha = if (appColors.isDark) 0.4f else 0.6f), appColors.accentOrange.copy(alpha = if (appColors.isDark) 0.3f else 0.5f))
    )

    return AchievementsPalette(
        background = background,
        glassSurface = appColors.glassSurface,
        glassBorder = appColors.glassBorder,
        cardSurface = appColors.cardSurface,
        cardBorder = appColors.cardBorder,
        quickStatSurface = appColors.subtleSurface,
        quickStatBorder = appColors.subtleBorder,
        quickStatIcon = appColors.accentGold,
        subtleSurface = appColors.subtleSurface,
        primaryText = appColors.primaryText,
        secondaryText = appColors.secondaryText,
        mutedText = appColors.mutedText,
        accentPurple = appColors.accentPurple,
        accentBlue = appColors.accentBlue,
        accentPink = appColors.accentPink,
        accentGold = appColors.accentGold,
        levelGradient = levelGradient,
        levelLabel = if (appColors.isDark) appColors.secondaryText else Color(0xFF92400E),
        levelValue = if (appColors.isDark) appColors.primaryText else Color(0xFF78350F),
        progressActive = if (appColors.isDark) appColors.accentGold else Color(0xFFFBBF24),
        progressTrack = if (appColors.isDark) appColors.glassBorder else Color.White.copy(alpha = 0.4f),
        chipSurface = appColors.subtleSurface,
        chipBorder = appColors.subtleBorder,
        chipText = appColors.primaryText,
        cautionText = appColors.warning,
        iconOnAccent = appColors.iconOnAccent,
        badgeGradient = badgeGradient,
        challengeGradient = challengeGradient,
        leaderboardGradient = leaderboardGradient,
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AchievementsPagePreview() {
    DamAndroidTheme {
        AchievementsPage(
            onBack = { }
        )
    }
}

