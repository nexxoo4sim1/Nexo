package com.example.damandroid.presentation.achievements.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.damandroid.domain.model.AchievementBadge
import com.example.damandroid.domain.model.AchievementChallenge
import com.example.damandroid.domain.model.AchievementUserStats
import com.example.damandroid.domain.model.AchievementsOverview
import com.example.damandroid.domain.model.LeaderboardEntry
import com.example.damandroid.presentation.achievements.model.AchievementsTab
import com.example.damandroid.presentation.achievements.model.AchievementsUiState
import com.example.damandroid.presentation.achievements.viewmodel.AchievementsViewModel
import com.example.damandroid.ui.theme.AppThemeColors
import com.example.damandroid.ui.theme.LocalThemeController
import com.example.damandroid.ui.theme.rememberAppThemeColors

@Composable
fun AchievementsRoute(
    viewModel: AchievementsViewModel,
    onBack: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    AchievementsScreen(
        state = uiState,
        onBack = onBack,
        onTabSelected = viewModel::onTabSelected,
        onRefresh = viewModel::refresh,
        modifier = modifier
    )
}

@Composable
fun AchievementsScreen(
    state: AchievementsUiState,
    onBack: (() -> Unit)?,
    onTabSelected: (tab: AchievementsTab) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    when {
        state.isLoading -> LoadingState(modifier)
        state.error != null -> ErrorState(message = state.error, onRetry = onRefresh, modifier = modifier)
        else -> {
            val overview = state.overview ?: sampleAchievementsOverview
            LegacyAchievementsContent(
                overview = overview,
                selectedTab = state.selectedTab,
                onBack = onBack,
                onTabSelected = onTabSelected,
                modifier = modifier
            )
        }
    }
}

