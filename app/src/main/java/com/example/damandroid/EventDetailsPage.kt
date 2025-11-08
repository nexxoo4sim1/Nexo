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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.damandroid.ui.theme.DamAndroidTheme
import com.example.damandroid.ui.theme.LocalThemeController
import com.example.damandroid.ui.theme.ThemeController
import com.example.damandroid.ui.theme.rememberAppThemeColors
import com.example.damandroid.ui.theme.AppThemeColors
import androidx.compose.ui.graphics.vector.ImageVector

data class EventDetails(
    val id: String,
    val title: String,
    val sportIcon: String,
    val sportType: String,
    val date: String,
    val time: String,
    val duration: String,
    val location: String,
    val distance: String,
    val price: Int,
    val type: String, // 'free' | 'paid' | 'members'
    val level: String,
    val maxParticipants: Int,
    val currentParticipants: Int,
    val description: String,
    val requirements: List<String>,
    val coach: CoachInfo
)

data class CoachInfo(
    val name: String,
    val avatar: String,
    val isVerified: Boolean,
    val rating: Double,
    val totalReviews: Int,
    val bio: String,
    val certifications: List<String>
)

data class Participant(
    val id: String,
    val name: String,
    val avatar: String
)

data class Review(
    val id: String,
    val userName: String,
    val userAvatar: String,
    val rating: Int,
    val comment: String,
    val date: String
)

