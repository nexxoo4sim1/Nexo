package com.example.damandroid
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import coil.compose.AsyncImage
import com.example.damandroid.ui.theme.LocalThemeController
import kotlinx.coroutines.delay

data class MatchUserProfile(
    val id: String,
    val name: String,
    val age: Int,
    val avatar: String,
    val coverImage: String,
    val location: String,
    val distance: String,
    val bio: String,
    val sports: List<Sport>,
    val interests: List<String>,
    val rating: Double,
    val activitiesJoined: Int
)

data class Sport(
    val name: String,
    val icon: String,
    val level: String
)

val mockProfiles = listOf(
    MatchUserProfile(
        id = "1",
        name = "Jessica",
        age = 26,
        avatar = "https://i.pravatar.cc/400?img=45",
        coverImage = "https://images.unsplash.com/photo-1571902943202-507ec2618e8f?w=800&h=600&fit=crop",
        location = "Downtown",
        distance = "2.3 mi",
        bio = "Love staying active and meeting new people! Always up for a challenge 🏃‍♀️",
        sports = listOf(
            Sport("Running", "🏃", "Intermediate"),
            Sport("Yoga", "🧘", "Advanced"),
            Sport("Tennis", "🎾", "Beginner")
        ),
        interests = listOf("Morning workouts", "Trail running", "Wellness"),
        rating = 4.8,
        activitiesJoined = 34
    ),
    MatchUserProfile(
        id = "2",
        name = "Marcus",
        age = 29,
        avatar = "https://i.pravatar.cc/400?img=12",
        coverImage = "https://images.unsplash.com/photo-1546483875-ad9014c88eba?w=800&h=600&fit=crop",
        location = "West Side",
        distance = "1.8 mi",
        bio = "Basketball enthusiast and fitness lover. Let's ball! 🏀",
        sports = listOf(
            Sport("Basketball", "🏀", "Advanced"),
            Sport("Swimming", "🏊", "Intermediate"),
            Sport("Cycling", "🚴", "Intermediate")
        ),
        interests = listOf("Team sports", "Competitive", "Weekend warrior"),
        rating = 4.9,
        activitiesJoined = 52
    ),
    MatchUserProfile(
        id = "3",
        name = "Olivia",
        age = 24,
        avatar = "https://i.pravatar.cc/400?img=32",
        coverImage = "https://images.unsplash.com/photo-1552196563-55cd4e45efb3?w=800&h=600&fit=crop",
        location = "North Beach",
        distance = "3.1 mi",
        bio = "Beach volleyball and ocean lover 🌊 Looking for active friends!",
        sports = listOf(
            Sport("Volleyball", "🏐", "Advanced"),
            Sport("Surfing", "🏄", "Intermediate"),
            Sport("Beach Sports", "⛱️", "Intermediate")
        ),
        interests = listOf("Beach activities", "Social sports", "Outdoor fun"),
        rating = 4.7,
        activitiesJoined = 28
    ),
    MatchUserProfile(
        id = "4",
        name = "David",
        age = 31,
        avatar = "https://i.pravatar.cc/400?img=15",
        coverImage = "https://images.unsplash.com/photo-1534438327276-14e5300c3a48?w=800&h=600&fit=crop",
        location = "Central Park",
        distance = "1.2 mi",
        bio = "Gym rat turned outdoor enthusiast. Always exploring new activities!",
        sports = listOf(
            Sport("Hiking", "🥾", "Advanced"),
            Sport("Rock Climbing", "🧗", "Intermediate"),
            Sport("CrossFit", "💪", "Advanced")
        ),
        interests = listOf("Adventure", "Strength training", "Nature"),
        rating = 4.9,
        activitiesJoined = 67
    ),
    MatchUserProfile(
        id = "5",
        name = "Sophia",
        age = 27,
        avatar = "https://i.pravatar.cc/400?img=27",
        coverImage = "https://images.unsplash.com/photo-1476480862126-209bfaa8edc8?w=800&h=600&fit=crop",
        location = "Riverside",
        distance = "2.7 mi",
        bio = "Dance, pilates, and positive vibes ✨ Let's move together!",
        sports = listOf(
            Sport("Dance", "💃", "Advanced"),
            Sport("Pilates", "🧘‍♀️", "Intermediate"),
            Sport("Zumba", "🎵", "Intermediate")
        ),
        interests = listOf("Group classes", "Music & movement", "Wellness"),
        rating = 4.8,
        activitiesJoined = 41
    )
)