@Composable
private fun LegacyAchievementsContent(
    overview: com.example.damandroid.domain.model.AchievementsOverview,
    selectedTab: AchievementsTab,
    onBack: (() -> Unit)?,
    onTabSelected: (AchievementsTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundBrush = remember {
        Brush.linearGradient(
            listOf(
                Color(0xFFE8D5F2),
                Color(0xFFFFE4F1),
                Color(0xFFE5E5F0)
            )
        )
    }
    val headerSurface = Color.White.copy(alpha = 0.4f)
    val headerBorder = Color.White.copy(alpha = 0.6f)
    val primaryText = Color(0xFF1A202C)
    val secondaryText = Color(0xFF4B5563)
    val mutedText = Color(0xFF6B7280)
    val accentPurple = Color(0xFFA855F7)
    val accentPink = Color(0xFFEC4899)
    val accentBlue = Color(0xFF2563EB)
    val accentGold = Color(0xFFFBBF24)
    val accentTeal = Color(0xFF86EFAC)
    val progressTrack = Color(0xFFE5E7EB)
    val progressActive = accentPurple
    val iconOnAccent = Color.White

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundBrush)
    ) {
        LegacyFloatingAchievementsOrbs(
            primary = accentPurple,
            secondary = accentPink,
            tertiary = accentBlue
        )

        Column(modifier = Modifier.fillMaxSize()) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
                color = headerSurface,
                border = BorderStroke(1.dp, headerBorder)
            ) {
                Column {
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
                            if (onBack != null) {
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
                            }
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "🏆", fontSize = 24.sp)
                                Text(
                                    text = "Achievements",
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = primaryText,
                                    letterSpacing = (-0.5).sp
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(40.dp))
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 16.dp)
                    ) {
                        LegacyLevelCard(
                            stats = overview.stats,
                            primaryText = primaryText,
                            labelColor = Color(0xFF92400E),
                            valueColor = Color(0xFF78350F),
                            progressActive = progressActive,
                            progressTrack = progressTrack
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            LegacyQuickStatCard(
                                icon = Icons.Default.Star,
                                value = overview.stats.totalBadges.toString(),
                                label = "Badges",
                                iconColor = accentGold,
                                primaryText = primaryText,
                                mutedText = mutedText,
                                modifier = Modifier.weight(1f)
                            )
                            LegacyQuickStatCard(
                                icon = Icons.Default.FlashOn,
                                value = overview.stats.currentStreak.toString(),
                                label = "Day Streak",
                                iconColor = accentPink,
                                primaryText = primaryText,
                                mutedText = mutedText,
                                modifier = Modifier.weight(1f)
                            )
                            LegacyQuickStatCard(
                                icon = Icons.Default.TrendingUp,
                                value = overview.stats.longestStreak.toString(),
                                label = "Best Streak",
                                iconColor = accentBlue,
                                primaryText = primaryText,
                                mutedText = mutedText,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                shape = RoundedCornerShape(16.dp),
                color = Color.White.copy(alpha = 0.4f),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.6f))
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(6.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        LegacyTabChip(
                            text = "Badges",
                            selected = selectedTab == AchievementsTab.BADGES,
                            onClick = { onTabSelected(AchievementsTab.BADGES) },
                            selectedBrush = Brush.linearGradient(listOf(accentPurple, accentPink)),
                            selectedTextColor = iconOnAccent,
                            unselectedTextColor = mutedText,
                            modifier = Modifier.weight(1f)
                        )
                        LegacyTabChip(
                            text = "Challenges",
                            selected = selectedTab == AchievementsTab.CHALLENGES,
                            onClick = { onTabSelected(AchievementsTab.CHALLENGES) },
                            selectedBrush = Brush.linearGradient(listOf(accentPurple, accentPink)),
                            selectedTextColor = iconOnAccent,
                            unselectedTextColor = mutedText,
                            modifier = Modifier.weight(1f)
                        )
                        LegacyTabChip(
                            text = "Leaderboard",
                            selected = selectedTab == AchievementsTab.LEADERBOARD,
                            onClick = { onTabSelected(AchievementsTab.LEADERBOARD) },
                            selectedBrush = Brush.linearGradient(listOf(accentPurple, accentPink)),
                            selectedTextColor = iconOnAccent,
                            unselectedTextColor = mutedText,
                            modifier = Modifier.weight(1f)
                        )
                    }

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
                            AchievementsTab.BADGES -> {
                                val chunks = overview.badges.chunked(2)
                                items(chunks) { rowBadges ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        rowBadges.forEach { badge ->
                                            LegacyBadgeCard(
                                                badge = badge,
                                                primaryText = primaryText,
                                                secondaryText = secondaryText,
                                                mutedText = mutedText,
                                                progressActive = progressActive,
                                                progressTrack = progressTrack,
                                                iconOnAccent = iconOnAccent,
                                                modifier = Modifier.weight(1f)
                                            )
                                        }
                                        if (rowBadges.size == 1) {
                                            Spacer(modifier = Modifier.weight(1f))
                                        }
                                    }
                                }
                            }

                            AchievementsTab.CHALLENGES -> {
                                items(overview.challenges) { challenge ->
                                    LegacyChallengeCard(
                                        challenge = challenge,
                                        primaryText = primaryText,
                                        secondaryText = secondaryText,
                                        mutedText = mutedText,
                                        progressActive = progressActive,
                                        progressTrack = progressTrack,
                                        iconOnAccent = iconOnAccent,
                                        gradient = Brush.linearGradient(listOf(accentPurple, accentPink))
                                    )
                                }
                            }

                            AchievementsTab.LEADERBOARD -> {
                                item {
                                    LegacyLeaderboardHeader(
                                        primaryText = primaryText,
                                        iconOnAccent = iconOnAccent,
                                        gradient = Brush.linearGradient(
                                            listOf(
                                                Color(0xFFFDE68A).copy(alpha = 0.6f),
                                                Color(0xFFF97316).copy(alpha = 0.5f)
                                            )
                                        )
                                    )
                                }
                                items(overview.leaderboard) { entry ->
                                    LegacyLeaderboardRow(
                                        entry = entry,
                                        primaryText = primaryText,
                                        secondaryText = secondaryText,
                                        mutedText = mutedText,
                                        accentGold = accentGold,
                                        chipSurface = Color.White.copy(alpha = 0.8f),
                                        chipBorder = Color.White.copy(alpha = 0.6f),
                                        currentUserBackground = Color(0xFFFDF4FF).copy(alpha = 0.7f),
                                        iconOnAccent = iconOnAccent
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
private fun LoadingState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorState(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = message, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(onClick = onRetry) {
                Text(text = "Retry")
            }
        }
    }
}

@Composable
private fun LegacyLevelCard(
    stats: AchievementUserStats,
    primaryText: Color,
    labelColor: Color,
    valueColor: Color,
    progressActive: Color,
    progressTrack: Color
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.linearGradient(
                    listOf(
                        Color(0xFFA855F7),
                        Color(0xFFEC4899)
                    )
                )
            )
            .border(1.dp, Color.White.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
            .padding(12.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Your Level", fontSize = 12.sp, color = Color.White.copy(alpha = 0.85f))
                    Text(text = "Level ${stats.level}", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(text = "XP Progress", fontSize = 12.sp, color = Color.White.copy(alpha = 0.85f))
                    Text(
                        text = "${stats.xp} / ${stats.nextLevelXp}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            LinearProgressIndicator(
                progress = { stats.xp.toFloat() / stats.nextLevelXp.toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = progressActive,
                trackColor = Color.White.copy(alpha = 0.2f)
            )
        }
    }
}

@Composable
private fun LegacyQuickStatCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    value: String,
    label: String,
    iconColor: Color,
    primaryText: Color,
    mutedText: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = Color.White.copy(alpha = 0.85f),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.6f)),
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(18.dp))
            Text(text = value, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = primaryText)
            Text(text = label, fontSize = 11.sp, color = mutedText)
        }
    }
}