@Composable
fun EventDetailsPage(
    onBack: () -> Unit,
    onJoin: () -> Unit = {},
    onViewCoach: () -> Unit = {},
    onMessage: () -> Unit = {},
    isCoachView: Boolean = false,
    modifier: Modifier = Modifier
) {
    var isSaved by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf("details") } // "details", "participants", "reviews"

    // Mock event data
    val event = EventDetails(
        id = "1",
        title = "Morning HIIT Bootcamp",
        sportIcon = "🏃",
        sportType = "HIIT Training",
        date = "Nov 5, 2025",
        time = "7:00 AM",
        duration = "60 min",
        location = "Central Park - Main Field",
        distance = "2.1 mi away",
        price = 25,
        type = "paid",
        level = "Intermediate",
        maxParticipants = 12,
        currentParticipants = 8,
        description = "High-intensity interval training session focused on building strength and endurance. Perfect for all fitness levels with modifications available. Bring water and a workout mat!",
        requirements = listOf("Yoga mat", "Water bottle", "Athletic shoes"),
        coach = CoachInfo(
            name = "Alex Thompson",
            avatar = "https://api.dicebear.com/7.x/avataaars/svg?seed=Alex",
            isVerified = true,
            rating = 4.8,
            totalReviews = 124,
            bio = "Certified personal trainer with 8+ years of experience",
            certifications = listOf("NASM-CPT", "ACE")
        )
    )

    val participants = listOf(
        Participant(id = "1", name = "Sarah M.", avatar = "https://api.dicebear.com/7.x/avataaars/svg?seed=Sarah"),
        Participant(id = "2", name = "Mike R.", avatar = "https://api.dicebear.com/7.x/avataaars/svg?seed=Mike"),
        Participant(id = "3", name = "Emma L.", avatar = "https://api.dicebear.com/7.x/avataaars/svg?seed=Emma"),
        Participant(id = "4", name = "John D.", avatar = "https://api.dicebear.com/7.x/avataaars/svg?seed=John"),
        Participant(id = "5", name = "Lisa K.", avatar = "https://api.dicebear.com/7.x/avataaars/svg?seed=Lisa")
    )

    val reviews = listOf(
        Review(
            id = "1",
            userName = "Sarah M.",
            userAvatar = "https://api.dicebear.com/7.x/avataaars/svg?seed=Sarah",
            rating = 5,
            comment = "Excellent workout! Alex is very motivating and adjusts exercises for different levels.",
            date = "Oct 28, 2025"
        ),
        Review(
            id = "2",
            userName = "Mike R.",
            userAvatar = "https://api.dicebear.com/7.x/avataaars/svg?seed=Mike",
            rating = 5,
            comment = "Great session, challenging but fun. Highly recommend!",
            date = "Oct 25, 2025"
        )
    )

    val spotsLeft = event.maxParticipants - event.currentParticipants
    val fillPercentage = (event.currentParticipants.toFloat() / event.maxParticipants.toFloat()) * 100

    val themeController = LocalThemeController.current
    val theme = rememberAppThemeColors(themeController.isDarkMode)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(theme.backgroundGradient)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = theme.glassSurface,
                shadowElevation = 1.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = theme.primaryText,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Text(
                        text = "Event Details",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = theme.primaryText
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        IconButton(
                            onClick = { isSaved = !isSaved },
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                imageVector = if (isSaved) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Save",
                                tint = if (isSaved) theme.danger else theme.mutedText,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        IconButton(
                            onClick = { /* Share */ },
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Share",
                                tint = theme.primaryText,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            // Content
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TabChip(
                        text = "Details",
                        selected = selectedTab == "details",
                        onClick = { selectedTab = "details" },
                        theme = theme
                    )
                    TabChip(
                        text = "Participants",
                        selected = selectedTab == "participants",
                        onClick = { selectedTab = "participants" },
                        theme = theme
                    )
                    TabChip(
                        text = "Reviews",
                        selected = selectedTab == "reviews",
                        onClick = { selectedTab = "reviews" },
                        theme = theme
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 140.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (selectedTab == "details") {
                    item {
                        SectionCard(title = "About this session", theme = theme) {
                            Text(
                                text = event.description,
                                fontSize = 13.sp,
                                color = theme.primaryText,
                                lineHeight = 18.sp
                            )
                        }
                    }

                    item {
                        SectionCard(title = "Requirements", theme = theme) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                event.requirements.forEach { requirement ->
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(24.dp)
                                                .clip(CircleShape)
                                                .background(theme.subtleSurface),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "•",
                                                fontSize = 16.sp,
                                                color = theme.accentPurple
                                            )
                                        }
                                        Text(
                                            text = requirement,
                                            fontSize = 13.sp,
                                            color = theme.primaryText
                                        )
                                    }
                                }
                            }
                        }
                    }

                    item {
                        SectionCard(title = "Coach", theme = theme) {
                            CoachCard(coach = event.coach, theme = theme, onViewCoach = onViewCoach, onMessage = onMessage)
                        }
                    }
                }

                if (selectedTab == "participants") {
                    item {
                        SectionCard(title = "Participants", theme = theme) {
                            ParticipantList(participants = participants, theme = theme)
                        }
                    }
                }

                if (selectedTab == "reviews") {
                    item {
                        SectionCard(title = "Reviews", theme = theme) {
                            ReviewList(reviews = reviews, theme = theme)
                        }
                    }
                }
            }
        }

        // Bottom Actions - Fixed at bottom
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            color = theme.glassSurface,
            shadowElevation = 8.dp
        ) {
            if (isCoachView) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { /* Edit */ },
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        shape = RoundedCornerShape(22.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = theme.primaryText),
                        border = BorderStroke(2.dp, theme.glassBorder)
                    ) {
                        Text("Edit Event")
                    }
                    Button(
                        onClick = { /* Manage */ },
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        shape = RoundedCornerShape(22.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = theme.accentBlue
                        )
                    ) {
                        Text("Manage Participants", color = theme.iconOnAccent)
                    }
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Total",
                            fontSize = 11.sp,
                            color = theme.mutedText,
                            fontWeight = FontWeight.Normal
                        )
                        Text(
                            text = "$${event.price}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (event.price > 0) theme.accentGreen else theme.success
                        )
                    }
                    Button(
                        onClick = onJoin,
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = theme.accentPurple
                        )
                    ) {
                        Text("Join Now", color = theme.iconOnAccent)
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = Color.White.copy(alpha = 0.1f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.8f),
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = label,
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }
    }
}

