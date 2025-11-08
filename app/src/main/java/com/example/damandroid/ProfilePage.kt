package com.example.damandroid

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import coil.compose.AsyncImage
import com.example.damandroid.ui.theme.AppThemeColors
import com.example.damandroid.ui.theme.DamAndroidTheme
import com.example.damandroid.ui.theme.LocalThemeController
import com.example.damandroid.ui.theme.ThemeController
import com.example.damandroid.ui.theme.rememberAppThemeColors

data class UserProfile(
    val name: String,
    val avatar: String,
    val bio: String,
    val location: String,
    val stats: UserStats
)

data class UserStats(
    val sessionsJoined: Int,
    val sessionsHosted: Int,
    val favoriteSports: List<String>
)

data class ProfileActivity(
    val id: String,
    val title: String,
    val sportIcon: String,
    val date: String,
    val time: String
)

@Composable
fun ProfilePage(
    onSettingsClick: () -> Unit,
    onAchievementsClick: (() -> Unit)? = null,
    onLogoutClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val currentUser = UserProfile(
        name = "Alex Thompson",
        avatar = "https://api.dicebear.com/7.x/avataaars/svg?seed=Alex",
        bio = "Fitness enthusiast and outdoor adventurer",
        location = "Los Angeles, CA",
        stats = UserStats(
            sessionsJoined = 24,
            sessionsHosted = 8,
            favoriteSports = listOf("Running", "Swimming", "Hiking", "Cycling")
        )
    )

    val mockActivities = listOf(
        ProfileActivity("1", "Morning Run", "🏃", "Nov 5, 2025", "7:00 AM"),
        ProfileActivity("2", "Swimming Session", "🏊", "Nov 6, 2025", "6:00 PM"),
        ProfileActivity("3", "Hiking Trail", "🥾", "Nov 7, 2025", "8:00 AM")
    )

    val themeController = LocalThemeController.current
    val appTheme = rememberAppThemeColors(themeController.isDarkMode)
    val colors = rememberProfileColors(appTheme)
    val isDarkMode = appTheme.isDark

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colors.backgroundBrush)
    ) {
        // Floating Orbs
        FloatingProfileOrbs(colors)

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            // Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .windowInsetsPadding(WindowInsets.statusBars)
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Dashboard",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.primaryText,
                        letterSpacing = (-0.5).sp
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            onClick = { themeController.toggle() },
                            shape = CircleShape,
                            color = colors.cardBackground,
                            border = BorderStroke(2.dp, colors.cardBorder)
                        ) {
                            Icon(
                                imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                                contentDescription = "Toggle dark mode",
                                tint = colors.primaryText,
                                modifier = Modifier
                                    .size(36.dp)
                                    .padding(8.dp)
                            )
                        }
                        Button(
                            onClick = onSettingsClick,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colors.cardBackground
                            ),
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(2.dp, colors.cardBorder)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = colors.primaryText,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Profile",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = colors.primaryText
                            )
                        }
                    }
                }
            }

            // Profile Info Card
            item {
                ProfileInfoCard(
                    user = currentUser,
                    colors = colors,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            // Achievements Button
            if (onAchievementsClick != null) {
                item {
                    AchievementsButton(
                        colors = colors,
                        onClick = onAchievementsClick,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }

            // Logout Button
            if (onLogoutClick != null) {
                item {
                    LogoutButton(
                        colors = colors,
                        onClick = onLogoutClick,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }

            // Tabs
            item {
                ProfileTabs(
                    user = currentUser,
                    activities = mockActivities,
                    colors = colors,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun FloatingProfileOrbs(colors: ProfileColors) {
    val infiniteTransition = rememberInfiniteTransition(label = "orbs")
    val pulse1 by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse1"
    )
    val pulse2 by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, delayMillis = 500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse2"
    )
    val pulse3 by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, delayMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse3"
    )
    val pulse4 by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, delayMillis = 1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse4"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .offset(x = 40.dp, y = 80.dp)
                .size(128.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            colors.accentPurple.copy(alpha = pulse1 * colors.orbAlpha),
                            colors.accentPink.copy(alpha = pulse1 * colors.orbAlpha),
                            Color.Transparent
                        )
                    ),
                    CircleShape
                )
                .blur(48.dp)
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = (-40).dp, y = (-160).dp)
                .size(160.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            colors.accentBlue.copy(alpha = pulse2 * colors.orbAlpha),
                            colors.accentPurple.copy(alpha = pulse2 * colors.orbAlpha),
                            Color.Transparent
                        )
                    ),
                    CircleShape
                )
                .blur(48.dp)
        )
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .size(96.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            colors.accentPink.copy(alpha = pulse3 * colors.orbAlpha),
                            colors.accentPurple.copy(alpha = pulse3 * colors.orbAlpha),
                            Color.Transparent
                        )
                    ),
                    CircleShape
                )
                .blur(32.dp)
        )
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = (-60).dp, y = 120.dp)
                .size(80.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            colors.accentPurple.copy(alpha = pulse4 * colors.orbAlpha),
                            colors.accentPink.copy(alpha = pulse4 * colors.orbAlpha),
                            Color.Transparent
                        )
                    ),
                    CircleShape
                )
                .blur(24.dp)
        )
    }
}