@Composable
private fun LegacyTabChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    selectedBrush: Brush,
    selectedTextColor: Color,
    unselectedTextColor: Color,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(36.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        contentPadding = PaddingValues(0.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = if (selected) 2.dp else 0.dp)
    ) {
        val shape = RoundedCornerShape(12.dp)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .then(
                    if (selected) {
                        Modifier.background(selectedBrush, shape)
                    } else {
                        Modifier.background(Color.Transparent, shape)
                    }
                )
                .border(
                    BorderStroke(1.dp, if (selected) Color.Transparent else Color.White.copy(alpha = 0.5f)),
                    shape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                fontSize = 13.sp,
                fontWeight = if (selected) FontWeight.Medium else FontWeight.Normal,
                color = if (selected) selectedTextColor else unselectedTextColor
            )
        }
    }
}

@Composable
private fun LegacyBadgeCard(
    badge: AchievementBadge,
    primaryText: Color,
    secondaryText: Color,
    mutedText: Color,
    progressActive: Color,
    progressTrack: Color,
    iconOnAccent: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (badge.unlocked) Color.White.copy(alpha = 0.9f) else Color.White.copy(alpha = 0.4f)
        ),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.6f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = badge.icon, fontSize = 42.sp, modifier = Modifier.alpha(if (badge.unlocked) 1f else 0.6f))
            Text(text = badge.title, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = primaryText, textAlign = TextAlign.Center)
            Text(text = badge.description, fontSize = 12.sp, color = mutedText, textAlign = TextAlign.Center)
            Text(text = "Category: ${badge.category}", fontSize = 11.sp, color = secondaryText)

            if (badge.unlocked) {
                badge.unlockedDate?.let {
                    Text(text = "Unlocked on $it", fontSize = 11.sp, color = mutedText)
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth()) {
                    LinearProgressIndicator(
                        progress = {
                            val progress = badge.progress ?: 0
                            val total = badge.total ?: 1
                            progress.toFloat() / total.toFloat()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = progressActive,
                        trackColor = progressTrack
                    )
                    Text(
                        text = "${badge.progress ?: 0}/${badge.total ?: 0} progress",
                        fontSize = 11.sp,
                        color = mutedText
                    )
                }
            }
        }
    }
}

@Composable
private fun LegacyChallengeCard(
    challenge: AchievementChallenge,
    primaryText: Color,
    secondaryText: Color,
    mutedText: Color,
    progressActive: Color,
    progressTrack: Color,
    iconOnAccent: Color,
    gradient: Brush
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f)),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.6f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(gradient),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Default.Star, contentDescription = null, tint = iconOnAccent, modifier = Modifier.size(20.dp))
                }
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = challenge.title, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = primaryText)
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color.White.copy(alpha = 0.8f),
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.6f))
                        ) {
                            Text(
                                text = challenge.deadline,
                                fontSize = 10.sp,
                                color = mutedText,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                    Text(text = challenge.description, fontSize = 12.sp, color = secondaryText)
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Progress", fontSize = 12.sp, color = mutedText)
                    Text(text = "${challenge.progress}/${challenge.total}", fontSize = 12.sp, color = primaryText, fontWeight = FontWeight.Medium)
                }
                LinearProgressIndicator(
                    progress = { challenge.progress.toFloat() / challenge.total.toFloat() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = progressActive,
                    trackColor = progressTrack
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(text = "🏆", fontSize = 14.sp)
                Text(text = "Reward: ${challenge.reward}", fontSize = 11.sp, color = secondaryText)
            }
        }
    }
}

