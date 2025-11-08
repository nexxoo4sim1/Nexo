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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import coil.compose.AsyncImage
import com.example.damandroid.ui.theme.DamAndroidTheme
import com.example.damandroid.ui.theme.LocalThemeController
import com.example.damandroid.ui.theme.ThemeController
import com.example.damandroid.ui.theme.rememberAppThemeColors

data class SportCategory(
    val name: String,
    val icon: String,
    val color: String
)

val sportCategoriesData = listOf(
    SportCategory("Basketball", "🏀", "#FF6B6B"),
    SportCategory("Running", "🏃", "#4ECDC4"),
    SportCategory("Tennis", "🎾", "#FFE66D"),
    SportCategory("Soccer", "⚽", "#95E1D3"),
    SportCategory("Swimming", "🏊", "#3498DB"),
    SportCategory("Cycling", "🚴", "#E74C3C"),
    SportCategory("Yoga", "🧘", "#9B59B6"),
    SportCategory("Gym", "💪", "#F39C12"),
    SportCategory("Hiking", "🥾", "#27AE60")
)

data class ActiveUser(
    val id: String,
    val name: String,
    val avatar: String,
    val sport: String,
    val distance: String
)

val activeUsersData = listOf(
    ActiveUser(
        id = "1",
        name = "Jessica Lee",
        avatar = "https://api.dicebear.com/7.x/avataaars/svg?seed=Jessica",
        sport = "Running",
        distance = "0.3 mi"
    ),
    ActiveUser(
        id = "2",
        name = "Tom Harris",
        avatar = "https://api.dicebear.com/7.x/avataaars/svg?seed=Tom",
        sport = "Cycling",
        distance = "0.7 mi"
    ),
    ActiveUser(
        id = "3",
        name = "Nina Patel",
        avatar = "https://api.dicebear.com/7.x/avataaars/svg?seed=Nina",
        sport = "Yoga",
        distance = "1.2 mi"
    )
)

@Composable
fun SearchDiscovery(
    onBack: (() -> Unit)? = null,
    onCoachClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }

    val themeController = LocalThemeController.current
    val appTheme = rememberAppThemeColors(themeController.isDarkMode)
    val backgroundGradient = appTheme.backgroundGradient
    val primaryText = appTheme.primaryText
    val secondaryText = appTheme.secondaryText
    val mutedText = appTheme.mutedText
    val glassSurface = appTheme.glassSurface
    val glassBorder = appTheme.glassBorder

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                backgroundGradient
            )
    ) {
        // Floating Orbs
        FloatingDiscoveryOrbs()

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header - Sticky
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = glassSurface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                border = BorderStroke(2.dp, glassBorder)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .windowInsetsPadding(WindowInsets.statusBars)
                            .padding(bottom = 10.dp, top = 8.dp),
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
                                    tint = primaryText,
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                        }
                        Text(
                            text = "Discover",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                        color = primaryText,
                            letterSpacing = (-0.5).sp
                        )
                    }

                    // Search Input
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = {
                            Text(
                                "Search sports, people, or places...",
                                color = mutedText.copy(alpha = 0.6f),
                                fontSize = 15.sp
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = mutedText.copy(alpha = 0.6f),
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = primaryText,
                            unfocusedTextColor = primaryText,
                            focusedBorderColor = appTheme.accentPurple.copy(alpha = 0.5f),
                            unfocusedBorderColor = glassBorder,
                            cursorColor = primaryText,
                            focusedContainerColor = glassSurface,
                            unfocusedContainerColor = glassSurface.copy(alpha = 0.9f),
                        ),
                        singleLine = true,
                        textStyle = MaterialTheme.typography.bodyMedium.copy(fontSize = 15.sp)
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
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Featured Coaches
                if (onCoachClick != null) {
                    item {
                        SectionTitle("Verified Coaches")
                        Spacer(modifier = Modifier.height(12.dp))
                        FeaturedCoachCard(onClick = onCoachClick)
                    }
                }

                // Categories
                item {
                    SectionTitle("Browse by Sport")
                    Spacer(modifier = Modifier.height(12.dp))
                    SportCategoriesGrid()
                }

                // Trending Activities
                item {
                    SectionTitle("Trending Near You")
                    Spacer(modifier = Modifier.height(12.dp))
                    TrendingActivitiesList()
                }

                // Active Now
                item {
                    SectionTitle("Active Now")
                    Spacer(modifier = Modifier.height(12.dp))
                    ActiveUsersList()
                }
            }
        }
    }
}