@Stable
private data class QuickMatchThemeColors(
    val isDark: Boolean,
    val backgroundGradient: List<Color>,
    val orbGradients: List<List<Color>>,
    val cardGlowGradient: List<Color>,
    val translucentSurface: Color,
    val translucentBorder: Color,
    val headerIcon: Color,
    val headerTitle: Color,
    val primaryText: Color,
    val secondaryText: Color,
    val chipSurface: Color,
    val chipBorder: Color,
    val buttonGlowGradient: List<Color>,
    val buttonFillGradient: List<Color>,
    val buttonBorder: Color,
    val buttonText: Color
)

@Composable
private fun rememberQuickMatchThemeColors(): QuickMatchThemeColors {
    val isDark = LocalThemeController.current.isDarkMode
    return remember(isDark) {
        if (isDark) {
            QuickMatchThemeColors(
                isDark = true,
                backgroundGradient = listOf(
                    Color(0xFF0F172A),
                    Color(0xFF1E293B),
                    Color(0xFF111827)
                ),
                orbGradients = listOf(
                    listOf(Color(0xFF3730A3).copy(alpha = 0.35f), Color(0xFF312E81).copy(alpha = 0.25f)),
                    listOf(Color(0xFFBE123C).copy(alpha = 0.4f), Color(0xFF9D174D).copy(alpha = 0.25f)),
                    listOf(Color(0xFF1E3A8A).copy(alpha = 0.4f), Color(0xFF1D4ED8).copy(alpha = 0.3f)),
                    listOf(Color(0xFFF59E0B).copy(alpha = 0.4f), Color(0xFFD97706).copy(alpha = 0.25f))
                ),
                cardGlowGradient = listOf(
                    Color(0xFF312E81).copy(alpha = 0.5f),
                    Color(0xFF1E293B).copy(alpha = 0.4f)
                ),
                translucentSurface = Color(0xFF1F2937),
                translucentBorder = Color(0xFF334155),
                headerIcon = Color(0xFFE2E8F0),
                headerTitle = Color(0xFFF8FAFC),
                primaryText = Color(0xFFE2E8F0),
                secondaryText = Color(0xFF94A3B8),
                chipSurface = Color(0xFF1E293B),
                chipBorder = Color(0xFF334155),
                buttonGlowGradient = listOf(
                    Color(0xFF3730A3).copy(alpha = 0.3f),
                    Color(0xFF9D174D).copy(alpha = 0.3f),
                    Color(0xFF1E1B4B).copy(alpha = 0.3f)
                ),
                buttonFillGradient = listOf(
                    Color(0xFF1F2937),
                    Color(0xFF111827)
                ),
                buttonBorder = Color(0xFF334155),
                buttonText = Color(0xFFE2E8F0)
            )
        } else {
            QuickMatchThemeColors(
                isDark = false,
                backgroundGradient = listOf(
                    Color(0xFFF5F3FF),
                    Color(0xFFFDF4FF),
                    Color(0xFFF0F4F8)
                ),
                orbGradients = listOf(
                    listOf(Color(0xFFE9D5FF).copy(alpha = 0.4f), Color(0xFFDDD6FE).copy(alpha = 0.3f)),
                    listOf(Color(0xFFFCE7F3).copy(alpha = 0.5f), Color(0xFFFBCFE8).copy(alpha = 0.3f)),
                    listOf(Color(0xFFE0E7FF).copy(alpha = 0.4f), Color(0xFFC7D2FE).copy(alpha = 0.3f)),
                    listOf(Color(0xFFFEF3C7).copy(alpha = 0.4f), Color(0xFFFDE68A).copy(alpha = 0.3f))
                ),
                cardGlowGradient = listOf(
                    Color(0xFFE9D5FF).copy(alpha = 0.6f),
                    Color(0xFFDDD6FE).copy(alpha = 0.5f)
                ),
                translucentSurface = Color.White,
                translucentBorder = Color.White.copy(alpha = 0.8f),
                headerIcon = Color(0xFF2D3748),
                headerTitle = Color(0xFF1A202C),
                primaryText = Color(0xFF2D3748),
                secondaryText = Color(0xFF718096),
                chipSurface = Color.White,
                chipBorder = Color.White.copy(alpha = 0.6f),
                buttonGlowGradient = listOf(
                    Color(0xFFE9D5FF).copy(alpha = 0.3f),
                    Color(0xFFFCE7F3).copy(alpha = 0.3f),
                    Color(0xFFE0E7FF).copy(alpha = 0.3f)
                ),
                buttonFillGradient = listOf(
                    Color.White.copy(alpha = 0.7f),
                    Color.White.copy(alpha = 0.5f)
                ),
                buttonBorder = Color.White.copy(alpha = 0.7f),
                buttonText = Color(0xFF2D3748)
            )
        }
    }
}