@Composable
private fun LegacyLeaderboardHeader(
    primaryText: Color,
    iconOnAccent: Color,
    gradient: Brush
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.6f))
    ) {
        Box(
            modifier = Modifier
                .background(gradient, RoundedCornerShape(16.dp))
                .padding(12.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(imageVector = Icons.Default.Leaderboard, contentDescription = null, tint = iconOnAccent, modifier = Modifier.size(18.dp))
                    Text(text = "Weekly Leaderboard", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = iconOnAccent)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Top performers this week. Keep going to reach the top!",
                    fontSize = 12.sp,
                    color = iconOnAccent.copy(alpha = 0.85f)
                )
            }
        }
    }
}

@Composable
private fun LegacyLeaderboardRow(
    entry: LeaderboardEntry,
    primaryText: Color,
    secondaryText: Color,
    mutedText: Color,
    accentGold: Color,
    chipSurface: Color,
    chipBorder: Color,
    currentUserBackground: Color,
    iconOnAccent: Color
) {
    val isCurrentUser = entry.name.equals("You", ignoreCase = true)
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = if (isCurrentUser) currentUserBackground else Color.White.copy(alpha = 0.9f)),
        border = BorderStroke(if (isCurrentUser) 2.dp else 1.dp, if (isCurrentUser) accentGold else Color.White.copy(alpha = 0.6f)),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isCurrentUser) 4.dp else 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(32.dp),
                shape = CircleShape,
                color = if (entry.badge.isNotEmpty()) accentGold.copy(alpha = 0.2f) else chipSurface,
                border = BorderStroke(1.dp, if (entry.badge.isNotEmpty()) accentGold else chipBorder)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    if (entry.badge.isNotEmpty()) {
                        Text(text = entry.badge, fontSize = 16.sp)
                    } else {
                        Text(text = entry.rank.toString(), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = primaryText)
                    }
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(text = entry.name, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = primaryText)
                Text(text = "${entry.points} XP", fontSize = 12.sp, color = mutedText)
            }
            if (isCurrentUser) {
                Surface(shape = RoundedCornerShape(8.dp), color = accentGold) {
                    Text(text = "You", fontSize = 11.sp, color = iconOnAccent, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp))
                }
            }
        }
    }
}

@Composable
private fun LegacyFloatingAchievementsOrbs(
    primary: Color,
    secondary: Color,
    tertiary: Color
) {
    Box(modifier = Modifier.fillMaxSize()) {
        val transition1 = rememberInfiniteTransition(label = "legacy-orb-1")
        val alpha1 by transition1.animateFloat(
            initialValue = 0.35f,
            targetValue = 0.15f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 2800, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "legacy-orb-1-alpha"
        )

        Box(
            modifier = Modifier
                .offset(x = (-8).dp, y = (-12).dp)
                .size(288.dp)
                .clip(CircleShape)
                .background(Brush.radialGradient(listOf(primary.copy(alpha = alpha1), Color.Transparent)))
                .blur(48.dp)
                .align(Alignment.TopStart)
        )

        val transition2 = rememberInfiniteTransition(label = "legacy-orb-2")
        val alpha2 by transition2.animateFloat(
            initialValue = 0.28f,
            targetValue = 0.12f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 3200, delayMillis = 600, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "legacy-orb-2-alpha"
        )

        Box(
            modifier = Modifier
                .offset(x = (-12).dp, y = (-20).dp)
                .size(384.dp)
                .clip(CircleShape)
                .background(Brush.radialGradient(listOf(secondary.copy(alpha = alpha2), Color.Transparent)))
                .blur(48.dp)
                .align(Alignment.BottomEnd)
        )

        val transition3 = rememberInfiniteTransition(label = "legacy-orb-3")
        val alpha3 by transition3.animateFloat(
            initialValue = 0.25f,
            targetValue = 0.1f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 3000, delayMillis = 1500, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "legacy-orb-3-alpha"
        )

        Box(
            modifier = Modifier
                .offset(x = 24.dp, y = 120.dp)
                .size(220.dp)
                .clip(CircleShape)
                .background(Brush.radialGradient(listOf(tertiary.copy(alpha = alpha3), Color.Transparent)))
                .blur(44.dp)
                .align(Alignment.TopEnd)
        )
    }
}

