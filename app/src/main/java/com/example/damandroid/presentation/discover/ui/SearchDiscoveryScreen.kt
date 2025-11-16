package com.example.damandroid.presentation.discover.ui

import android.graphics.Color.parseColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import com.example.damandroid.domain.model.DiscoverOverview
import com.example.damandroid.domain.model.DiscoverSportCategory
import com.example.damandroid.domain.model.DiscoverUser
import com.example.damandroid.domain.model.FeaturedCoach
import com.example.damandroid.domain.model.TrendingActivity
import com.example.damandroid.presentation.discover.model.DiscoverUiState
import com.example.damandroid.presentation.discover.viewmodel.DiscoverViewModel
import com.example.damandroid.ui.theme.AppThemeColors
import com.example.damandroid.ui.theme.LocalThemeController
import com.example.damandroid.ui.theme.rememberAppThemeColors

@Composable
fun DiscoverRoute(
    viewModel: DiscoverViewModel,
    onBack: (() -> Unit)? = null,
    onCoachClick: ((String) -> Unit)? = null,
    onChatClick: ((String, String, String?, Boolean) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    DiscoverScreen(
        state = uiState,
        onBack = onBack,
        onCoachClick = onCoachClick,
        onChatClick = onChatClick,
        onCategorySelected = viewModel::onCategorySelected,
        onRefresh = viewModel::refresh,
        onSearchQueryChange = viewModel::onSearchQueryChange,
        modifier = modifier
    )
}

@Composable
fun DiscoverScreen(
    state: DiscoverUiState,
    onBack: (() -> Unit)?,
    onCoachClick: ((String) -> Unit)?,
    onChatClick: ((String, String, String?, Boolean) -> Unit)?,
    onCategorySelected: (String?) -> Unit,
    onRefresh: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    when {
        state.isLoading -> LoadingState(modifier)
        state.error != null -> ErrorState(message = state.error, onRetry = onRefresh, modifier = modifier)
        state.overview != null -> DiscoverContent(
            overview = state.overview,
            searchQuery = state.searchQuery,
            selectedCategory = state.selectedCategory,
            onCategorySelected = onCategorySelected,
            onChatClick = onChatClick,
            onSearchQueryChange = onSearchQueryChange,
            onBack = onBack,
            onCoachClick = onCoachClick,
            modifier = modifier
        )
        else -> ErrorState(
            message = "No data available",
            onRetry = onRefresh,
            modifier = modifier
        )
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
        Text(text = message)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onRetry) {
                Text(text = "Retry")
            }
        }
    }
}

@Composable
private fun DiscoverContent(
    overview: DiscoverOverview,
    searchQuery: String,
    selectedCategory: String?,
    onCategorySelected: (String?) -> Unit,
    onChatClick: ((String, String, String?, Boolean) -> Unit)?,
    onSearchQueryChange: (String) -> Unit,
    onBack: (() -> Unit)?,
    onCoachClick: ((String) -> Unit)?,
    modifier: Modifier = Modifier
) {
    val themeController = LocalThemeController.current
    val colors = rememberAppThemeColors(themeController.isDarkMode)

    val filteredCategories = overview.sportCategories.filter {
        searchQuery.isBlank() || it.name.contains(searchQuery, ignoreCase = true)
    }
    val selectedIcon = selectedCategory?.let { name ->
        overview.sportCategories.find { it.name.equals(name, ignoreCase = true) }?.icon
    }
    val filteredActivities = overview.trendingActivities.filter { activity ->
        val searchMatch = searchQuery.isBlank() || activity.title.contains(searchQuery, ignoreCase = true)
        val categoryMatch = selectedCategory == null ||
                activity.title.contains(selectedCategory, ignoreCase = true) ||
                (selectedIcon != null && activity.sportIcon == selectedIcon)
        searchMatch && categoryMatch
    }
    val filteredUsers = overview.activeUsers.filter {
        searchQuery.isBlank() || it.name.contains(searchQuery, ignoreCase = true)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colors.backgroundGradient)
    ) {
        FloatingDiscoveryOrbs(colors)

        Column(modifier = Modifier.fillMaxSize()) {
            DiscoverHeader(
                searchQuery = searchQuery,
                onSearchQueryChange = onSearchQueryChange,
                onBack = onBack,
                colors = colors
            )

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    top = 16.dp,
                    end = 16.dp,
                    bottom = 96.dp
                ),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                item {
                    FeaturedCoachSection(
                        coach = overview.featuredCoach,
                        onCoachClick = onCoachClick,
                        colors = colors
                    )
                }

                item {
                    SportCategoriesSection(
                        categories = filteredCategories,
                        colors = colors,
                        selectedCategory = selectedCategory,
                        onCategorySelected = onCategorySelected
                    )
                }

                item {
                    TrendingActivitiesSection(
                        activities = filteredActivities,
                        colors = colors,
                        onChatNow = { activityId, title ->
                            launchChatForActivity(activityId, title, onChatClick)
                        }
                    )
                }
                // Removed "Active Now" section per request
            }
        }
    }
}

