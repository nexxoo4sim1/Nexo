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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.example.damandroid.ui.theme.DamAndroidTheme
import com.example.damandroid.ui.theme.LocalThemeController
import com.example.damandroid.ui.theme.ThemeController
import com.example.damandroid.ui.theme.rememberAppThemeColors

val aiSuggestionsData = listOf(
    Activity(
        id = "ai-1",
        title = "Morning Beach Volleyball Match",
        sportType = "Volleyball",
        sportIcon = "🏐",
        level = "Intermediate",
        hostName = "Emma Wilson",
        hostAvatar = "https://i.pravatar.cc/150?img=5",
        date = "Today",
        time = "8:00 AM",
        location = "Santa Monica Beach",
        distance = "1.2 mi",
        spotsTotal = 12,
        spotsTaken = 8
    ),
    Activity(
        id = "ai-2",
        title = "Evening Running Group",
        sportType = "Running",
        sportIcon = "🏃",
        level = "All Levels",
        hostName = "Michael Chen",
        hostAvatar = "https://i.pravatar.cc/150?img=12",
        date = "Today",
        time = "6:30 PM",
        location = "Central Park",
        distance = "0.8 mi",
        spotsTotal = 15,
        spotsTaken = 10
    ),
    Activity(
        id = "ai-3",
        title = "Yoga & Meditation Session",
        sportType = "Yoga",
        sportIcon = "🧘",
        level = "Beginner",
        hostName = "Sarah Johnson",
        hostAvatar = "https://i.pravatar.cc/150?img=9",
        date = "Tomorrow",
        time = "7:00 AM",
        location = "Zen Studio",
        distance = "1.5 mi",
        spotsTotal = 20,
        spotsTaken = 15
    ),
    Activity(
        id = "ai-4",
        title = "Pickup Basketball Game",
        sportType = "Basketball",
        sportIcon = "🏀",
        level = "Intermediate",
        hostName = "James Rodriguez",
        hostAvatar = "https://i.pravatar.cc/150?img=15",
        date = "Tomorrow",
        time = "5:00 PM",
        location = "Downtown Court",
        distance = "2.1 mi",
        spotsTotal = 10,
        spotsTaken = 7
    )
)

@Composable
fun AISuggestionsPage(
    onBack: () -> Unit,
    onActivityClick: (Activity) -> Unit,
    showBackButton: Boolean = true,
    modifier: Modifier = Modifier
) {
    var savedActivities by remember { mutableStateOf(setOf<String>()) }
    var viewMode by remember { mutableStateOf("list") } // "map" or "list"

    val toggleSave = { id: String ->
        savedActivities = if (savedActivities.contains(id)) {
            savedActivities - id
        } else {
            savedActivities + id
        }
    }

    val appTheme = rememberAppThemeColors()
    val primaryText = appTheme.primaryText
    val secondaryText = appTheme.secondaryText
    val mutedText = appTheme.mutedText
    val cardSurface = appTheme.cardSurface
    val subtleSurface = appTheme.subtleSurface
    val isDark = appTheme.isDark

    val accentPurple = Color(0xFFA855F7)
    val accentPink = Color(0xFFEC4899)
    val accentMint = Color(0xFF86EFAC)
    val accentAmber = Color(0xFFD97706)

    val headerSurfaceColor = if (isDark) subtleSurface else Color(0xFFE8D5F2).copy(alpha = 0.4f)
    val toggleSurfaceColor = if (isDark) subtleSurface.copy(alpha = 0.9f) else Color(0xFFE8D5F2).copy(alpha = 0.6f)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(appTheme.backgroundGradient)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header with light purple background
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = headerSurfaceColor,
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Sessions",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = primaryText
                            )
                            Text(
                                text = "AI-powered recommendations",
                                fontSize = 14.sp,
                                color = secondaryText,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                        
                        // AI Icon button
                        IconButton(
                            onClick = { /* AI action */ },
                            modifier = Modifier.size(48.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                                    .background(accentPurple),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = "AI",
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Map/List Toggle
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        color = toggleSurfaceColor
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Map Button
                            Button(
                                onClick = { viewMode = "map" },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(40.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (viewMode == "map") accentMint else Color.Transparent
                                ),
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.LocationOn,
                                        contentDescription = "Map",
                                        tint = if (viewMode == "map") Color.White else primaryText,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Map",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = if (viewMode == "map") Color.White else primaryText
                                    )
                                }
                            }

                            // List Button
                            Button(
                                onClick = { viewMode = "list" },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(40.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (viewMode == "list") accentMint else Color.Transparent
                                ),
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.List,
                                        contentDescription = "List",
                                        tint = if (viewMode == "list") Color.White else primaryText,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "List",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = if (viewMode == "list") Color.White else primaryText
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Content based on view mode
            if (viewMode == "list") {
                // List View
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        top = 16.dp,
                        end = 16.dp,
                        bottom = 100.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Personalized For You Card (with gradient)
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(
                                            Color(0xFFEC4899), // Fuchsia
                                            Color(0xFFA855F7)  // Purple
                                        )
                                    )
                                )
                                .padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(CircleShape)
                                        .background(Color.White.copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = "Personalized For You",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Text(
                                        text = "Based on your activity & preferences",
                                        fontSize = 14.sp,
                                        color = Color.White.copy(alpha = 0.9f),
                                        modifier = Modifier.padding(top = 2.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Why This Section
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, appTheme.cardBorder, RoundedCornerShape(16.dp)),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = cardSurface
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(32.dp)
                                            .clip(CircleShape)
                                            .background(accentPurple.copy(alpha = if (isDark) 0.25f else 0.2f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.AutoAwesome,
                                            contentDescription = null,
                                            tint = accentPurple,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                    Text(
                                        text = "Why these activities?",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = primaryText
                                    )
                                }
                                Text(
                                    text = "We've selected activities matching your skill level, preferred sports, and typical schedule. These are nearby and have availability.",
                                    fontSize = 12.sp,
                                    color = mutedText,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }

                    // AI Suggested Activities
                    items(
                        items = aiSuggestionsData,
                        key = { it.id }
                    ) { activity ->
                        AISuggestedActivityCard(
                            activity = activity,
                            isSaved = savedActivities.contains(activity.id),
                            onSaveClick = { toggleSave(activity.id) },
                            onJoinClick = { onActivityClick(activity) }
                        )
                    }
                }
            } else {
                // Map View
                MapView(
                    activities = aiSuggestionsData,
                    onActivityClick = onActivityClick,
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                )
            }
        }
    }
}