@Composable
private fun ContentState(
    state: AchievementsUiState,
    onBack: (() -> Unit)?,
    onTabSelected: (tab: AchievementsTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val overview = state.overview ?: return
    val themeController = LocalThemeController.current
    val appColors = rememberAppThemeColors(themeController.isDarkMode)
    val palette = rememberAchievementsPalette(appColors)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(palette.background)
    ) {
        FloatingAchievementsOrbs(palette)

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { AchievementsHeader(onBack = onBack, palette = palette) }
            item { StatsSection(stats = overview.stats, palette = palette) }
            item {
                AchievementsTabs(
                    selectedTab = state.selectedTab,
                    onTabSelected = onTabSelected,
                    palette = palette
                )
            }

            when (state.selectedTab) {
                AchievementsTab.BADGES -> {
                    items(overview.badges, key = { it.id }) { badge ->
                        BadgeCard(badge = badge, palette = palette)
                    }
                }
                AchievementsTab.CHALLENGES -> {
                    items(overview.challenges, key = { it.id }) { challenge ->
                        ChallengeCard(challenge = challenge, palette = palette)
                    }
                }
                AchievementsTab.LEADERBOARD -> {
                    item { LeaderboardHeader(palette = palette) }
                    items(overview.leaderboard, key = { "${it.rank}-${it.name}" }) { entry ->
                        LeaderboardRow(entry = entry, palette = palette)
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }
        }
    }
}

// region Header & Stats

@Composable
private fun AchievementsHeader(
    onBack: (() -> Unit)?,
    palette: AchievementsPalette
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = palette.glassSurface),
        border = BorderStroke(1.dp, palette.glassBorder),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (onBack != null) {
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
                }
                Text(
                    text = "🏆 Achievements",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = palette.primaryText,
                    letterSpacing = (-0.5).sp
                )
            }
            Icon(
                imageVector = Icons.Default.Leaderboard,
                contentDescription = null,
                tint = palette.secondaryText
            )
        }
    }
}

@Composable
private fun StatsSection(
    stats: AchievementUserStats,
    palette: AchievementsPalette
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        LevelCard(stats = stats, palette = palette)
        QuickStatsRow(stats = stats, palette = palette)
    }
}

@Composable
private fun LevelCard(
    stats: AchievementUserStats,
    palette: AchievementsPalette
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = palette.levelCardBackground),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, palette.levelCardBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Your Level",
                        fontSize = 12.sp,
                        color = palette.levelLabel
                    )
                    Text(
                        text = "Level ${stats.level}",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = palette.levelValue
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "XP Progress",
                        fontSize = 12.sp,
                        color = palette.levelLabel
                    )
                    Text(
                        text = "${stats.xp} / ${stats.nextLevelXp}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = palette.levelValue
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            LinearProgressIndicator(
                progress = { stats.xp.toFloat() / stats.nextLevelXp.toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(6.dp)),
                color = palette.progressActive,
                trackColor = palette.progressTrack
            )
        }
    }
}

@Composable
private fun QuickStatsRow(
    stats: AchievementUserStats,
    palette: AchievementsPalette
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        QuickStatCard(
            icon = Icons.Default.EmojiEvents,
            label = "Badges",
            value = stats.totalBadges.toString(),
            palette = palette,
            modifier = Modifier.weight(1f)
        )
        QuickStatCard(
            icon = Icons.Default.Whatshot,
            label = "Day Streak",
            value = stats.currentStreak.toString(),
            palette = palette,
            modifier = Modifier.weight(1f)
        )
        QuickStatCard(
            icon = Icons.Default.TrendingUp,
            label = "Best Streak",
            value = stats.longestStreak.toString(),
            palette = palette,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun QuickStatCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    palette: AchievementsPalette,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = palette.statCardBackground),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, palette.statCardBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = palette.primaryText,
                modifier = Modifier.size(22.dp)
            )
            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = palette.primaryText
            )
            Text(
                text = label,
                fontSize = 12.sp,
                color = palette.secondaryText
            )
        }
    }
}

@Composable
private fun AchievementsTabs(
    selectedTab: AchievementsTab,
    onTabSelected: (AchievementsTab) -> Unit,
    palette: AchievementsPalette
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = palette.tabBackground),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, palette.tabBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            TabButton(
                text = "Badges",
                selected = selectedTab == AchievementsTab.BADGES,
                onClick = { onTabSelected(AchievementsTab.BADGES) },
                palette = palette,
                modifier = Modifier.weight(1f)
            )
            TabButton(
                text = "Challenges",
                selected = selectedTab == AchievementsTab.CHALLENGES,
                onClick = { onTabSelected(AchievementsTab.CHALLENGES) },
                palette = palette,
                modifier = Modifier.weight(1f)
            )
            TabButton(
                text = "Leaderboard",
                selected = selectedTab == AchievementsTab.LEADERBOARD,
                onClick = { onTabSelected(AchievementsTab.LEADERBOARD) },
                palette = palette,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun TabButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    palette: AchievementsPalette,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(40.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected) palette.tabSelected else Color.Transparent,
            contentColor = if (selected) palette.tabSelectedText else palette.secondaryText
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = if (selected) 2.dp else 0.dp)
    ) {
        Text(
            text = text,
            fontSize = 13.sp,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium
        )
    }
}