@Composable
private fun DiscoverHeader(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onBack: (() -> Unit)?,
    colors: AppThemeColors
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = colors.glassSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        border = BorderStroke(2.dp, colors.glassBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .padding(bottom = 12.dp),
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
                            tint = colors.primaryText,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }
                Text(
                    text = "Discover",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.primaryText,
                    letterSpacing = (-0.5).sp
                )
            }

            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = {
                    Text(
                        "Search sports, people, or places...",
                        color = colors.mutedText.copy(alpha = 0.6f),
                        fontSize = 15.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = colors.mutedText.copy(alpha = 0.6f),
                        modifier = Modifier.size(18.dp)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                shape = RoundedCornerShape(16.dp),
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyMedium.copy(fontSize = 15.sp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = colors.primaryText,
                    unfocusedTextColor = colors.primaryText,
                    focusedBorderColor = colors.accentPurple.copy(alpha = 0.45f),
                    unfocusedBorderColor = colors.glassBorder,
                    cursorColor = colors.primaryText,
                    focusedContainerColor = colors.glassSurface,
                    unfocusedContainerColor = colors.glassSurface.copy(alpha = 0.92f)
                )
            )
        }
    }
}

@Composable
private fun FeaturedCoachSection(
    coach: FeaturedCoach?,
    onCoachClick: ((String) -> Unit)?,
    colors: AppThemeColors
) {
    if (coach == null) return
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        SectionTitle("Verified Coach", colors)
        FeaturedCoachCard(coach = coach, onClick = onCoachClick, colors = colors)
    }
}

@Composable
private fun FeaturedCoachCard(
    coach: FeaturedCoach,
    onClick: ((String) -> Unit)?,
    colors: AppThemeColors
) {
    Box {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset(x = 2.dp, y = 4.dp)
                .height(128.dp)
                .clip(RoundedCornerShape(28.dp))
                .background(
                    Brush.linearGradient(
                        listOf(
                            colors.accentPurple.copy(alpha = 0.18f),
                            colors.accentPink.copy(alpha = 0.18f),
                            colors.accentBlue.copy(alpha = 0.18f)
                        )
                    )
                )
                .blur(18.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(128.dp)
                .then(
                    if (onClick != null) Modifier.clickable { onClick(coach.id) } else Modifier
                ),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = colors.glassSurface),
            border = BorderStroke(2.dp, colors.glassBorder),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = coach.avatarUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .border(3.dp, colors.glassBorder, CircleShape)
                )

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = coach.name,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = colors.primaryText
                        )
                        Surface(
                            modifier = Modifier
                                .height(20.dp)
                                .padding(start = 2.dp),
                            shape = RoundedCornerShape(12.dp),
                            color = colors.success,
                            contentColor = colors.iconOnAccent
                        ) {
                            Text(
                                text = coach.badge,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Text(
                        text = coach.title,
                        fontSize = 13.sp,
                        color = colors.secondaryText,
                        modifier = Modifier.padding(top = 4.dp, bottom = 6.dp)
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            text = "⭐ ${coach.rating}",
                            fontSize = 12.sp,
                            color = colors.mutedText
                        )
                        Text(
                            text = "${coach.reviewCount} reviews",
                            fontSize = 12.sp,
                            color = colors.mutedText
                        )
                    }
                }

                Button(
                    onClick = { onClick?.invoke(coach.id) },
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colors.accentPurple),
                    contentPadding = PaddingValues(horizontal = 18.dp, vertical = 10.dp)
                ) {
                    Text(
                        text = "Book",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = colors.iconOnAccent
                    )
                }
            }
        }
    }
}