@Composable
private fun FloatingDiscoveryOrbs() {
    val appTheme = rememberAppThemeColors()
    val purple = appTheme.accentPurple
    val pink = appTheme.accentPink
    val blue = appTheme.accentBlue
    val teal = appTheme.accentTeal
    val glassSurface = appTheme.glassSurface

    Box(modifier = Modifier.fillMaxSize()) {
        // Orb 1
        val infiniteTransition1 = rememberInfiniteTransition(label = "orb1")
        val pulseAlpha1 by infiniteTransition1.animateFloat(
            initialValue = 0.3f,
            targetValue = 0.15f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 3000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "pulse1"
        )

        Box(
            modifier = Modifier
                .offset(x = 40.dp, y = 80.dp)
                .size(128.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            purple.copy(alpha = pulseAlpha1),
                            glassSurface.copy(alpha = pulseAlpha1 * 0.67f),
                            Color.Transparent
                        )
                    )
                )
                .blur(48.dp)
        )

        // Orb 2
        val infiniteTransition2 = rememberInfiniteTransition(label = "orb2")
        val pulseAlpha2 by infiniteTransition2.animateFloat(
            initialValue = 0.35f,
            targetValue = 0.175f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 3000, delayMillis = 1000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "pulse2"
        )

        Box(
            modifier = Modifier
                .offset(x = (-40).dp, y = (-40).dp)
                .size(160.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            pink.copy(alpha = pulseAlpha2),
                            glassSurface.copy(alpha = pulseAlpha2 * 0.57f),
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
                .size(96.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            blue.copy(alpha = pulseAlpha3),
                            glassSurface.copy(alpha = pulseAlpha3 * 0.67f),
                            Color.Transparent
                        )
                    )
                )
                .blur(32.dp)
                .align(Alignment.Center)
        )

        // Orb 4
        val infiniteTransition4 = rememberInfiniteTransition(label = "orb4")
        val pulseAlpha4 by infiniteTransition4.animateFloat(
            initialValue = 0.25f,
            targetValue = 0.125f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 3000, delayMillis = 3000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "pulse4"
        )

        Box(
            modifier = Modifier
                .offset(x = (-20).dp, y = (-20).dp)
                .size(80.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            teal.copy(alpha = pulseAlpha4),
                            glassSurface.copy(alpha = pulseAlpha4 * 0.6f),
                            Color.Transparent
                        )
                    )
                )
                .blur(32.dp)
                .align(Alignment.TopEnd)
        )
    }
}

@Composable
private fun SectionTitle(title: String) {
    val appTheme = rememberAppThemeColors()
    Text(
        text = title,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = appTheme.primaryText
    )
}