@Composable
private fun ProfileInfoCard(
    user: UserProfile,
    colors: ProfileColors,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        // Outer glow
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(y = 2.dp)
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            colors.accentPurple.copy(alpha = colors.overlayAlpha),
                            colors.accentPink.copy(alpha = colors.overlayAlpha),
                            colors.accentBlue.copy(alpha = colors.overlayAlpha)
                        )
                    ),
                    RoundedCornerShape(24.dp)
                )
                .blur(16.dp)
        )

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            color = colors.cardBackground,
            border = BorderStroke(2.dp, colors.cardBorder),
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Avatar with edit button
                Box {
                    Box(
                        modifier = Modifier
                            .size(96.dp)
                            .clip(CircleShape)
                            .border(4.dp, colors.cardBorder, CircleShape)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(
                                        colors.accentPurple.copy(alpha = colors.overlayAlpha),
                                        colors.cardBackground,
                                        colors.accentPink.copy(alpha = colors.overlayAlpha)
                                    )
                                )
                            )
                            .blur(16.dp)
                            .offset(x = 0.dp, y = 0.dp)
                    )
                    AsyncImage(
                        model = user.avatar,
                        contentDescription = null,
                        modifier = Modifier
                            .size(96.dp)
                            .clip(CircleShape)
                            .border(4.dp, colors.cardBorder, CircleShape)
                    )
                    Surface(
                        onClick = { /* Edit */ },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(32.dp),
                        shape = CircleShape,
                        color = colors.accentPurple,
                        border = BorderStroke(2.dp, colors.cardBorder)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = colors.primaryText,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(6.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = user.name,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.primaryText
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = user.bio,
                    fontSize = 14.sp,
                    color = colors.secondaryText,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = colors.chipBackground,
                    border = BorderStroke(2.dp, colors.chipBorder)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = colors.secondaryText,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = user.location,
                            fontSize = 14.sp,
                            color = colors.secondaryText
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Stats
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    StatPill(
                        value = user.stats.sessionsJoined.toString(),
                        label = "Joined",
                        colors = colors,
                        modifier = Modifier.weight(1f)
                    )
                    StatPill(
                        value = user.stats.sessionsHosted.toString(),
                        label = "Hosted",
                        colors = colors,
                        modifier = Modifier.weight(1f)
                    )
                    StatPill(
                        value = "⭐ 4.9",
                        label = "Rating",
                        colors = colors,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun StatPill(
    value: String,
    label: String,
    colors: ProfileColors,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = colors.chipBackground,
        border = BorderStroke(2.dp, colors.chipBorder),
        shadowElevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = colors.primaryText
            )
            Text(
                text = label,
                fontSize = 11.sp,
                color = colors.secondaryText,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}

@Composable
private fun AchievementsButton(
    colors: ProfileColors,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(y = 2.dp)
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            colors.accentGold.copy(alpha = colors.overlayAlpha + 0.1f),
                            colors.accentPink.copy(alpha = colors.overlayAlpha + 0.1f)
                        )
                    ),
                    RoundedCornerShape(24.dp)
                )
                .blur(16.dp)
        )
        Surface(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            color = colors.cardBackground,
            border = BorderStroke(2.dp, colors.cardBorder),
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.size(48.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = colors.accentGold,
                    border = BorderStroke(2.dp, colors.cardBorder)
                ) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = null,
                        tint = colors.iconOnAccent,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp)
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Achievements",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = colors.primaryText
                    )
                    Text(
                        text = "View badges & rewards",
                        fontSize = 12.sp,
                        color = colors.secondaryText
                    )
                }
            }
        }
    }
}