@Composable
fun QuickMatchPage(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentIndex by remember { mutableStateOf(0) }
    var matches by remember { mutableStateOf(setOf<String>()) }
    var showMatch by remember { mutableStateOf(false) }
    var matchedUser by remember { mutableStateOf<MatchUserProfile?>(null) }

    val currentProfiles = remember { mockProfiles }
    val currentProfile = if (currentIndex < currentProfiles.size) currentProfiles[currentIndex] else null

    val colors = rememberQuickMatchThemeColors()

    LaunchedEffect(showMatch) {
        if (showMatch) {
            delay(3000)
            showMatch = false
            matchedUser = null
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = colors.backgroundGradient
                )
            )
    ) {
        FloatingMatchOrbs(colors)

        if (currentIndex >= currentProfiles.size) {
            AllCaughtUpScreen(onBack = onBack, colors = colors)
        } else if (currentProfile != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                MatchHeader(
                    matchesCount = matches.size,
                    onBack = onBack,
                    colors = colors
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    for (i in 1..2) {
                        if (currentIndex + i < currentProfiles.size) {
                            NextCardPreview(
                                profile = currentProfiles[currentIndex + i],
                                index = i,
                                colors = colors,
                                modifier = Modifier.zIndex((currentProfiles.size - i).toFloat())
                            )
                        }
                    }

                    SwipeCard(
                        profile = currentProfile,
                        onSwipe = { direction ->
                            if (direction == "right") {
                                val isMatch = kotlin.random.Random.nextBoolean()
                                if (isMatch) {
                                    matchedUser = currentProfile
                                    showMatch = true
                                    matches = matches + currentProfile.id
                                }
                            }
                            currentIndex++
                        },
                        modifier = Modifier.zIndex(1000f),
                        colors = colors
                    )
                }

                ActionButtons(
                    onLike = {
                        if (currentProfile != null) {
                            val isMatch = kotlin.random.Random.nextBoolean()
                            if (isMatch) {
                                matchedUser = currentProfile
                                showMatch = true
                                matches = matches + currentProfile.id
                            }
                            currentIndex++
                        }
                    },
                    onPass = { currentIndex++ },
                    colors = colors,
                    modifier = Modifier
                        .padding(bottom = 70.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }
        }

        if (showMatch && matchedUser != null) {
            MatchModal(
                user = matchedUser!!,
                modifier = Modifier.zIndex(2000f)
            )
        }
    }
}

@Composable
private fun FloatingMatchOrbs(colors: QuickMatchThemeColors) {
    val orbGradients = colors.orbGradients
    val orb1 = orbGradients.getOrElse(0) { listOf(Color.Transparent, Color.Transparent) }
    val orb2 = orbGradients.getOrElse(1) { listOf(Color.Transparent, Color.Transparent) }
    val orb3 = orbGradients.getOrElse(2) { listOf(Color.Transparent, Color.Transparent) }
    val orb4 = orbGradients.getOrElse(3) { listOf(Color.Transparent, Color.Transparent) }

    Box(
        modifier = Modifier
            .offset(x = 40.dp, y = 80.dp)
            .size(128.dp)
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    colors = orb1
                )
            )
            .blur(48.dp)
    )

    Box(
        modifier = Modifier
            .offset(x = (-40).dp, y = (-160).dp)
            .size(160.dp)
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    colors = orb2
                )
            )
            .blur(48.dp)
    )

    Box(
        modifier = Modifier
            .offset(x = 200.dp, y = 300.dp)
            .size(96.dp)
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    colors = orb3
                )
            )
            .blur(32.dp)
    )

    Box(
        modifier = Modifier
            .offset(x = 300.dp, y = 120.dp)
            .size(80.dp)
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    colors = orb4
                )
            )
            .blur(32.dp)
    )
}

