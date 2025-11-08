package com.example.damandroid

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.animation.core.*
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.example.damandroid.ui.theme.DamAndroidTheme
import com.example.damandroid.ui.theme.AppThemeColors
import com.example.damandroid.ui.theme.LocalThemeController
import com.example.damandroid.ui.theme.ThemeController
import com.example.damandroid.ui.theme.rememberAppThemeColors

data class Activity(
    val id: String,
    val title: String,
    val sportType: String,
    val sportIcon: String,
    val hostName: String,
    val hostAvatar: String,
    val date: String,
    val time: String,
    val location: String,
    val distance: String,
    val spotsTotal: Int,
    val spotsTaken: Int,
    val level: String
)

// Mock data
val mockActivities = listOf(
    Activity(
        id = "1",
        title = "Basketball at Central Park",
        sportType = "Basketball",
        sportIcon = "🏀",
        hostName = "Mike Johnson",
        hostAvatar = "",
        date = "Today",
        time = "6:00 PM",
        location = "Central Park",
        distance = "0.5 miles",
        spotsTotal = 10,
        spotsTaken = 6,
        level = "Intermediate"
    ),
    Activity(
        id = "2",
        title = "Morning Run Group",
        sportType = "Running",
        sportIcon = "🏃",
        hostName = "Sarah Williams",
        hostAvatar = "",
        date = "Tomorrow",
        time = "7:00 AM",
        location = "Riverside Park",
        distance = "1.2 miles",
        spotsTotal = 15,
        spotsTaken = 8,
        level = "All Levels"
    ),
    Activity(
        id = "3",
        title = "Tennis Doubles",
        sportType = "Tennis",
        sportIcon = "🎾",
        hostName = "David Chen",
        hostAvatar = "",
        date = "Friday",
        time = "5:30 PM",
        location = "City Tennis Club",
        distance = "2.3 miles",
        spotsTotal = 4,
        spotsTaken = 2,
        level = "Advanced"
    )
)

val sportCategories = listOf(
    "Basketball", "Running", "Tennis", "Soccer", "Swimming", "Cycling"
)