@Composable
private fun LogoutButton(
    colors: ProfileColors,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(y = 2.dp)
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            colors.destructive.copy(alpha = colors.overlayAlpha + 0.1f),
                            colors.destructiveBorder.copy(alpha = colors.overlayAlpha + 0.1f)
                        )
                    ),
                    RoundedCornerShape(24.dp)
                )
                .blur(16.dp)
        )
        Surface(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            color = colors.cardBackground,
            border = BorderStroke(2.dp, colors.destructiveBorder),
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.size(48.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = colors.destructive,
                    border = BorderStroke(2.dp, colors.cardBorder)
                ) {
                    Icon(
                        imageVector = Icons.Default.ExitToApp,
                        contentDescription = null,
                        tint = colors.iconOnAccent,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp)
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Logout",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = colors.destructiveText
                    )
                    Text(
                        text = "Sign out of your account",
                        fontSize = 12.sp,
                        color = colors.secondaryText
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileTabs(
    user: UserProfile,
    activities: List<ProfileActivity>,
    colors: ProfileColors,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf("about") }

    Column(modifier = modifier) {
        // Tab List
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            color = colors.cardBackground,
            border = BorderStroke(2.dp, colors.cardBorder),
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                ProfileTabButton(
                    text = "About",
                    isSelected = selectedTab == "about",
                    onClick = { selectedTab = "about" },
                    colors = colors,
                    modifier = Modifier.weight(1f)
                )
                ProfileTabButton(
                    text = "Activities",
                    isSelected = selectedTab == "activities",
                    onClick = { selectedTab = "activities" },
                    colors = colors,
                    modifier = Modifier.weight(1f)
                )
                ProfileTabButton(
                    text = "Medals",
                    isSelected = selectedTab == "achievements",
                    onClick = { selectedTab = "achievements" },
                    colors = colors,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Tab Content
        when (selectedTab) {
            "about" -> AboutTabContent(user = user, colors = colors)
            "activities" -> ActivitiesTabContent(activities = activities, colors = colors)
            "achievements" -> MedalsTabContent(colors = colors)
        }
    }
}

@Composable
private fun ProfileTabButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    colors: ProfileColors,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(36.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) {
                colors.tabSelectedBackground
            } else {
                Color.Transparent
            }
        ),
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(
            text = text,
            fontSize = 13.sp,
            color = if (isSelected) colors.tabSelectedText else colors.tabUnselectedText,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

@Composable
private fun AboutTabContent(user: UserProfile, colors: ProfileColors) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Favorite Sports
        InfoCard(
            title = "Favorite Sports",
            colors = colors,
            content = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    user.stats.favoriteSports.forEach { sport ->
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = colors.chipBackground,
                            border = BorderStroke(2.dp, colors.chipBorder)
                        ) {
                            Text(
                                text = sport,
                                fontSize = 12.sp,
                                color = colors.primaryText,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        )

        // Interests
        InfoCard(
            title = "Interests",
            colors = colors,
            content = {
                Text(
                    text = "Outdoor activities • Fitness challenges • Meeting new people • Trail running • Open water swimming",
                    fontSize = 12.sp,
                    color = colors.secondaryText,
                    lineHeight = 18.sp
                )
            }
        )

        // Skill Levels
        InfoCard(
            title = "Skill Levels",
            colors = colors,
            content = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SkillLevelRow("Running", "Intermediate", colors)
                    SkillLevelRow("Swimming", "Advanced", colors)
                    SkillLevelRow("Hiking", "Intermediate", colors)
                }
            }
        )
    }
}

@Composable
private fun InfoCard(
    title: String,
    colors: ProfileColors,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = colors.cardBackground,
        border = BorderStroke(2.dp, colors.cardBorder),
        shadowElevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = colors.primaryText,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            content()
        }
    }
}

@Composable
private fun SkillLevelRow(sport: String, level: String, colors: ProfileColors) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = sport,
            fontSize = 13.sp,
            color = colors.primaryText
        )
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = colors.chipBackground,
            border = BorderStroke(2.dp, colors.chipBorder)
        ) {
            Text(
                text = level,
                fontSize = 11.sp,
                color = colors.primaryText,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
            )
        }
    }
}

@Composable
private fun ActivitiesTabContent(activities: List<ProfileActivity>, colors: ProfileColors) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Recent Activities",
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = colors.primaryText,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        activities.forEach { activity ->
            ActivityCard(activity = activity, colors = colors)
        }
    }
}

@Composable
private fun ActivityCard(activity: ProfileActivity, colors: ProfileColors) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = colors.cardBackground,
        border = BorderStroke(2.dp, colors.cardBorder),
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(
                modifier = Modifier.size(44.dp),
                shape = RoundedCornerShape(16.dp),
                color = colors.chipBackground,
                border = BorderStroke(2.dp, colors.chipBorder)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = activity.sportIcon,
                        fontSize = 24.sp
                    )
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = activity.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = colors.primaryText
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null,
                        tint = colors.secondaryText,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = activity.date,
                        fontSize = 12.sp,
                        color = colors.secondaryText
                    )
                    Text(
                        text = "•",
                        fontSize = 12.sp,
                        color = colors.secondaryText
                    )
                    Text(
                        text = activity.time,
                        fontSize = 12.sp,
                        color = colors.secondaryText
                    )
                }
            }
        }
    }
}