@Composable
private fun MatchHeader(
    matchesCount: Int,
    onBack: () -> Unit,
    colors: QuickMatchThemeColors
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(horizontal = 16.dp)
            .padding(top = 8.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .size(48.dp)
                    .offset(x = (-4).dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = colors.headerIcon,
                    modifier = Modifier.size(26.dp)
                )
            }
            Text(
                text = "Quick Match",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = colors.headerTitle,
                letterSpacing = (-0.5).sp
            )
        }

        // Matches badge
        Box(
            modifier = Modifier.padding(start = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .offset(x = 4.dp, y = 4.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                Color(0xFFEC4899).copy(alpha = 0.5f),
                                Color(0xFFA855F7).copy(alpha = 0.5f)
                            )
                        )
                    )
                    .blur(10.dp)
            )
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = Color.Transparent,
                modifier = Modifier
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFFEC4899),
                                Color(0xFFA855F7)
                            )
                        ),
                        RoundedCornerShape(24.dp)
                    )
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = Color.White
                    )
                    Text(
                        text = "$matchesCount",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun NextCardPreview(
    profile: MatchUserProfile,
    index: Int,
    colors: QuickMatchThemeColors,
    modifier: Modifier = Modifier
) {
    val scale = 1f - (index * 0.05f)
    val offsetY = index * 10f
    val opacity = 1f - (index * 0.2f)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(530.dp)
            .offset(y = (-offsetY).dp)
            .scale(scale)
            .alpha(opacity)
            .clip(RoundedCornerShape(24.dp))
            .background(
                colors.translucentSurface.copy(alpha = if (colors.isDark) 0.82f else 0.7f),
                RoundedCornerShape(24.dp)
            )
            .border(
                2.dp,
                colors.translucentBorder.copy(
                    alpha = if (colors.isDark) 0.6f else colors.translucentBorder.alpha
                ),
                RoundedCornerShape(24.dp)
            )
    )
}