@Composable
fun HomeFeed(
    onActivityClick: (Activity) -> Unit,
    onSearchClick: (() -> Unit)? = null,
    onAISuggestionsClick: (() -> Unit)? = null,
    onQuickMatchClick: (() -> Unit)? = null,
    onAIMatchmakerClick: (() -> Unit)? = null,
    onEventDetailsClick: (() -> Unit)? = null,
    onCreateClick: (() -> Unit)? = null,
    onNotificationsClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var savedActivities by remember { mutableStateOf(setOf<String>()) }
    var filterSport by remember { mutableStateOf("all") }
    var filterDistance by remember { mutableStateOf(5f) }
    var showFilterSheet by remember { mutableStateOf(false) }

    val themeController = LocalThemeController.current
    val appColors = rememberAppThemeColors(themeController.isDarkMode)
    val isDark = appColors.isDark

    val backgroundBrush = if (isDark) {
        appColors.backgroundGradient
    } else {
        Brush.linearGradient(
            colors = listOf(
                Color(0xFFF5F3FF),
                Color(0xFFFDF4FF),
                Color(0xFFF0F4F8)
            )
        )
    }

    val headlineText = if (isDark) appColors.primaryText else Color(0xFF1A202C)
    val titleText = if (isDark) appColors.primaryText else Color(0xFF2D3748)
    val subtitleText = if (isDark) appColors.mutedText else Color(0xFF718096)
    val secondaryText = if (isDark) appColors.secondaryText else Color(0xFF4A5568)
    val iconPrimary = if (isDark) appColors.secondaryText else Color(0xFF2D3748)
    val iconMuted = if (isDark) appColors.mutedText else Color(0xFF718096)
    val cardSurface = if (isDark) appColors.cardSurface else Color.White
    val cardBorder = if (isDark) appColors.cardBorder else Color(0xFFE2E8F0)
    val chipBackground = if (isDark) appColors.subtleSurface else Color(0xFFF7FAFC)
    val chipBorder = if (isDark) appColors.subtleBorder else Color(0xFFE2E8F0)
    val accentPurple = appColors.accentPurple
    val accentPink = appColors.accentPink
    val accentBlue = appColors.accentBlue
    val accentGreen = if (isDark) appColors.accentGreen else Color(0xFF86EFAC)
    val accentRed = appColors.danger
    val savedInactive = if (isDark) appColors.subtleBorder else Color(0xFFCBD5E0)
    val searchContainer = cardSurface
    val searchBorder = cardBorder
    val searchPlaceholder = subtitleText
    val searchTextColor = titleText
    val searchIconColor = iconMuted
    val filterButtonBackground = cardSurface
    val filterButtonBorder = cardBorder
    val filterButtonIcon = iconPrimary

    val filteredActivities = mockActivities.filter { activity ->
        val matchesSearch = activity.title.contains(searchQuery, ignoreCase = true) ||
                activity.sportType.contains(searchQuery, ignoreCase = true)
        val matchesSport = filterSport == "all" || activity.sportType == filterSport
        matchesSearch && matchesSport
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundBrush)
    ) {
        // Floating Orbs for Depth
        FloatingOrbs(appColors)

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 16.dp,
                top = 16.dp,
                end = 16.dp,
                bottom = 100.dp
            ),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp, top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Discover",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = headlineText,
                            letterSpacing = (-0.5).sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Near You",
                            fontSize = 16.sp,
                            color = subtitleText,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    
                    // Notification Icon
                    Box(
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .size(48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(
                            onClick = { onNotificationsClick?.invoke() },
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notifications",
                                tint = iconPrimary,
                                modifier = Modifier.size(26.dp)
                            )
                        }
                        // Notification dot
                       /* Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFEF4444))
                                .align(Alignment.TopEnd)
                                .offset(x = 4.dp, y = 2.dp)
                                .border(2.dp, Color.White, CircleShape)
                        )*/
                    }
                }
            }
            
            item {
                Column(
                    modifier = Modifier.padding(bottom = 12.dp)
                ) {
                    // Search and Filter
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            modifier = Modifier.weight(1f),
                            placeholder = { Text("Search activities...", fontSize = 15.sp, color = searchPlaceholder) },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp),
                                    tint = searchIconColor
                                )
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedContainerColor = searchContainer,
                                focusedContainerColor = searchContainer,
                                unfocusedBorderColor = searchBorder,
                                focusedBorderColor = accentPurple,
                                focusedTextColor = searchTextColor,
                                unfocusedTextColor = searchTextColor,
                                focusedPlaceholderColor = searchPlaceholder,
                                unfocusedPlaceholderColor = searchPlaceholder,
                                focusedLeadingIconColor = searchIconColor,
                                unfocusedLeadingIconColor = searchIconColor
                            ),
                            shape = RoundedCornerShape(16.dp),
                            singleLine = true
                        )

                        IconButton(
                            onClick = { showFilterSheet = true },
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    filterButtonBackground,
                                    RoundedCornerShape(16.dp)
                                )
                                .border(1.dp, filterButtonBorder, RoundedCornerShape(16.dp))
                        ) {
                            Icon(
                                imageVector = Icons.Default.FilterList,
                                contentDescription = "Filters",
                                tint = filterButtonIcon,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            // Featured Cards Grid
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (onQuickMatchClick != null) {
                        QuickMatchCard(
                            onClick = onQuickMatchClick,
                            appColors = appColors,
                            titleColor = titleText,
                            subtitleColor = subtitleText,
                            iconBackground = cardSurface,
                            iconBorder = cardBorder,
                            accentPurple = accentPurple,
                            accentPink = accentPink,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (onAIMatchmakerClick != null) {
                        AIMatchmakerCard(
                            onClick = onAIMatchmakerClick,
                            appColors = appColors,
                            titleColor = titleText,
                            subtitleColor = subtitleText,
                            iconBackground = cardSurface,
                            iconBorder = cardBorder,
                            accentPurple = accentPurple,
                            accentBlue = accentBlue,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Explore More Card
            if (onSearchClick != null) {
                item {
                    ExploreMoreCard(
                        onClick = onSearchClick,
                        cardSurface = cardSurface,
                        cardBorder = cardBorder,
                        titleColor = titleText,
                        subtitleColor = subtitleText,
                        iconColor = iconPrimary
                    )
                }
            }

            // Activity List
            items(filteredActivities) { activity ->
                ActivityCard(
                    activity = activity,
                    isSaved = savedActivities.contains(activity.id),
                    onSaveToggle = {
                        savedActivities = if (savedActivities.contains(activity.id)) {
                            savedActivities - activity.id
                        } else {
                            savedActivities + activity.id
                        }
                    },
                    onActivityClick = { onActivityClick(activity) },
                    onDetailsClick = { onEventDetailsClick?.invoke() },
                    appColors = appColors,
                    titleColor = titleText,
                    subtitleColor = subtitleText,
                    secondaryText = secondaryText,
                    iconColor = iconMuted,
                    cardSurface = cardSurface,
                    cardBorder = cardBorder,
                    chipBackground = chipBackground,
                    chipBorder = chipBorder,
                    accentGreen = accentGreen,
                    accentRed = accentRed,
                    savedInactive = savedInactive
                )
            }
        }

        // Floating Create Button
        if (onCreateClick != null) {
            FloatingCreateButton(
                onClick = onCreateClick,
                appColors = appColors,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp, bottom = 90.dp) // Position above bottom nav
                    .zIndex(10f) // Ensure it's above other content
            )
        }

        // Filter Sheet
        if (showFilterSheet) {
            FilterSheet(
                filterSport = filterSport,
                filterDistance = filterDistance,
                onSportChange = { filterSport = it },
                onDistanceChange = { filterDistance = it },
                onDismiss = { showFilterSheet = false },
                appColors = appColors,
                titleColor = titleText,
                subtitleColor = subtitleText,
                secondaryText = secondaryText,
                iconColor = iconPrimary,
                cardSurface = cardSurface,
                cardBorder = cardBorder,
                accentPurple = accentPurple
            )
        }
    }
}

@Composable
private fun FloatingOrbs(appColors: AppThemeColors) {
    val isDark = appColors.isDark
    val orb1Colors = if (isDark) {
        listOf(
            appColors.accentPurple.copy(alpha = 0.35f),
            appColors.accentBlue.copy(alpha = 0.25f)
        )
    } else {
        listOf(
            Color(0xFFE9D5FF).copy(alpha = 0.4f),
            Color(0xFFDDD6FE).copy(alpha = 0.3f)
        )
    }
    val orb2Colors = if (isDark) {
        listOf(
            appColors.accentPink.copy(alpha = 0.35f),
            appColors.accentPurple.copy(alpha = 0.25f)
        )
    } else {
        listOf(
            Color(0xFFFCE7F3).copy(alpha = 0.5f),
            Color(0xFFFBCFE8).copy(alpha = 0.3f)
        )
    }
    val orb3Colors = if (isDark) {
        listOf(
            appColors.accentBlue.copy(alpha = 0.35f),
            appColors.accentTeal.copy(alpha = 0.25f)
        )
    } else {
        listOf(
            Color(0xFFE0E7FF).copy(alpha = 0.4f),
            Color(0xFFC7D2FE).copy(alpha = 0.3f)
        )
    }
    val orb4Colors = if (isDark) {
        listOf(
            appColors.accentGold.copy(alpha = 0.35f),
            appColors.accentOrange.copy(alpha = 0.25f)
        )
    } else {
        listOf(
            Color(0xFFFEF3C7).copy(alpha = 0.4f),
            Color(0xFFFDE68A).copy(alpha = 0.3f)
        )
    }

    // Orb 1
    Box(
        modifier = Modifier
            .offset(x = 40.dp, y = 80.dp)
            .size(128.dp)
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    colors = orb1Colors
                )
            )
            .blur(48.dp)
    )

    // Orb 2
    Box(
        modifier = Modifier
            .offset(x = (-40).dp, y = (-160).dp)
            .size(160.dp)
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    colors = orb2Colors
                )
            )
            .blur(48.dp)
    )

    // Orb 3
    Box(
        modifier = Modifier
            .offset(x = 200.dp, y = 300.dp)
            .size(96.dp)
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    colors = orb3Colors
                )
            )
            .blur(32.dp)
    )

    // Orb 4
    Box(
        modifier = Modifier
            .offset(x = 300.dp, y = 120.dp)
            .size(80.dp)
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    colors = orb4Colors
                )
            )
            .blur(32.dp)
    )
}