@Composable
private fun MapView(
    activities: List<Activity>,
    onActivityClick: (Activity) -> Unit,
    modifier: Modifier = Modifier
) {
    val appTheme = rememberAppThemeColors()
    val primaryText = appTheme.primaryText
    val cardSurface = appTheme.cardSurface
    val accentMint = Color(0xFF86EFAC)
    val accentAmber = Color(0xFFD97706)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(appTheme.altBackgroundGradient)
    ) {
        // Map Grid Background
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            cardSurface,
                            appTheme.subtleSurface,
                            cardSurface
                        ),
                        start = androidx.compose.ui.geometry.Offset(0f, 0f),
                        end = androidx.compose.ui.geometry.Offset(100f, 100f)
                    )
                )
        ) {
            // Grid Pattern (simulated with lines)
            // In a real implementation, you'd use a proper map library
            // Here we'll create a simple grid effect
            
            // Green areas (parks)
            Box(
                modifier = Modifier
                    .offset(x = 40.dp, y = 80.dp)
                    .size(width = 120.dp, height = 80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(accentMint.copy(alpha = 0.3f))
            )
            Box(
                modifier = Modifier
                    .offset(x = 200.dp, y = 200.dp)
                    .size(width = 100.dp, height = 60.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(accentMint.copy(alpha = 0.3f))
            )
            Box(
                modifier = Modifier
                    .offset(x = 60.dp, y = 350.dp)
                    .size(width = 140.dp, height = 90.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(accentMint.copy(alpha = 0.3f))
            )
            
            // Brown buildings
            Box(
                modifier = Modifier
                    .offset(x = 160.dp, y = 120.dp)
                    .size(width = 80.dp, height = 50.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(accentAmber.copy(alpha = 0.3f))
            )
            Box(
                modifier = Modifier
                    .offset(x = 280.dp, y = 280.dp)
                    .size(width = 70.dp, height = 45.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(accentAmber.copy(alpha = 0.3f))
            )
            Box(
                modifier = Modifier
                    .offset(x = 120.dp, y = 400.dp)
                    .size(width = 90.dp, height = 55.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(accentAmber.copy(alpha = 0.3f))
            )
        }
        
        // Activity Markers
        // Marker 1 - Volleyball (top-left)
        ActivityMarker(
            x = 100.dp,
            y = 140.dp,
            activity = activities[0],
            onClick = { onActivityClick(activities[0]) }
        )
        
        // Marker 2 - Running (middle-right)
        ActivityMarker(
            x = 280.dp,
            y = 220.dp,
            activity = activities[1],
            onClick = { onActivityClick(activities[1]) }
        )
        
        // Marker 3 - Yoga (bottom-left)
        ActivityMarker(
            x = 80.dp,
            y = 380.dp,
            activity = activities[2],
            onClick = { onActivityClick(activities[2]) }
        )
        
        // Marker 4 - Basketball (bottom-right)
        ActivityMarker(
            x = 320.dp,
            y = 420.dp,
            activity = activities[3],
            onClick = { onActivityClick(activities[3]) }
        )
        
        // Zoom Controls (right side)
        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Zoom In Button
            Surface(
                modifier = Modifier
                    .size(48.dp),
                shape = CircleShape,
                color = cardSurface,
                shadowElevation = 4.dp
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Zoom In",
                        tint = primaryText,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            
            // Zoom Out Button
            Surface(
                modifier = Modifier
                    .size(48.dp),
                shape = CircleShape,
                color = cardSurface,
                shadowElevation = 4.dp
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = "Zoom Out",
                        tint = primaryText,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Recenter/Current Location Button
            Surface(
                modifier = Modifier
                    .size(48.dp),
                shape = CircleShape,
                color = cardSurface,
                shadowElevation = 4.dp
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Recenter",
                        tint = primaryText,
                        modifier = Modifier
                            .size(20.dp)
                            .rotate(45f)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Filter Button
            Surface(
                modifier = Modifier
                    .size(48.dp),
                shape = CircleShape,
                color = Color(0xFFA855F7),
                shadowElevation = 4.dp
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = "Filter",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ActivityMarker(
    x: androidx.compose.ui.unit.Dp,
    y: androidx.compose.ui.unit.Dp,
    activity: Activity,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .offset(x = x, y = y)
            .clickable(onClick = onClick)
    ) {
        // Purple oval marker
        Surface(
            modifier = Modifier
                .size(width = 56.dp, height = 40.dp),
            shape = RoundedCornerShape(28.dp),
            color = Color(0xFFA855F7),
            shadowElevation = 4.dp
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
        
        // Pink sparkle badge on top-right
        Surface(
            modifier = Modifier
                .offset(x = 32.dp, y = (-4).dp)
                .size(24.dp),
            shape = CircleShape,
            color = Color(0xFFEC4899),
            shadowElevation = 2.dp
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = "AI Pick",
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

@Composable
private fun AISuggestedActivityCard(
    activity: Activity,
    isSaved: Boolean,
    onSaveClick: () -> Unit,
    onJoinClick: () -> Unit
) {
    val spotsLeft = activity.spotsTotal - activity.spotsTaken
    val appTheme = rememberAppThemeColors()
    val cardSurface = appTheme.cardSurface
    val primaryText = appTheme.primaryText
    val secondaryText = appTheme.secondaryText
    val mutedText = appTheme.mutedText

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onJoinClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // AI Badge Ribbon - Top Right
            Surface(
                modifier = Modifier
                    .align(Alignment.TopEnd),
                shape = RoundedCornerShape(12.dp),
                color = Color.Transparent
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFFEC4899), // Fuchsia
                                    Color(0xFFA855F7)  // Purple
                                )
                            ),
                            RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "AI Pick",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 70.dp) // Space for AI badge
            ) {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = activity.hostAvatar,
                        contentDescription = null,
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                    )
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = activity.hostName,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = primaryText
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            Text(
                                text = activity.sportIcon,
                                fontSize = 16.sp
                            )
                            Text(
                                text = activity.sportType,
                                fontSize = 13.sp,
                                color = secondaryText
                            )
                        }
                    }
                }

                // Title
                Text(
                    text = activity.title,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryText,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                // Details
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 12.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = null,
                            tint = mutedText,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "${activity.date} • ${activity.time}",
                            fontSize = 13.sp,
                            color = mutedText
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = mutedText,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "${activity.location} • ${activity.distance}",
                            fontSize = 13.sp,
                            color = mutedText
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = mutedText,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "$spotsLeft of ${activity.spotsTotal} spots remaining",
                            fontSize = 13.sp,
                            color = mutedText
                        )
                    }
                }

                // Badge and Actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFA855F7)
                    ) {
                        Text(
                            text = activity.level,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = onSaveClick,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                imageVector = if (isSaved) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Save",
                                tint = if (isSaved) Color(0xFFEF4444) else mutedText,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Button(
                            onClick = onJoinClick,
                            modifier = Modifier.height(32.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF86EFAC)
                            ),
                            contentPadding = PaddingValues(horizontal = 20.dp)
                        ) {
                            Text(
                                text = "Join",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
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
fun AISuggestionsPagePreview() {
    CompositionLocalProvider(
        LocalThemeController provides ThemeController(
            isDarkMode = false,
            setDarkMode = {}
        )
    ) {
        DamAndroidTheme(darkTheme = false) {
            AISuggestionsPage(
                onBack = { },
                onActivityClick = { }
            )
        }
    }
}

