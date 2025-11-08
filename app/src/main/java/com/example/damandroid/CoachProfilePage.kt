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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.damandroid.ui.theme.DamAndroidTheme
import com.example.damandroid.ui.theme.LocalThemeController
import com.example.damandroid.ui.theme.ThemeController
import com.example.damandroid.ui.theme.rememberAppThemeColors
import com.example.damandroid.ui.theme.AppThemeColors

@Composable
fun CoachProfilePage(
    onBack: () -> Unit,
    onMessage: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var isFollowing by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf("about") } // "about", "sessions", "reviews"

    val coach = CoachProfile(
        name = "Alex Thompson",
        avatar = "https://api.dicebear.com/7.x/avataaars/svg?seed=Alex",
        isVerified = true,
        rating = 4.8,
        totalReviews = 124,
        location = "Los Angeles, CA",
        sessions = 450,
        followers = 1234,
        experience = "8 years",
        bio = "Certified personal trainer with 8+ years of experience. Specialized in HIIT, strength training, and functional fitness.",
        specializations = listOf("HIIT", "Strength Training", "Yoga", "Running"),
        certifications = listOf(
            Certification("NASM-CPT", "NASM-CPT"),
            Certification("ACE", "ACE"),
            Certification("Yoga Alliance RYT-200", "Yoga Alliance RYT-200")
        )
    )

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
                        text = "Coach Profile",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = theme.primaryText
                    )

                    IconButton(
                        onClick = { /* Share */ },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Share",
                            tint = theme.primaryText,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // Content
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                // Profile Header with Gradient
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(
                                        theme.accentBlue,
                                        theme.accentTeal
                                    )
                                )
                            )
                            .padding(vertical = 24.dp, horizontal = 16.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Profile Picture
                            Box {
                                AsyncImage(
                                    model = coach.avatar,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(100.dp)
                                        .clip(CircleShape)
                                        .border(4.dp, theme.iconOnAccent, CircleShape)
                                )
                                if (coach.isVerified) {
                                    Surface(
                                        modifier = Modifier
                                            .size(32.dp)
                                            .align(Alignment.BottomEnd)
                                            .offset(x = 8.dp, y = 8.dp),
                                        shape = CircleShape,
                                        color = theme.success,
                                        border = BorderStroke(3.dp, theme.iconOnAccent)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = "Verified",
                                            tint = theme.iconOnAccent,
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(4.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Name
                            Text(
                                text = coach.name,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = theme.iconOnAccent
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Verified Badge
                            if (coach.isVerified) {
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = theme.success
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = null,
                                            tint = theme.iconOnAccent,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Text(
                                            text = "Verified Coach",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = theme.iconOnAccent
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Rating
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = theme.accentGold,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = "${coach.rating} (${coach.totalReviews} reviews)",
                                    fontSize = 14.sp,
                                    color = theme.iconOnAccent
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Location
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = theme.iconOnAccent,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = coach.location,
                                    fontSize = 14.sp,
                                    color = theme.iconOnAccent
                                )
                            }

                            Spacer(modifier = Modifier.height(20.dp))
                        }
                    }
                }

                // Action Buttons - White cards below gradient
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = { isFollowing = !isFollowing },
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = if (isFollowing) theme.accentPurple else theme.accentBlue,
                                containerColor = theme.glassSurface
                            ),
                            border = BorderStroke(1.5.dp, if (isFollowing) theme.accentPurple else theme.accentBlue)
                        ) {
                            Icon(
                                imageVector = if (isFollowing) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = null,
                                tint = if (isFollowing) theme.accentPurple else theme.accentBlue,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isFollowing) "Following" else "Follow",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = if (isFollowing) theme.accentPurple else theme.accentBlue
                            )
                        }
                        Button(
                            onClick = onMessage,
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = theme.accentPurple
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Chat,
                                contentDescription = null,
                                tint = theme.iconOnAccent,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Message",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = theme.iconOnAccent
                            )
                        }
                    }
                }

                // Statistics - White cards
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        StatCard(
                            value = coach.sessions.toString(),
                            label = "Sessions",
                            valueColor = theme.accentBlue,
                            theme = theme,
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            value = coach.followers.toString(),
                            label = "Followers",
                            valueColor = theme.primaryText,
                            theme = theme,
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            value = coach.experience,
                            label = "Experience",
                            valueColor = theme.primaryText,
                            theme = theme,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Tabs
                item {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 16.dp),
                        shape = RoundedCornerShape(16.dp),
                        color = theme.glassSurface,
                        border = BorderStroke(2.dp, theme.glassBorder)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(4.dp),
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                ProfileTabButton(
                                    text = "About",
                                    isSelected = selectedTab == "about",
                                    onClick = { selectedTab = "about" },
                                    theme = theme,
                                    modifier = Modifier.weight(1f)
                                )
                                ProfileTabButton(
                                    text = "Sessions",
                                    isSelected = selectedTab == "sessions",
                                    onClick = { selectedTab = "sessions" },
                                    theme = theme,
                                    modifier = Modifier.weight(1f)
                                )
                                ProfileTabButton(
                                    text = "Reviews",
                                    isSelected = selectedTab == "reviews",
                                    onClick = { selectedTab = "reviews" },
                                    theme = theme,
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            // Tab Content
                            when (selectedTab) {
                                "about" -> {
                                    AboutTabContent(coach = coach, theme = theme)
                                }
                                "sessions" -> {
                                    SessionsTabContent(theme = theme)
                                }
                                "reviews" -> {
                                    ReviewsTabContent(coach = coach, theme = theme)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

data class CoachProfile(
    val name: String,
    val avatar: String,
    val isVerified: Boolean,
    val rating: Double,
    val totalReviews: Int,
    val location: String,
    val sessions: Int,
    val followers: Int,
    val experience: String,
    val bio: String,
    val specializations: List<String>,
    val certifications: List<Certification>
)

data class Certification(
    val name: String,
    val code: String
)

data class CoachSession(
    val id: String,
    val title: String,
    val icon: String,
    val date: String,
    val time: String,
    val location: String,
    val price: Int,
    val spotsAvailable: Int
)

@Composable
private fun StatCard(
    value: String,
    label: String,
    valueColor: Color = Color(0xFF111827),
    theme: AppThemeColors,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = theme.glassSurface,
        border = BorderStroke(2.dp, theme.glassBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = valueColor
            )
            Text(
                text = label,
                fontSize = 12.sp,
                color = theme.mutedText
            )
        }
    }
}

@Composable
private fun ProfileTabButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    theme: AppThemeColors,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(40.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) theme.accentPurple else theme.glassSurface,
            contentColor = if (isSelected) theme.iconOnAccent else theme.primaryText
        ),
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            color = if (isSelected) theme.iconOnAccent else theme.primaryText,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

@Composable
private fun AboutTabContent(coach: CoachProfile, theme: AppThemeColors) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // About Section
        Column {
            Text(
                text = "About",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = theme.primaryText,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = coach.bio,
                fontSize = 14.sp,
                color = theme.secondaryText,
                lineHeight = 20.sp
            )
        }

        // Specializations Section
        Column {
            Text(
                text = "Specializations",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = theme.primaryText,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            // Display specializations in a wrap layout (simple approach)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    coach.specializations.forEachIndexed { index, specialization ->
                        if (index % 2 == 0 || coach.specializations.size <= 2) {
                            ChipCard(text = specialization, theme = theme)
                        }
                    }
                }
                if (coach.specializations.size > 2) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        coach.specializations.forEachIndexed { index, specialization ->
                            if (index % 2 == 1) {
                                ChipCard(text = specialization, theme = theme)
                            }
                        }
                    }
                }
            }
        }

        // Certifications Section
        Column {
            Text(
                text = "Certifications",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = theme.primaryText,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                coach.certifications.forEach { cert ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = theme.subtleSurface,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "✓",
                                    fontSize = 16.sp,
                                    color = theme.success,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Column {
                            Text(
                                text = cert.name,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = theme.primaryText
                            )
                            Text(
                                text = cert.code,
                                fontSize = 12.sp,
                                color = theme.mutedText
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ChipCard(text: String, theme: AppThemeColors) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = theme.subtleSurface,
        border = BorderStroke(1.dp, theme.glassBorder)
    ) {
        Text(
            text = text,
            fontSize = 13.sp,
            color = theme.primaryText,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        )
    }
}

@Composable
private fun SessionsTabContent(theme: AppThemeColors) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Upcoming Sessions",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = theme.primaryText
        )

        val sessions = listOf(
            CoachSession(
                id = "1",
                title = "Sunrise HIIT Bootcamp",
                icon = "🌅",
                date = "Tomorrow",
                time = "6:30 AM",
                location = "Downtown Arena",
                price = 25,
                spotsAvailable = 4
            ),
            CoachSession(
                id = "2",
                title = "Strength & Conditioning",
                icon = "🏋️",
                date = "Nov 10",
                time = "5:00 PM",
                location = "Peak Performance Gym",
                price = 30,
                spotsAvailable = 2
            )
        )

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            sessions.forEach { session ->
                CoachSessionCard(session = session, theme = theme)
            }
        }
    }
}