@Composable
private fun QuickMatchCard(
    onClick: () -> Unit,
    appColors: AppThemeColors,
    titleColor: Color,
    subtitleColor: Color,
    iconBackground: Color,
    iconBorder: Color,
    accentPurple: Color,
    accentPink: Color,
    modifier: Modifier = Modifier
) {
    val isDark = appColors.isDark
    val outerGlow = if (isDark) {
        Brush.linearGradient(
            colors = listOf(
                accentPink.copy(alpha = 0.35f),
                accentPurple.copy(alpha = 0.32f)
            )
        )
    } else {
        Brush.linearGradient(
            colors = listOf(
                Color(0xFFFCE7F3).copy(alpha = 0.6f),
                Color(0xFFFBCFE8).copy(alpha = 0.5f)
            )
        )
    }
    val cardGradient = if (isDark) {
        Brush.linearGradient(
            colors = listOf(
                accentPink.copy(alpha = 0.45f),
                accentPurple.copy(alpha = 0.4f)
            )
        )
    } else {
        Brush.linearGradient(
            colors = listOf(
                Color(0xFFFCE7F3).copy(alpha = 0.7f),
                Color(0xFFFBCFE8).copy(alpha = 0.6f)
            )
        )
    }
    val highlightGradient = if (isDark) {
        Brush.verticalGradient(
            colors = listOf(
                appColors.glassSurface.copy(alpha = 0.45f),
                Color.Transparent
            )
        )
    } else {
        Brush.verticalGradient(
            colors = listOf(
                Color.White.copy(alpha = 0.6f),
                Color.Transparent
            )
        )
    }
    val innerGlow = if (isDark) {
        Brush.linearGradient(
            colors = listOf(
                appColors.glassSurface.copy(alpha = 0.4f),
                Color.Transparent
            )
        )
    } else {
        Brush.linearGradient(
            colors = listOf(
                Color.White.copy(alpha = 0.5f),
                Color.Transparent
            )
        )
    }
    val borderColor = if (isDark) appColors.glassBorder else Color.White.copy(alpha = 0.8f)
    val circleBackground = if (isDark) iconBackground else Color.White
    val circleBorderColor = if (isDark) iconBorder else Color.White

    Box(
        modifier = modifier.height(100.dp)
    ) {
        // Outer glow
        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset(x = (-4).dp, y = (-4).dp)
                .clip(RoundedCornerShape(14.dp))
                .background(outerGlow)
                .blur(16.dp)
        )
        
        Card(
            onClick = onClick,
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        cardGradient,
                        shape = RoundedCornerShape(14.dp)
                    )
                    .border(2.dp, borderColor, RoundedCornerShape(14.dp))
                    .padding(16.dp)
            ) {
                // Top highlight
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clip(RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp))
                        .background(highlightGradient)
                        .offset(y = (-16).dp)
                )
                
                // Inner glow
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(14.dp))
                        .background(innerGlow)
                        .offset(x = 2.dp, y = 2.dp)
                )

                Column(
                    modifier = Modifier.align(Alignment.TopStart)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(circleBackground)
                            .border(2.dp, circleBorderColor, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.FlashOn,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = accentPurple
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Quick Match",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = titleColor
                    )
                    Text(
                        text = "Swipe to connect",
                        fontSize = 11.sp,
                        color = subtitleColor
                    )
                }
            }
        }
    }
}