@Composable
private fun BadgeCard(
    badge: AchievementBadge,
    palette: AchievementsPalette
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = palette.badgeCardBackground),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, palette.badgeCardBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.size(60.dp),
                    shape = CircleShape,
                    color = rarityColor(badge.rarity, palette).copy(alpha = 0.2f),
                    border = BorderStroke(1.dp, rarityColor(badge.rarity, palette))
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = badge.icon, fontSize = 26.sp)
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = badge.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = palette.primaryText
                    )
                    Text(
                        text = badge.description,
                        fontSize = 13.sp,
                        color = palette.secondaryText
                    )
                }

                BadgeStatusChip(unlocked = badge.unlocked, palette = palette)
            }

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = badge.category,
                    fontSize = 12.sp,
                    color = palette.secondaryText
                )
                badge.unlockedDate?.let {
                    Text(
                        text = it,
                        fontSize = 12.sp,
                        color = palette.secondaryText
                    )
                }
            }

            if (!badge.unlocked && badge.progress != null && badge.total != null) {
                LinearProgressIndicator(
                    progress = { badge.progress.toFloat() / badge.total.toFloat() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = palette.progressActive,
                    trackColor = palette.progressTrack
                )
                Text(
                    text = "${badge.progress} / ${badge.total}",
                    fontSize = 12.sp,
                    color = palette.secondaryText
                )
            }
        }
    }
}

@Composable
private fun BadgeStatusChip(
    unlocked: Boolean,
    palette: AchievementsPalette
) {
    val label = if (unlocked) "Unlocked" else "In progress"
    val color = if (unlocked) palette.success else palette.warning
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = color.copy(alpha = 0.18f),
        border = BorderStroke(1.dp, color.copy(alpha = 0.5f))
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            color = color,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun ChallengeCard(
    challenge: AchievementChallenge,
    palette: AchievementsPalette
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = palette.challengeCardBackground),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, palette.challengeCardBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Insights,
                    contentDescription = null,
                    tint = palette.primaryText,
                    modifier = Modifier.size(24.dp)
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = challenge.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = palette.primaryText
                    )
                    Text(
                        text = challenge.description,
                        fontSize = 13.sp,
                        color = palette.secondaryText
                    )
                }
            }

            LinearProgressIndicator(
                progress = { challenge.progress.toFloat() / challenge.total.toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = palette.progressActive,
                trackColor = palette.progressTrack
            )

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${challenge.progress} / ${challenge.total}",
                    fontSize = 12.sp,
                    color = palette.secondaryText
                )
                Text(
                    text = challenge.deadline,
                    fontSize = 12.sp,
                    color = palette.warning
                )
            }

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = palette.glassSurface.copy(alpha = 0.6f),
                border = BorderStroke(1.dp, palette.glassBorder)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = null,
                        tint = palette.success,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = challenge.reward,
                        fontSize = 12.sp,
                        color = palette.primaryText
                    )
                }
            }
        }
    }
}

@Composable
private fun LeaderboardHeader(palette: AchievementsPalette) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Leaderboard",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = palette.primaryText
        )
        Icon(
            imageVector = Icons.Default.CalendarToday,
            contentDescription = null,
            tint = palette.secondaryText
        )
    }
}

@Composable
private fun LeaderboardRow(
    entry: LeaderboardEntry,
    palette: AchievementsPalette
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = palette.leaderboardCardBackground),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, palette.leaderboardCardBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = entry.rank.toString(),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = palette.primaryText
            )
            Text(
                text = entry.badge.ifBlank { "${entry.rank}°" },
                fontSize = 20.sp
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = entry.name,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = palette.primaryText
                )
                Text(
                    text = "${entry.points} pts",
                    fontSize = 12.sp,
                    color = palette.secondaryText
                )
            }
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = palette.accentGold.copy(alpha = 0.8f)
            )
        }
    }
}

// region Palette & Background