@Composable
private fun SwipeCard(
    profile: MatchUserProfile,
    onSwipe: (String) -> Unit,
    colors: QuickMatchThemeColors,
    modifier: Modifier = Modifier
) {
    var offsetX by remember { mutableStateOf(0f) }
    var isDragging by remember { mutableStateOf(false) }

    val rotation = offsetX / 20f
    val opacity = when {
        offsetX < -100 -> 0f
        offsetX > 100 -> 0f
        offsetX < -50 -> 1f - ((-50 - offsetX) / 50f)
        offsetX > 50 -> 1f - ((offsetX - 50) / 50f)
        else -> 1f
    }

    val likeOpacity by animateFloatAsState(
        targetValue = if (offsetX > 0) (offsetX / 100f).coerceIn(0f, 1f) else 0f,
        label = "like-opacity"
    )

    val nopeOpacity by animateFloatAsState(
        targetValue = if (offsetX < 0) ((-offsetX) / 100f).coerceIn(0f, 1f) else 0f,
        label = "nope-opacity"
    )

    val surfaceColor = colors.translucentSurface
    val borderColor = colors.translucentBorder

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(530.dp)
            .offset(x = offsetX.dp)
            .rotate(rotation)
            .alpha(opacity)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragEnd = {
                        if (kotlin.math.abs(offsetX) > 100) {
                            val direction = if (offsetX > 0) "right" else "left"
                            onSwipe(direction)
                        } else {
                            offsetX = 0f
                        }
                        isDragging = false
                    },
                    onDrag = { change, dragAmount ->
                        isDragging = true
                        offsetX += dragAmount.x
                        change.consume()
                    }
                )
            }
    ) {
        // Outer glow
        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset(x = (-4).dp, y = (-4).dp)
                .clip(RoundedCornerShape(24.dp))
                .background(
                    Brush.linearGradient(
                        colors = colors.cardGlowGradient
                    )
                )
                .blur(16.dp)
        )

        Card(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        surfaceColor.copy(alpha = if (colors.isDark) 0.92f else 0.7f),
                        RoundedCornerShape(24.dp)
                    )
                    .border(
                        2.dp,
                        borderColor.copy(alpha = if (colors.isDark) 0.9f else borderColor.alpha),
                        RoundedCornerShape(24.dp)
                    )
            ) {
                // Top highlight
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(128.dp)
                        .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    surfaceColor.copy(alpha = if (colors.isDark) 0.35f else 0.6f),
                                    Color.Transparent
                                )
                            )
                        )
                        .offset(y = (-224).dp)
                )
                
                // Inner shadow for depth
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(24.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    surfaceColor.copy(alpha = if (colors.isDark) 0.25f else 0.5f),
                                    Color.Transparent
                                )
                            )
                        )
                        .offset(x = 2.dp, y = 2.dp)
                )
                
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                // Cover Image
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(224.dp)
                ) {
                    AsyncImage(
                        model = profile.coverImage,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Black.copy(alpha = 0.2f),
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.8f)
                                    )
                                )
                            )
                    )

                    // Swipe indicators
                    if (likeOpacity > 0) {
                        Box(
                            modifier = Modifier
                                .offset(x = 12.dp, y = 12.dp)
                                .alpha(likeOpacity)
                                .rotate(-20f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF86EFAC))
                                .border(2.dp, Color.White, RoundedCornerShape(12.dp))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "LIKE",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    if (nopeOpacity > 0) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .offset(x = (-12).dp, y = 12.dp)
                                .alpha(nopeOpacity)
                                .rotate(20f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFF87171))
                                .border(2.dp, Color.White, RoundedCornerShape(12.dp))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "NOPE",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    // Name and location
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(horizontal = 10.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "${profile.name}, ${profile.age}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = Color.White.copy(alpha = 0.9f)
                            )
                            Text(
                                text = "${profile.location} • ${profile.distance}",
                                fontSize = 11.sp,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                        }
                    }
                }

                // Profile Info
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(10.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Stats
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        StatCard(
                            title = "Rating",
                            value = "${profile.rating}",
                            icon = Icons.Default.Star,
                            modifier = Modifier.weight(1f),
                            colors = colors
                        )
                        StatCard(
                            title = "Activities",
                            value = "${profile.activitiesJoined}",
                            modifier = Modifier.weight(1f),
                            colors = colors
                        )
                    }

                    // Bio
                    Text(
                        text = profile.bio,
                        fontSize = 13.sp,
                        color = colors.primaryText,
                        lineHeight = 18.sp
                    )

                    // Sports
                    Text(
                        text = "Favorite Sports",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = colors.primaryText,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        profile.sports.forEach { sport ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(80.dp)
                                        .clip(CircleShape)
                                        .background(
                                            colors.chipSurface.copy(alpha = if (colors.isDark) 0.9f else 0.6f),
                                            CircleShape
                                        )
                                        .border(
                                            2.dp,
                                            colors.chipBorder.copy(alpha = if (colors.isDark) 0.65f else colors.chipBorder.alpha),
                                            CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = sport.icon,
                                            fontSize = 24.sp
                                        )
                                        Text(
                                            text = sport.name,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = colors.primaryText
                                        )
                                    }
                                }
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = Color(0xFFA855F7),
                                    modifier = Modifier.padding(top = 4.dp)
                                ) {
                                    Text(
                                        text = sport.level,
                                        fontSize = 8.sp,
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Interests
                    Text(
                        text = "Interests",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = colors.primaryText,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        profile.interests.forEach { interest ->
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = colors.chipSurface.copy(alpha = if (colors.isDark) 0.85f else 0.6f),
                                border = BorderStroke(
                                    2.dp,
                                    colors.chipBorder.copy(alpha = if (colors.isDark) 0.6f else colors.chipBorder.alpha)
                                )
                            ) {
                                Text(
                                    text = interest,
                                    fontSize = 10.sp,
                                    color = colors.secondaryText,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
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
private fun StatCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    colors: QuickMatchThemeColors,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = colors.chipSurface.copy(alpha = if (colors.isDark) 0.85f else 0.6f),
        border = BorderStroke(
            2.dp,
            colors.chipBorder.copy(alpha = if (colors.isDark) 0.6f else colors.chipBorder.alpha)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (icon != null) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = Color(0xFFFFD700)
                    )
                    Text(
                        text = value,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.primaryText
                    )
                }
            } else {
                Text(
                    text = value,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.primaryText,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
            }
            Text(
                text = title,
                fontSize = 10.sp,
                color = colors.secondaryText
            )
        }
    }
}


@Composable
private fun ActionButtons(
    onLike: () -> Unit,
    onPass: () -> Unit,
    colors: QuickMatchThemeColors,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Pass button
        Box {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .offset(x = 4.dp, y = 4.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                Color(0xFFF87171).copy(alpha = 0.4f),
                                Color(0xFFEF4444).copy(alpha = 0.3f)
                            )
                        )
                    )
                    .blur(8.dp)
            )
            IconButton(
                onClick = onPass,
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        colors.translucentSurface.copy(alpha = if (colors.isDark) 0.65f else 0.7f),
                        CircleShape
                    )
                    .border(
                        2.dp,
                        colors.translucentBorder.copy(alpha = if (colors.isDark) 0.8f else colors.translucentBorder.alpha),
                        CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Pass",
                    modifier = Modifier.size(24.dp),
                    tint = Color(0xFFF87171)
                )
            }
        }

        // Like button
        Box {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .offset(x = 4.dp, y = 4.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                Color(0xFF86EFAC).copy(alpha = 0.4f),
                                Color(0xFF6EE7B7).copy(alpha = 0.3f)
                            )
                        )
                    )
                    .blur(8.dp)
            )
            IconButton(
                onClick = onLike,
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        Color(0xFF86EFAC),
                        CircleShape
                    )
                    .border(2.dp, Color.White, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Like",
                    modifier = Modifier.size(24.dp),
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
private fun AllCaughtUpScreen(
    onBack: () -> Unit,
    colors: QuickMatchThemeColors
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp, top = 8.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .size(48.dp)
                    .offset(x = (-4).dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = colors.headerIcon,
                    modifier = Modifier.size(26.dp)
                )
            }
            Text(
                text = "Quick Match",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = colors.headerTitle,
                letterSpacing = (-0.5).sp,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Box(
            modifier = Modifier
                .size(96.dp)
                .padding(bottom = 24.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .offset(x = (-4).dp, y = (-4).dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.linearGradient(
                            colors = colors.cardGlowGradient
                        )
                    )
                    .blur(16.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        colors.translucentSurface.copy(alpha = if (colors.isDark) 0.9f else 0.7f),
                        RoundedCornerShape(24.dp)
                    )
                    .border(
                        2.dp,
                        colors.translucentBorder.copy(alpha = if (colors.isDark) 0.85f else colors.translucentBorder.alpha),
                        RoundedCornerShape(24.dp)
                    ),
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
                                    colors.translucentSurface.copy(alpha = if (colors.isDark) 0.35f else 0.6f),
                                    Color.Transparent
                                )
                            )
                        )
                        .offset(y = (-48).dp)
                )
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = Color(0xFFA855F7)
                )
            }
        }

        Text(
            text = "All caught up!",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = colors.primaryText
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "You've seen all available profiles. Check back later for more sport buddies!",
            fontSize = 15.sp,
            color = colors.secondaryText,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Box {
            Box(
                modifier = Modifier
                    .offset(x = 4.dp, y = 4.dp)
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.linearGradient(
                        colors = colors.buttonGlowGradient
                        )
                    )
                    .blur(16.dp)
            )
            Button(
                onClick = onBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.linearGradient(
                                colors = colors.buttonFillGradient
                            ),
                            RoundedCornerShape(24.dp)
                        )
                        .border(2.dp, colors.buttonBorder, RoundedCornerShape(24.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Back to Home",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.buttonText
                    )
                }
            }
        }
    }
}

@Composable
private fun MatchModal(
    user: MatchUserProfile,
    modifier: Modifier = Modifier
) {
    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = spring(
            dampingRatio = 0.6f,
            stiffness = 300f
        ),
        label = "match-modal-scale"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFEC4899),
                        Color(0xFFA855F7)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(24.dp)
                .scale(scale)
        ) {
            Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = Color.White
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "It's a Match!",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "You and ${user.name} both like each other",
                fontSize = 17.sp,
                color = Color.White.copy(alpha = 0.9f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            Surface(
                shape = CircleShape,
                modifier = Modifier.size(96.dp),
                border = BorderStroke(4.dp, Color.White)
            ) {
                AsyncImage(
                    model = user.avatar,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Starting a conversation...",
                fontSize = 15.sp,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}