@Composable
private fun AIMatchmakerCard(
    onClick: () -> Unit,
    appColors: AppThemeColors,
    titleColor: Color,
    subtitleColor: Color,
    iconBackground: Color,
    iconBorder: Color,
    accentPurple: Color,
    accentBlue: Color,
    modifier: Modifier = Modifier
) {
    val isDark = appColors.isDark
    val outerGlow = if (isDark) {
        Brush.linearGradient(
            colors = listOf(
                accentPurple.copy(alpha = 0.35f),
                accentBlue.copy(alpha = 0.35f)
            )
        )
    } else {
        Brush.linearGradient(
            colors = listOf(
                Color(0xFFE9D5FF).copy(alpha = 0.6f),
                Color(0xFFDDD6FE).copy(alpha = 0.5f)
            )
        )
    }
    val cardGradient = if (isDark) {
        Brush.linearGradient(
            colors = listOf(
                accentPurple.copy(alpha = 0.45f),
                accentBlue.copy(alpha = 0.4f)
            )
        )
    } else {
        Brush.linearGradient(
            colors = listOf(
                Color(0xFFE9D5FF).copy(alpha = 0.7f),
                Color(0xFFDDD6FE).copy(alpha = 0.6f)
            )
        )
    }
    val highlightGradient = if (isDark) {
        Brush.verticalGradient(
            colors = listOf(
                appColors.glassSurface.copy(alpha = 0.45f),
                Color.Transparent
            )
        )
    } else {
        Brush.verticalGradient(
            colors = listOf(
                Color.White.copy(alpha = 0.6f),
                Color.Transparent
            )
        )
    }
    val innerGlow = if (isDark) {
        Brush.linearGradient(
            colors = listOf(
                appColors.glassSurface.copy(alpha = 0.4f),
                Color.Transparent
            )
        )
    } else {
        Brush.linearGradient(
            colors = listOf(
                Color.White.copy(alpha = 0.5f),
                Color.Transparent
            )
        )
    }
    val borderColor = if (isDark) appColors.glassBorder else Color.White.copy(alpha = 0.8f)
    val circleBackground = if (isDark) iconBackground else Color.White
    val circleBorderColor = if (isDark) iconBorder else Color.White

    Box(
        modifier = modifier.height(100.dp)
    ) {
        // Outer glow
        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset(x = (-4).dp, y = (-4).dp)
                .clip(RoundedCornerShape(14.dp))
                .background(outerGlow)
                .blur(16.dp)
        )
        
        Card(
            onClick = onClick,
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        cardGradient,
                        shape = RoundedCornerShape(14.dp)
                    )
                    .border(2.dp, borderColor, RoundedCornerShape(14.dp))
                    .padding(16.dp)
            ) {
                // Top highlight
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clip(RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp))
                        .background(highlightGradient)
                        .offset(y = (-16).dp)
                )
                
                // Inner glow
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(14.dp))
                        .background(innerGlow)
                        .offset(x = 2.dp, y = 2.dp)
                )

                Column(
                    modifier = Modifier.align(Alignment.TopStart)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(circleBackground)
                            .border(2.dp, circleBorderColor, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = accentPurple
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "AI Matchmaker",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = titleColor
                    )
                    Text(
                        text = "Find partners",
                        fontSize = 11.sp,
                        color = subtitleColor
                    )
                }
            }
        }
    }
}