@Composable
private fun MedalsTabContent(colors: ProfileColors) {
    val medals = listOf(
        Medal("🏃", "Marathon Runner", "Completed 5+ running events", Color(0xFF3498DB)),
        Medal("🏊", "Water Warrior", "Joined 10+ swimming sessions", Color(0xFF2ECC71)),
        Medal("👥", "Social Butterfly", "Connected with 25+ athletes", Color(0xFF9B59B6)),
        Medal("⭐", "Top Host", "Hosted 10+ successful events", Color(0xFFF39C12)),
        Medal("💪", "Consistency King", "30-day activity streak", Color(0xFFE74C3C)),
        Medal("🎯", "Goal Crusher", "Achieved 5 personal goals", Color(0xFF1ABC9C))
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Medals",
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = colors.primaryText,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        medals.forEach { medal ->
            MedalCard(medal = medal, colors = colors)
        }
    }
}

data class Medal(
    val icon: String,
    val title: String,
    val desc: String,
    val color: Color
)

@Composable
private fun MedalCard(medal: Medal, colors: ProfileColors) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = colors.cardBackground,
        border = BorderStroke(2.dp, colors.cardBorder),
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(56.dp),
                shape = RoundedCornerShape(16.dp),
                color = medal.color.copy(alpha = if (colors.isDarkMode) 0.35f else 0.3f),
                border = BorderStroke(2.dp, colors.chipBorder)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = medal.icon,
                        fontSize = 28.sp
                    )
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = medal.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = colors.primaryText
                )
                Text(
                    text = medal.desc,
                    fontSize = 12.sp,
                    color = colors.secondaryText
                )
            }
            Icon(
                imageVector = Icons.Default.EmojiEvents,
                contentDescription = null,
                tint = colors.accentGold,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

private data class ProfileColors(
    val isDarkMode: Boolean,
    val backgroundBrush: Brush,
    val primaryText: Color,
    val secondaryText: Color,
    val cardBackground: Color,
    val cardBorder: Color,
    val chipBackground: Color,
    val chipBorder: Color,
    val accentPurple: Color,
    val accentPink: Color,
    val accentBlue: Color,
    val accentGreen: Color,
    val accentGold: Color,
    val destructive: Color,
    val destructiveBorder: Color,
    val destructiveText: Color,
    val iconOnAccent: Color,
    val tabSelectedBackground: Color,
    val tabSelectedText: Color,
    val tabUnselectedText: Color,
    val overlayAlpha: Float,
    val orbAlpha: Float
)

@Composable
private fun rememberProfileColors(appTheme: AppThemeColors): ProfileColors {
    return remember(appTheme) {
        val isDarkMode = appTheme.isDark
        ProfileColors(
            isDarkMode = isDarkMode,
            backgroundBrush = appTheme.backgroundGradient,
            primaryText = appTheme.primaryText,
            secondaryText = appTheme.secondaryText,
            cardBackground = appTheme.cardSurface,
            cardBorder = appTheme.cardBorder,
            chipBackground = appTheme.subtleSurface,
            chipBorder = appTheme.subtleBorder,
            accentPurple = appTheme.accentPurple,
            accentPink = appTheme.accentPink,
            accentBlue = appTheme.accentBlue,
            accentGreen = appTheme.accentGreen,
            accentGold = appTheme.accentGold,
            destructive = appTheme.danger,
            destructiveBorder = if (isDarkMode) appTheme.danger.copy(alpha = 0.6f) else appTheme.danger.copy(alpha = 0.4f),
            destructiveText = appTheme.danger,
            iconOnAccent = appTheme.iconOnAccent,
            tabSelectedBackground = appTheme.accentPurple,
            tabSelectedText = appTheme.iconOnAccent,
            tabUnselectedText = appTheme.mutedText,
            overlayAlpha = if (isDarkMode) 0.24f else 0.18f,
            orbAlpha = if (isDarkMode) 0.45f else 0.35f
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfilePagePreview() {
    val controller = ThemeController(isDarkMode = false, setDarkMode = {})
    CompositionLocalProvider(LocalThemeController provides controller) {
        DamAndroidTheme(darkTheme = controller.isDarkMode) {
            ProfilePage(
                onSettingsClick = { },
                onAchievementsClick = { }
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfilePageDarkPreview() {
    val controller = ThemeController(isDarkMode = true, setDarkMode = {})
    CompositionLocalProvider(LocalThemeController provides controller) {
        DamAndroidTheme(darkTheme = controller.isDarkMode) {
            ProfilePage(
                onSettingsClick = { },
                onAchievementsClick = { }
            )
        }
    }
}