@Composable
private fun CoachSessionCard(
    session: CoachSession,
    theme: AppThemeColors,
    onBookClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = theme.glassSurface
        ),
        border = BorderStroke(2.dp, theme.glassBorder)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Surface(
                    modifier = Modifier.size(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = theme.subtleSurface
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = session.icon,
                            fontSize = 24.sp
                        )
                    }
                }

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = session.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = theme.primaryText,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = null,
                            tint = theme.mutedText,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "${session.date} • ${session.time}",
                            fontSize = 13.sp,
                            color = theme.mutedText
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = theme.mutedText,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = session.location,
                            fontSize = 13.sp,
                            color = theme.mutedText
                        )
                    }
                }

                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text(
                        text = "${session.price}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = theme.accentGreen
                    )
                    Text(
                        text = "${session.spotsAvailable} spots",
                        fontSize = 12.sp,
                        color = theme.mutedText,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            Button(
                onClick = onBookClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .height(40.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = theme.accentPurple
                )
            ) {
                Text(
                    text = "Book Session",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = theme.iconOnAccent
                )
            }
        }
    }
}

@Composable
private fun ReviewsTabContent(coach: CoachProfile, theme: AppThemeColors) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Client Reviews",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = theme.primaryText
        )

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            repeat(3) {
                ReviewCard(
                    review = Review(
                        id = "1",
                        userName = "Sarah M.",
                        userAvatar = "https://api.dicebear.com/7.x/avataaars/svg?seed=Sarah",
                        rating = 5,
                        comment = "Alex is incredibly motivating and keeps sessions challenging!",
                        date = "Oct 20, 2025"
                    ),
                    theme = theme
                )
            }
        }
    }
}

@Composable
private fun ReviewCard(review: Review, theme: AppThemeColors) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = theme.glassSurface
        ),
        border = BorderStroke(2.dp, theme.glassBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AsyncImage(
                    model = review.userAvatar,
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .border(2.dp, theme.glassBorder, CircleShape)
                )
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = review.userName,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = theme.primaryText
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = theme.accentGold,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = review.rating.toString(),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = theme.primaryText
                            )
                        }
                    }
                    Text(
                        text = review.date,
                        fontSize = 12.sp,
                        color = theme.mutedText,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = review.comment,
                fontSize = 14.sp,
                color = theme.primaryText,
                lineHeight = 20.sp
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CoachProfilePagePreview() {
    DamAndroidTheme {
        CoachProfilePage(
            onBack = { },
            onMessage = { }
        )
    }
}