private data class AchievementsPalette(
    val background: Brush,
    val glassSurface: Color,
    val glassBorder: Color,
    val primaryText: Color,
    val secondaryText: Color,
    val mutedText: Color,
    val success: Color,
    val warning: Color,
    val accentGold: Color,
    val accentPurple: Color,
    val accentPink: Color,
    val accentBlue: Color,
    val accentTeal: Color,
    val progressActive: Color,
    val progressTrack: Color,
    val levelLabel: Color,
    val levelValue: Color,
    val levelCardBackground: Color,
    val levelCardBorder: Color,
    val statCardBackground: Color,
    val statCardBorder: Color,
    val tabBackground: Color,
    val tabBorder: Color,
    val tabSelected: Color,
    val tabSelectedText: Color,
    val badgeCardBackground: Color,
    val badgeCardBorder: Color,
    val challengeCardBackground: Color,
    val challengeCardBorder: Color,
    val leaderboardCardBackground: Color,
    val leaderboardCardBorder: Color
)

@Composable
private fun rememberAchievementsPalette(appColors: AppThemeColors): AchievementsPalette {
    return AchievementsPalette(
        background = appColors.backgroundGradient,
        glassSurface = appColors.glassSurface,
        glassBorder = appColors.glassBorder,
        primaryText = appColors.primaryText,
        secondaryText = appColors.secondaryText,
        mutedText = appColors.mutedText,
        success = appColors.accentGreen,
        warning = appColors.accentOrange,
        accentGold = appColors.accentGold,
        accentPurple = appColors.accentPurple,
        accentPink = appColors.accentPink,
        accentBlue = appColors.accentBlue,
        accentTeal = appColors.accentTeal,
        progressActive = appColors.accentPurple,
        progressTrack = appColors.outline.copy(alpha = 0.4f),
        levelLabel = appColors.secondaryText,
        levelValue = appColors.primaryText,
        levelCardBackground = appColors.glassSurface.copy(alpha = 0.9f),
        levelCardBorder = appColors.accentPurple.copy(alpha = 0.35f),
        statCardBackground = appColors.glassSurface.copy(alpha = 0.85f),
        statCardBorder = appColors.glassBorder.copy(alpha = 0.6f),
        tabBackground = appColors.glassSurface.copy(alpha = 0.8f),
        tabBorder = appColors.glassBorder.copy(alpha = 0.6f),
        tabSelected = appColors.accentPurple,
        tabSelectedText = appColors.iconOnAccent,
        badgeCardBackground = appColors.glassSurface.copy(alpha = 0.9f),
        badgeCardBorder = appColors.glassBorder,
        challengeCardBackground = appColors.glassSurface.copy(alpha = 0.92f),
        challengeCardBorder = appColors.glassBorder,
        leaderboardCardBackground = appColors.glassSurface.copy(alpha = 0.88f),
        leaderboardCardBorder = appColors.glassBorder
    )
}

@Composable
private fun FloatingAchievementsOrbs(palette: AchievementsPalette) {
    Box(modifier = Modifier.fillMaxSize()) {
        val transition1 = rememberInfiniteTransition(label = "achievements-orb1")
        val alpha1 by transition1.animateFloat(
            initialValue = 0.32f,
            targetValue = 0.18f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 3200, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "achievements-orb1-alpha"
        )

        Box(
            modifier = Modifier
                .offset(x = 48.dp, y = 120.dp)
                .size(140.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        listOf(
                            palette.accentPurple.copy(alpha = alpha1),
                            palette.glassSurface.copy(alpha = alpha1 * 0.6f),
                            Color.Transparent
                        )
                    )
                )
                .blur(56.dp)
        )

        val transition2 = rememberInfiniteTransition(label = "achievements-orb2")
        val alpha2 by transition2.animateFloat(
            initialValue = 0.28f,
            targetValue = 0.14f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 3400, delayMillis = 600, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "achievements-orb2-alpha"
        )

        Box(
            modifier = Modifier
                .offset(x = (-40).dp, y = (-60).dp)
                .size(176.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        listOf(
                            palette.accentPink.copy(alpha = alpha2),
                            palette.glassSurface.copy(alpha = alpha2 * 0.55f),
                            Color.Transparent
                        )
                    )
                )
                .blur(60.dp)
                .align(Alignment.BottomEnd)
        )

        val transition3 = rememberInfiniteTransition(label = "achievements-orb3")
        val alpha3 by transition3.animateFloat(
            initialValue = 0.24f,
            targetValue = 0.12f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 3600, delayMillis = 1200, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "achievements-orb3-alpha"
        )

        Box(
            modifier = Modifier
                .size(110.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        listOf(
                            palette.accentBlue.copy(alpha = alpha3),
                            palette.glassSurface.copy(alpha = alpha3 * 0.6f),
                            Color.Transparent
                        )
                    )
                )
                .blur(40.dp)
                .align(Alignment.Center)
        )

        val transition4 = rememberInfiniteTransition(label = "achievements-orb4")
        val alpha4 by transition4.animateFloat(
            initialValue = 0.22f,
            targetValue = 0.11f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 3600, delayMillis = 1800, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "achievements-orb4-alpha"
        )

        Box(
            modifier = Modifier
                .offset(x = (-8).dp, y = 140.dp)
                .size(92.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        listOf(
                            palette.accentTeal.copy(alpha = alpha4),
                            palette.glassSurface.copy(alpha = alpha4 * 0.55f),
                            Color.Transparent
                        )
                    )
                )
                .blur(32.dp)
                .align(Alignment.TopEnd)
        )
    }
}