@Composable
private fun SportCategoriesSection(
    categories: List<DiscoverSportCategory>,
    colors: AppThemeColors,
    selectedCategory: String?,
    onCategorySelected: (String?) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        SectionTitle("Browse by Sport", colors)
        if (categories.isEmpty()) {
            EmptySectionMessage("No categories match your search", colors)
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                categories.chunked(3).forEach { rowItems ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        rowItems.forEach { category ->
                            SportCategoryCard(
                                category = category,
                                colors = colors,
                                isSelected = selectedCategory?.equals(category.name, ignoreCase = true) == true,
                                onClick = {
                                    onCategorySelected(category.name)
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        if (rowItems.size < 3) {
                            repeat(3 - rowItems.size) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SportCategoryCard(
    category: DiscoverSportCategory,
    colors: AppThemeColors,
    isSelected: Boolean = false,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val categoryColor = parseHexOrDefault(category.colorHex, colors.accentPurple)
    Box(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset(x = 2.dp, y = 2.dp)
                .height(96.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(
                    Brush.linearGradient(
                        listOf(
                            colors.accentPurple.copy(alpha = 0.1f),
                            colors.accentPink.copy(alpha = 0.1f)
                        )
                    )
                )
                .blur(10.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(96.dp)
                .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = colors.glassSurface),
            border = BorderStroke(2.dp, if (isSelected) colors.accentPurple else colors.glassBorder),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(categoryColor.copy(alpha = 0.25f))
                        .border(2.dp, colors.glassBorder, RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = category.icon, fontSize = 22.sp)
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = category.name,
                    fontSize = 12.sp,
                    color = colors.primaryText,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun TrendingActivitiesSection(
    activities: List<TrendingActivity>,
    colors: AppThemeColors,
    onChatNow: (String, String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        SectionTitle("Trending Near You", colors)
        if (activities.isEmpty()) {
            EmptySectionMessage("No activities match your search", colors)
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                activities.forEach { activity ->
                    TrendingActivityCard(
                        activity = activity,
                        colors = colors,
                        onChatNow = { onChatNow(activity.id, activity.title) }
                    )
                }
            }
        }
    }
}

@Composable
private fun TrendingActivityCard(
    activity: TrendingActivity,
    colors: AppThemeColors,
    onChatNow: (() -> Unit)? = null
) {
    Box {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset(x = 2.dp, y = 2.dp)
                .height(110.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(
                    Brush.linearGradient(
                        listOf(
                            colors.accentPurple.copy(alpha = 0.15f),
                            colors.accentPink.copy(alpha = 0.15f),
                            colors.accentBlue.copy(alpha = 0.15f)
                        )
                    )
                )
                .blur(16.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = colors.glassSurface),
            border = BorderStroke(2.dp, colors.glassBorder),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(14.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(colors.subtleSurface)
                        .border(2.dp, colors.glassBorder, RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = activity.sportIcon, fontSize = 24.sp)
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = activity.title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = colors.primaryText,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = colors.mutedText,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = activity.location,
                            fontSize = 12.sp,
                            color = colors.mutedText
                        )
                        Text(
                            text = " • ${activity.date} ${activity.time}",
                            fontSize = 12.sp,
                            color = colors.mutedText
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        AsyncImage(
                            model = activity.hostAvatar,
                            contentDescription = null,
                            modifier = Modifier
                                .size(22.dp)
                                .clip(CircleShape)
                                .border(2.dp, colors.glassBorder, CircleShape)
                        )
                        Text(
                            text = activity.hostName,
                            fontSize = 12.sp,
                            color = colors.secondaryText
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = colors.subtleSurface,
                        border = BorderStroke(2.dp, colors.glassBorder)
                    ) {
                        Text(
                            text = "${activity.maxParticipants - activity.participants} spots",
                            fontSize = 12.sp,
                            color = colors.success,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { onChatNow?.invoke() },
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = colors.accentPurple),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "Chat Now",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = colors.iconOnAccent
                        )
                    }
                }
            }
        }
    }
}

private fun launchChatForActivity(
    activityId: String,
    activityTitle: String,
    onChatClick: ((String, String, String?, Boolean) -> Unit)?
) {
    // Reuse the ActivityRoom flow (join then create/get group chat)
    kotlinx.coroutines.GlobalScope.launch {
        val repository = com.example.damandroid.api.ActivityRoomRepository()
        val joinResult = repository.joinActivity(activityId)
        when (joinResult) {
            is com.example.damandroid.api.ActivityRoomRepository.ActivityRoomResult.Success,
            is com.example.damandroid.api.ActivityRoomRepository.ActivityRoomResult.Error -> {
                // Proceed even if already participant
                when (val chatResult = repository.createOrGetActivityGroupChat(activityId)) {
                    is com.example.damandroid.api.ActivityRoomRepository.ActivityRoomResult.Success -> {
                        val chat = chatResult.data.chat
                        onChatClick?.invoke(chat.id, chat.groupName, chat.groupAvatar, chat.isGroup)
                    }
                    else -> { /* ignore errors here for brevity */ }
                }
            }
        }
    }
}

@Composable
private fun ActiveUsersSection(users: List<DiscoverUser>, colors: AppThemeColors) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        SectionTitle("Active Now", colors)
        if (users.isEmpty()) {
            EmptySectionMessage("No active users match your search", colors)
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                users.forEach { user ->
                    ActiveUserCard(user = user, colors = colors)
                }
            }
        }
    }
}

@Composable
private fun ActiveUserCard(user: DiscoverUser, colors: AppThemeColors) {
    Box {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset(x = 2.dp, y = 2.dp)
                .height(90.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(
                    Brush.linearGradient(
                        listOf(
                            colors.accentTeal.copy(alpha = 0.16f),
                            colors.accentGreen.copy(alpha = 0.12f)
                        )
                    )
                )
                .blur(16.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = colors.glassSurface),
            border = BorderStroke(2.dp, colors.glassBorder),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(14.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box {
                    AsyncImage(
                        model = user.avatarUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .border(2.dp, colors.glassBorder, CircleShape)
                    )
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .offset(x = 32.dp, y = 32.dp)
                            .clip(CircleShape)
                            .background(colors.success)
                            .border(2.dp, colors.glassSurface, CircleShape)
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = user.name,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = colors.primaryText
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = user.sport,
                            fontSize = 12.sp,
                            color = colors.mutedText
                        )
                        Text(
                            text = "•",
                            fontSize = 12.sp,
                            color = colors.mutedText
                        )
                        Text(
                            text = "${user.distance} away",
                            fontSize = 12.sp,
                            color = colors.mutedText
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = { /* Follow */ },
                        modifier = Modifier.height(30.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = colors.primaryText
                        ),
                        border = BorderStroke(2.dp, colors.glassBorder),
                        contentPadding = PaddingValues(horizontal = 14.dp)
                    ) {
                        Text(text = "Follow", fontSize = 12.sp, color = colors.primaryText)
                    }
                    Button(
                        onClick = { /* Chat */ },
                        modifier = Modifier.height(30.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = colors.success),
                        contentPadding = PaddingValues(horizontal = 14.dp)
    ) {
        Text(
                            text = "Chat",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = colors.iconOnAccent
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionTitle(title: String, colors: AppThemeColors) {
    Text(
        text = title,
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        color = colors.primaryText
    )
}

@Composable
private fun EmptySectionMessage(message: String, colors: AppThemeColors) {
    Text(
        text = message,
        fontSize = 13.sp,
        color = colors.mutedText
    )
}

@Composable
private fun FloatingDiscoveryOrbs(colors: AppThemeColors) {
    val purple = colors.accentPurple
    val pink = colors.accentPink
    val blue = colors.accentBlue
    val teal = colors.accentTeal
    val glassSurface = colors.glassSurface

    Box(modifier = Modifier.fillMaxSize()) {
        val transition1 = rememberInfiniteTransition(label = "orb1")
        val pulse1 by transition1.animateFloat(
            initialValue = 0.35f,
            targetValue = 0.18f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 3200, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "pulse1"
        )

        Box(
            modifier = Modifier
                .offset(x = 40.dp, y = 80.dp)
                .size(132.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        listOf(
                            purple.copy(alpha = pulse1),
                            glassSurface.copy(alpha = pulse1 * 0.6f),
                            Color.Transparent
                        )
                    )
                )
                .blur(52.dp)
        )

        val transition2 = rememberInfiniteTransition(label = "orb2")
        val pulse2 by transition2.animateFloat(
            initialValue = 0.3f,
            targetValue = 0.15f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 3200, delayMillis = 800, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "pulse2"
        )

        Box(
            modifier = Modifier
                .offset(x = (-50).dp, y = (-40).dp)
                .size(168.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        listOf(
                            pink.copy(alpha = pulse2),
                            glassSurface.copy(alpha = pulse2 * 0.55f),
                            Color.Transparent
                        )
                    )
                )
                .blur(52.dp)
                .align(Alignment.BottomEnd)
        )

        val transition3 = rememberInfiniteTransition(label = "orb3")
        val pulse3 by transition3.animateFloat(
            initialValue = 0.28f,
            targetValue = 0.14f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 3200, delayMillis = 1600, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "pulse3"
        )

        Box(
            modifier = Modifier
                .size(104.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        listOf(
                            blue.copy(alpha = pulse3),
                            glassSurface.copy(alpha = pulse3 * 0.6f),
                            Color.Transparent
                        )
                    )
                )
                .blur(36.dp)
                .align(Alignment.Center)
        )

        val transition4 = rememberInfiniteTransition(label = "orb4")
        val pulse4 by transition4.animateFloat(
            initialValue = 0.24f,
            targetValue = 0.12f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 3200, delayMillis = 2200, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "pulse4"
        )

        Box(
            modifier = Modifier
                .offset(x = (-12).dp, y = (-12).dp)
                .size(86.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        listOf(
                            teal.copy(alpha = pulse4),
                            glassSurface.copy(alpha = pulse4 * 0.55f),
                            Color.Transparent
                        )
                    )
                )
                .blur(32.dp)
                .align(Alignment.TopEnd)
        )
    }
}

private fun parseHexOrDefault(hex: String, fallback: Color): Color {
    return runCatching { Color(parseColor(hex)) }.getOrElse { fallback }
}