@Composable
private fun ExploreMoreCard(
    onClick: () -> Unit,
    cardSurface: Color,
    cardBorder: Color,
    titleColor: Color,
    subtitleColor: Color,
    iconColor: Color
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardSurface
        ),
        border = BorderStroke(1.dp, cardBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Explore More",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = titleColor
                )
                Text(
                    text = "Browse sports & discover new people",
                    fontSize = 12.sp,
                    color = subtitleColor,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(cardSurface)
                    .border(2.dp, cardBorder, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = iconColor
                )
            }
        }
    }
}

@Composable
private fun ActivityCard(
    activity: Activity,
    isSaved: Boolean,
    onSaveToggle: () -> Unit,
    onActivityClick: () -> Unit,
    onDetailsClick: () -> Unit,
    appColors: AppThemeColors,
    titleColor: Color,
    subtitleColor: Color,
    secondaryText: Color,
    iconColor: Color,
    cardSurface: Color,
    cardBorder: Color,
    chipBackground: Color,
    chipBorder: Color,
    accentGreen: Color,
    accentRed: Color,
    savedInactive: Color
) {
    val spotsLeft = activity.spotsTotal - activity.spotsTaken

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardSurface
        ),
        border = BorderStroke(1.dp, cardBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = CircleShape,
                        modifier = Modifier.size(40.dp),
                        color = Color.Transparent
                    ) {
                        if (activity.hostAvatar.isNotEmpty()) {
                            AsyncImage(
                                model = activity.hostAvatar,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                                    .border(2.dp, cardBorder, CircleShape)
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                                    .background(if (appColors.isDark) appColors.subtleSurface else Color.Gray.copy(alpha = 0.2f))
                                    .border(2.dp, cardBorder, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = activity.hostName.first().toString(),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = titleColor
                                )
                            }
                        }
                    }
                    Column {
                        Text(
                            text = activity.hostName,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = titleColor
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 2.dp)
                        ) {
                            Text(
                                text = activity.sportIcon,
                                fontSize = 14.sp
                            )
                            Text(
                                text = activity.sportType,
                                fontSize = 12.sp,
                                color = subtitleColor
                            )
                        }
                    }
                }

                IconButton(
                    onClick = onSaveToggle,
                    modifier = Modifier.padding(4.dp)
                ) {
                    Icon(
                        imageVector = if (isSaved) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Save",
                        tint = if (isSaved) accentRed else savedInactive,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Title
            Text(
                text = activity.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = titleColor,
                modifier = Modifier.padding(bottom = 10.dp)
            )

            // Details
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = iconColor
                    )
                    Text(
                        text = "${activity.date} • ${activity.time}",
                        fontSize = 12.sp,
                        color = subtitleColor
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = iconColor
                    )
                    Text(
                        text = "${activity.location} • ${activity.distance}",
                        fontSize = 12.sp,
                        color = subtitleColor
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Group,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = iconColor
                    )
                    Text(
                        text = "$spotsLeft of ${activity.spotsTotal} spots remaining",
                        fontSize = 12.sp,
                        color = subtitleColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Badge and Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = chipBackground,
                    border = BorderStroke(1.dp, chipBorder)
                ) {
                    Text(
                        text = activity.level,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        color = secondaryText,
                        fontWeight = FontWeight.Medium
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDetailsClick,
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = cardSurface
                        ),
                        border = BorderStroke(1.dp, cardBorder),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Text(
                            text = "Details",
                            fontSize = 13.sp,
                            color = secondaryText
                        )
                    }

                    Button(
                        onClick = onActivityClick,
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = accentGreen
                        ),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Text(
                            text = "Join",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = appColors.iconOnAccent
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FloatingCreateButton(
    onClick: () -> Unit,
    appColors: AppThemeColors,
    modifier: Modifier = Modifier
) {
    val accentPurple = appColors.accentPurple
    val accentPink = appColors.accentPink
    val accentGradient = Brush.linearGradient(listOf(accentPurple, accentPink))
    val glowGradient = Brush.radialGradient(
        colors = listOf(
            accentPurple.copy(alpha = 0.4f),
            accentPink.copy(alpha = 0.35f)
        )
    )

    Box(modifier = modifier) {
        // Floating shadow (bottom shadow)
        Box(
            modifier = Modifier
                .offset(x = 0.dp, y = 28.dp) // Position below button
                .size(width = 48.dp, height = 12.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            accentPurple.copy(alpha = 0.25f),
                            Color.Transparent
                        )
                    )
                )
                .blur(8.dp)
                .align(Alignment.BottomCenter)
        )
        
        // Outer glow
        Box(
            modifier = Modifier
                .size(58.dp) // Slightly larger than button for glow effect
                .clip(CircleShape)
                .background(glowGradient)
                .blur(16.dp)
                .alpha(0.75f)
                .align(Alignment.Center)
        )
        
        // Main button
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(accentGradient)
                .clickable(onClick = onClick)
                .align(Alignment.Center),
            contentAlignment = Alignment.Center
        ) {
            // Shine effect
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.3f),
                                Color.Transparent
                            ),
                            start = androidx.compose.ui.geometry.Offset(0f, 0f),
                            end = androidx.compose.ui.geometry.Offset(56f, 56f)
                        )
                    )
                    .clip(CircleShape)
            )
            
            // Icon
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Create",
                tint = appColors.iconOnAccent,
                modifier = Modifier
                    .size(28.dp)
                    .zIndex(1f)
            )
            
            // Pulse ring animation
            val infiniteTransition = rememberInfiniteTransition(label = "pulse")
            val pulseAlpha by infiniteTransition.animateFloat(
                initialValue = 0.3f,
                targetValue = 0.0f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 2000, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                ),
                label = "pulse-alpha"
            )
            
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(accentPurple.copy(alpha = pulseAlpha))
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilterSheet(
    filterSport: String,
    filterDistance: Float,
    onSportChange: (String) -> Unit,
    onDistanceChange: (Float) -> Unit,
    onDismiss: () -> Unit,
    appColors: AppThemeColors,
    titleColor: Color,
    subtitleColor: Color,
    secondaryText: Color,
    iconColor: Color,
    cardSurface: Color,
    cardBorder: Color,
    accentPurple: Color
) {
    val sports = listOf("All Sports", "Basketball", "Running", "Tennis", "Soccer", "Swimming", "Cycling")
    var expandedSport by remember { mutableStateOf(false) }
    var selectedSport by remember(filterSport) { mutableStateOf(filterSport) }

    val sheetBackground = if (appColors.isDark) appColors.subtleSurface else Color(0xFFF5F6F8)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = sheetBackground,
        dragHandle = null,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Filters",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = titleColor
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Customize your activity search preferences",
                        fontSize = 14.sp,
                        color = subtitleColor
                    )
                }
                
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = iconColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Sport Type
            Text(
                text = "Sport Type",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = titleColor,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Box {
                ExposedDropdownMenuBox(
                    expanded = expandedSport,
                    onExpandedChange = { expandedSport = !expandedSport }
                ) {
                    OutlinedTextField(
                        value = if (selectedSport == "all") "All Sports" else selectedSport,
                        onValueChange = { },
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedSport)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = cardSurface,
                            focusedContainerColor = cardSurface,
                            unfocusedBorderColor = cardBorder,
                            focusedBorderColor = accentPurple,
                            focusedTextColor = titleColor,
                            unfocusedTextColor = titleColor,
                            focusedPlaceholderColor = subtitleColor,
                            unfocusedPlaceholderColor = subtitleColor
                        )
                    )

                    ExposedDropdownMenu(
                        expanded = expandedSport,
                        onDismissRequest = { expandedSport = false }
                    ) {
                        sports.forEach { sport ->
                            DropdownMenuItem(
                                text = { Text(sport) },
                                onClick = {
                                    selectedSport = if (sport == "All Sports") "all" else sport
                                    onSportChange(selectedSport)
                                    expandedSport = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Distance
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Distance: ${filterDistance.toInt()} miles",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = titleColor
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Slider
            Slider(
                value = filterDistance,
                onValueChange = { onDistanceChange(it) },
                valueRange = 1f..20f,
                steps = 18, // 20 - 1 - 1 = 18 steps
                colors = SliderDefaults.colors(
                    thumbColor = cardSurface,
                    activeTrackColor = accentPurple,
                    inactiveTrackColor = cardBorder
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Help button in bottom right
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                FloatingActionButton(
                    onClick = { /* Help action */ },
                    modifier = Modifier.size(56.dp),
                    shape = CircleShape,
                    containerColor = accentPurple
                ) {
                    Text(
                        text = "?",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = appColors.iconOnAccent
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeFeedPreview() {
    val controller = ThemeController(isDarkMode = false) { }
    CompositionLocalProvider(LocalThemeController provides controller) {
        DamAndroidTheme(darkTheme = controller.isDarkMode) {
            HomeFeed(
                onActivityClick = { _ -> },
                onSearchClick = { },
                onQuickMatchClick = { },
                onAIMatchmakerClick = { },
                onEventDetailsClick = { },
                onCreateClick = { },
                onNotificationsClick = { }
            )
        }
    }
}