@Composable
private fun TabChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    theme: AppThemeColors
) {
    val background = if (selected) theme.accentPurple else theme.glassSurface
    val borderColor = if (selected) theme.accentPurple else theme.glassBorder
    val textColor = if (selected) theme.iconOnAccent else theme.primaryText

    Surface(
        modifier = Modifier
            .width(110.dp)
            .height(36.dp)
            .clip(RoundedCornerShape(18.dp))
            .clickable(onClick = onClick),
        color = background,
        border = BorderStroke(1.dp, borderColor)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = text,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = textColor
            )
        }
    }
}

@Composable
private fun SectionCard(
    title: String,
    theme: AppThemeColors,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = theme.primaryText,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Surface(
            shape = RoundedCornerShape(24.dp),
            color = theme.glassSurface,
            border = BorderStroke(2.dp, theme.glassBorder)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                content()
            }
        }
    }
}

@Composable
private fun CoachCard(
    coach: CoachInfo,
    theme: AppThemeColors,
    onViewCoach: () -> Unit,
    onMessage: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = coach.avatar,
            contentDescription = null,
            modifier = Modifier
                .size(56.dp)
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
                    text = coach.name,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = theme.primaryText
                )
                if (coach.isVerified) {
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = null,
                        tint = theme.success,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Text(
                text = coach.bio,
                fontSize = 12.sp,
                color = theme.mutedText,
                modifier = Modifier.padding(top = 4.dp, bottom = 6.dp)
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "⭐ ${coach.rating} (${coach.totalReviews} reviews)",
                    fontSize = 12.sp,
                    color = theme.mutedText
                )
                Text(
                    text = coach.certifications.joinToString(", "),
                    fontSize = 12.sp,
                    color = theme.mutedText
                )
            }
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = onViewCoach,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = theme.primaryText),
                border = BorderStroke(2.dp, theme.glassBorder)
            ) {
                Text("View Profile", fontSize = 12.sp)
            }

            Button(
                onClick = onMessage,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = theme.accentPurple)
            ) {
                Text("Message", fontSize = 12.sp, color = theme.iconOnAccent)
            }
        }
    }
}

@Composable
private fun ParticipantList(
    participants: List<Participant>,
    theme: AppThemeColors
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        participants.forEach { participant ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(
                        model = participant.avatar,
                        contentDescription = null,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .border(2.dp, theme.glassBorder, CircleShape)
                    )
                    Text(
                        text = participant.name,
                        fontSize = 13.sp,
                        color = theme.primaryText
                    )
                }

                Button(
                    onClick = { /* message */ },
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = theme.accentPurple)
                ) {
                    Text("Message", fontSize = 12.sp, color = theme.iconOnAccent)
                }
            }
        }
    }
}

@Composable
private fun ReviewList(
    reviews: List<Review>,
    theme: AppThemeColors
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        reviews.forEach { review ->
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(
                        model = review.userAvatar,
                        contentDescription = null,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .border(2.dp, theme.glassBorder, CircleShape)
                    )
                    Column {
                        Text(
                            text = review.userName,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = theme.primaryText
                        )
                        Text(
                            text = review.date,
                            fontSize = 11.sp,
                            color = theme.mutedText
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                    repeat(review.rating) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = theme.accentGold,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }

                Text(
                    text = review.comment,
                    fontSize = 12.sp,
                    color = theme.primaryText
                )
            }
        }
    }
}

@Composable
private fun StatChip(
    icon: ImageVector,
    label: String,
    theme: AppThemeColors
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(theme.subtleSurface)
            .border(1.dp, theme.glassBorder, RoundedCornerShape(12.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = theme.mutedText,
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            color = theme.mutedText
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EventDetailsPagePreview() {
    DamAndroidTheme {
        EventDetailsPage(
            onBack = { },
            onJoin = { },
            onViewCoach = { },
            onMessage = { }
        )
    }
}