@Composable
private fun FeaturedCoachCard(onClick: () -> Unit) {
    val theme = rememberAppThemeColors()
    val gradientColors = listOf(
        theme.accentPurple.copy(alpha = 0.15f),
        theme.accentPink.copy(alpha = 0.15f),
        theme.accentBlue.copy(alpha = 0.15f)
    )
    val highlightGradient = Brush.verticalGradient(
        colors = listOf(
            theme.glassSurface.copy(alpha = 0.4f),
            Color.Transparent
        )
    )

    Box {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset(x = 2.dp, y = 2.dp)
                .height(120.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Brush.linearGradient(gradientColors))
                .blur(16.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = theme.glassSurface
            ),
            border = BorderStroke(2.dp, theme.glassBorder),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                        .background(highlightGradient)
                        .align(Alignment.TopStart)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = "https://api.dicebear.com/7.x/avataaars/svg?seed=Alex",
                        contentDescription = null,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .border(2.dp, theme.glassBorder, CircleShape)
                    )

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Alex Thompson",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = theme.primaryText
                            )
                            Surface(
                                modifier = Modifier.size(16.dp),
                                shape = CircleShape,
                                color = theme.success
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "✓",
                                        fontSize = 10.sp,
                                        color = theme.iconOnAccent,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        Text(
                            text = "Certified HIIT & Strength Trainer",
                            fontSize = 12.sp,
                            color = theme.secondaryText,
                            modifier = Modifier.padding(top = 2.dp, bottom = 4.dp)
                        )

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "⭐ 4.8 (124 reviews)",
                                fontSize = 11.sp,
                                color = theme.mutedText
                            )
                            Text(
                                text = "450+ sessions",
                                fontSize = 11.sp,
                                color = theme.mutedText
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .offset(y = (-12).dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(theme.success)
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "Book Session",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = theme.iconOnAccent
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SportCategoriesGrid() {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        sportCategoriesData.chunked(3).forEach { rowCategories ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                rowCategories.forEach { category ->
                    SportCategoryCard(
                        category = category,
                        modifier = Modifier.weight(1f)
                    )
                }
                // Fill empty spaces if row has less than 3 items
                repeat(3 - rowCategories.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun SportCategoryCard(
    category: SportCategory,
    modifier: Modifier = Modifier
) {
    val theme = rememberAppThemeColors()
    val categoryColor = Color(android.graphics.Color.parseColor(category.color))

    Box(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset(x = 2.dp, y = 2.dp)
                .height(90.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            theme.accentPurple.copy(alpha = 0.1f),
                            theme.accentPink.copy(alpha = 0.1f)
                        )
                    )
                )
                .blur(8.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = theme.glassSurface
            ),
            border = BorderStroke(2.dp, theme.glassBorder),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    theme.glassSurface.copy(alpha = 0.4f),
                                    Color.Transparent
                                )
                            )
                        )
                        .align(Alignment.TopStart)
                )

                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(categoryColor.copy(alpha = 0.25f))
                            .border(2.dp, theme.glassBorder, RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = category.icon,
                            fontSize = 20.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = category.name,
                        fontSize = 11.sp,
                        color = theme.primaryText,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun TrendingActivitiesList() {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        mockActivities.take(3).forEach { activity ->
            TrendingActivityCard(activity = activity)
        }
    }
}

@Composable
private fun TrendingActivityCard(activity: Activity) {
    val theme = rememberAppThemeColors()
    val gradientColors = listOf(
        theme.accentPurple.copy(alpha = 0.15f),
        theme.accentPink.copy(alpha = 0.15f),
        theme.accentBlue.copy(alpha = 0.15f)
    )

    Box {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset(x = 2.dp, y = 2.dp)
                .height(100.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Brush.linearGradient(gradientColors))
                .blur(16.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = theme.glassSurface
            ),
            border = BorderStroke(2.dp, theme.glassBorder),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    theme.glassSurface.copy(alpha = 0.4f),
                                    Color.Transparent
                                )
                            )
                        )
                        .align(Alignment.TopStart)
                )

                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(theme.subtleSurface)
                            .border(2.dp, theme.glassBorder, RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = activity.sportIcon,
                            fontSize = 22.sp
                        )
                    }

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = activity.title,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = theme.primaryText,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = theme.mutedText,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = activity.distance,
                                fontSize = 12.sp,
                                color = theme.mutedText
                            )
                            Text(
                                text = "•",
                                fontSize = 12.sp,
                                color = theme.mutedText
                            )
                            Text(
                                text = activity.date,
                                fontSize = 12.sp,
                                color = theme.mutedText
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                model = activity.hostAvatar,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(CircleShape)
                                    .border(1.dp, theme.glassBorder, CircleShape)
                            )
                            Text(
                                text = activity.hostName,
                                fontSize = 12.sp,
                                color = theme.secondaryText
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = theme.glassSurface,
                        border = BorderStroke(2.dp, theme.glassBorder)
                    ) {
                        Text(
                            text = "${activity.spotsTotal - activity.spotsTaken} spots",
                            fontSize = 11.sp,
                            color = theme.success,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ActiveUsersList() {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        activeUsersData.forEach { user ->
            ActiveUserCard(user = user)
        }
    }
}

@Composable
private fun ActiveUserCard(user: ActiveUser) {
    val theme = rememberAppThemeColors()

    Box {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset(x = 2.dp, y = 2.dp)
                .height(80.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            theme.accentTeal.copy(alpha = 0.15f),
                            theme.accentGreen.copy(alpha = 0.1f)
                        )
                    )
                )
                .blur(16.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = theme.glassSurface
            ),
            border = BorderStroke(2.dp, theme.glassBorder),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    theme.glassSurface.copy(alpha = 0.4f),
                                    Color.Transparent
                                )
                            )
                        )
                        .align(Alignment.TopStart)
                )

                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box {
                        AsyncImage(
                            model = user.avatar,
                            contentDescription = null,
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .border(2.dp, theme.glassBorder, CircleShape)
                        )
                        Box(
                            modifier = Modifier
                                .size(14.dp)
                                .offset(x = 30.dp, y = 30.dp)
                                .clip(CircleShape)
                                .background(theme.success)
                                .border(2.dp, theme.glassSurface, CircleShape)
                        )
                    }

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = user.name,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = theme.primaryText
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = user.sport,
                                fontSize = 12.sp,
                                color = theme.mutedText
                            )
                            Text(
                                text = "•",
                                fontSize = 12.sp,
                                color = theme.mutedText
                            )
                            Text(
                                text = "${user.distance} away",
                                fontSize = 12.sp,
                                color = theme.mutedText
                            )
                        }
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        OutlinedButton(
                            onClick = { /* Follow */ },
                            modifier = Modifier.height(28.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = theme.primaryText
                            ),
                            border = BorderStroke(2.dp, theme.glassBorder),
                            contentPadding = PaddingValues(horizontal = 12.dp)
                        ) {
                            Text(
                                text = "Follow",
                                fontSize = 12.sp,
                                color = theme.primaryText
                            )
                        }

                        Button(
                            onClick = { /* Chat */ },
                            modifier = Modifier.height(28.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = theme.success
                            ),
                            contentPadding = PaddingValues(horizontal = 12.dp)
                        ) {
                            Text(
                                text = "Chat",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = theme.iconOnAccent
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SearchDiscoveryPreview() {
    CompositionLocalProvider(
        LocalThemeController provides ThemeController(
            isDarkMode = false,
            setDarkMode = {}
        )
    ) {
        DamAndroidTheme(darkTheme = false) {
            SearchDiscovery(
                onBack = { },
                onCoachClick = { }
            )
        }
    }
}