private fun rarityColor(rarity: String, palette: AchievementsPalette): Color {
    return when (rarity.lowercase()) {
        "common" -> palette.secondaryText
        "uncommon" -> palette.accentTeal
        "rare" -> palette.accentBlue
        "epic" -> palette.accentPurple
        "legendary" -> palette.accentGold
        else -> palette.secondaryText
    }
}

private val sampleAchievementsOverview = AchievementsOverview(
    stats = AchievementUserStats(
        level = 12,
        xp = 2350,
        nextLevelXp = 3000,
        totalBadges = 18,
        currentStreak = 7,
        longestStreak = 21
    ),
    badges = listOf(
        AchievementBadge(
            id = "badge-1",
            icon = "🏃",
            title = "Marathon Runner",
            description = "Completed 5+ running events",
            category = "Running",
            unlocked = true,
            unlockedDate = "Oct 28, 2025",
            rarity = "rare"
        ),
        AchievementBadge(
            id = "badge-2",
            icon = "🏊",
            title = "Water Warrior",
            description = "Joined 10+ swimming sessions",
            category = "Swimming",
            unlocked = true,
            unlockedDate = "Oct 15, 2025",
            rarity = "common"
        ),
        AchievementBadge(
            id = "badge-3",
            icon = "👥",
            title = "Social Butterfly",
            description = "Connected with 25+ athletes",
            category = "Social",
            unlocked = true,
            unlockedDate = "Oct 10, 2025",
            rarity = "uncommon"
        ),
        AchievementBadge(
            id = "badge-4",
            icon = "⭐",
            title = "Top Host",
            description = "Hosted 10+ successful events",
            category = "Hosting",
            unlocked = true,
            unlockedDate = "Oct 5, 2025",
            rarity = "rare"
        ),
        AchievementBadge(
            id = "badge-5",
            icon = "🔥",
            title = "Consistency King",
            description = "Maintain a 30-day streak",
            category = "Consistency",
            unlocked = false,
            progress = 7,
            total = 30,
            rarity = "epic"
        ),
        AchievementBadge(
            id = "badge-6",
            icon = "🌟",
            title = "Early Bird",
            description = "Join 20 morning sessions",
            category = "Participation",
            unlocked = false,
            progress = 12,
            total = 20,
            rarity = "uncommon"
        )
    ),
    challenges = listOf(
        AchievementChallenge(
            id = "challenge-1",
            title = "Weekend Warrior",
            description = "Complete 4 activities this weekend",
            progress = 2,
            total = 4,
            reward = "100 XP + Weekend Badge",
            deadline = "2 days left"
        ),
        AchievementChallenge(
            id = "challenge-2",
            title = "Variety Seeker",
            description = "Try 3 different sports this week",
            progress = 1,
            total = 3,
            reward = "150 XP + Explorer Badge",
            deadline = "5 days left"
        ),
        AchievementChallenge(
            id = "challenge-3",
            title = "Social Sprint",
            description = "Connect with 5 new sport buddies",
            progress = 3,
            total = 5,
            reward = "75 XP",
            deadline = "7 days left"
        )
    ),
    leaderboard = listOf(
        LeaderboardEntry(rank = 1, name = "You", points = 2350, badge = "🥇"),
        LeaderboardEntry(rank = 2, name = "Sarah M.", points = 2280, badge = "🥈"),
        LeaderboardEntry(rank = 3, name = "Mike R.", points = 2150, badge = "🥉"),
        LeaderboardEntry(rank = 4, name = "Emma L.", points = 2020, badge = ""),
        LeaderboardEntry(rank = 5, name = "Alex T.", points = 1980, badge = "")
    )
)
