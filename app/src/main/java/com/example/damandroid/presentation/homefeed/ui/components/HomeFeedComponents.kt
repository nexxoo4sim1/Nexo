package com.example.damandroid.presentation.homefeed.ui.components

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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.example.damandroid.domain.model.HomeActivity
import com.example.damandroid.presentation.homefeed.model.HomeFeedUiState
import com.example.damandroid.ui.theme.AppThemeColors
import com.example.damandroid.ui.theme.LocalThemeController
import com.example.damandroid.ui.theme.rememberAppThemeColors


@Composable
fun HomeFeedContent(
    state: HomeFeedUiState,
    onSearchQueryChange: (String) -> Unit,
    onFilterSportChange: (String) -> Unit,
    onFilterDistanceChange: (Float) -> Unit,
    onFilterSheetToggle: (Boolean) -> Unit,
    onToggleSaved: (String) -> Unit,
    onActivityClick: (HomeActivity) -> Unit,
    onSearchClick: (() -> Unit)?,
    onAISuggestionsClick: (() -> Unit)?,
    onQuickMatchClick: (() -> Unit)?,
    onAIMatchmakerClick: (() -> Unit)?,
    onEventDetailsClick: ((String) -> Unit)?,
    onCreateClick: (() -> Unit)?,
    onNotificationsClick: (() -> Unit)?,
    onActivityTypeFilterChange: ((com.example.damandroid.presentation.homefeed.model.ActivityTypeFilter) -> Unit)? = null,
    onChatNowClick: ((HomeActivity) -> Unit)? = null,
    modifier: Modifier = Modifier,
    snackbarHost: @Composable () -> Unit
) {
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

    val usingSampleData = state.filteredActivities.isEmpty()
    var sampleSavedActivities by remember { mutableStateOf(setOf<String>()) }
    val baseActivities = if (usingSampleData) sampleHomeActivities else state.filteredActivities
    
    // Filtrer les activités selon le type sélectionné
    // Note: Pour "Mine", les activités sont déjà filtrées côté backend via /activities/my-activities
    // Donc on ne filtre pas à nouveau, on affiche directement toutes les activités retournées
    val filteredByType = if (state.activityTypeFilter == com.example.damandroid.presentation.homefeed.model.ActivityTypeFilter.MINE) {
        // Pour "Mine", les activités viennent déjà de /activities/my-activities, donc pas besoin de filtrer
        baseActivities
    } else {
        getFilteredActivitiesByType(
            activities = baseActivities,
            filter = state.activityTypeFilter,
            usingSampleData = usingSampleData
        )
    }
    
    val displayedActivities = if (usingSampleData) {
        filteredByType.map { activity ->
            if (sampleSavedActivities.contains(activity.id)) {
                activity.copy(isSaved = true)
            } else {
                activity.copy(isSaved = false)
            }
        }
    } else {
        filteredByType
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundBrush)
    ) {
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
                    }
                }
            }

            item {
                Column(
                    modifier = Modifier.padding(bottom = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = state.searchQuery,
                            onValueChange = onSearchQueryChange,
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
                            onClick = { onFilterSheetToggle(true) },
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

            // Filtres d'activité (Mine, Coach, Individual)
            item {
                ActivityTypeFilterRow(
                    selectedFilter = state.activityTypeFilter,
                    onFilterChange = onActivityTypeFilterChange ?: {},
                    appColors = appColors,
                    cardSurface = cardSurface,
                    cardBorder = cardBorder,
                    titleColor = titleText,
                    subtitleColor = subtitleText,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }

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

            items(
                items = displayedActivities,
                key = { it.id }
            ) { activity ->
                ActivityCard(
                    activity = activity,
                    activityType = state.activityTypeFilter,
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
                    savedInactive = savedInactive,
                    onSaveToggle = {
                        if (usingSampleData) {
                            sampleSavedActivities = if (sampleSavedActivities.contains(activity.id)) {
                                sampleSavedActivities - activity.id
                            } else {
                                sampleSavedActivities + activity.id
                            }
                        } else {
                            onToggleSaved(activity.id)
                        }
                    },
                    onActivityClick = { onActivityClick(activity) },
                    onDetailsClick = { onEventDetailsClick?.invoke(activity.id) },
                    onChatNowClick = onChatNowClick?.let { { it(activity) } }
                )
            }
        }

        if (state.showFilterSheet) {
            FilterSheet(
                filterSport = state.filterSport,
                filterDistance = state.filterDistance,
                onSportChange = onFilterSportChange,
                onDistanceChange = onFilterDistanceChange,
                onDismiss = { onFilterSheetToggle(false) },
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

        if (onCreateClick != null) {
            FloatingCreateButton(
                onClick = onCreateClick,
                appColors = appColors,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 24.dp, bottom = 96.dp)
                    .zIndex(10f)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 16.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            snackbarHost()
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
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clip(RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp))
                        .background(highlightGradient)
                        .offset(y = (-16).dp)
                )

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
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clip(RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp))
                        .background(highlightGradient)
                        .offset(y = (-16).dp)
                )

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
    activity: HomeActivity,
    activityType: com.example.damandroid.presentation.homefeed.model.ActivityTypeFilter,
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
    savedInactive: Color,
    onSaveToggle: () -> Unit,
    onActivityClick: () -> Unit,
    onDetailsClick: () -> Unit,
    onChatNowClick: (() -> Unit)? = null
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
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 2.dp)
                        ) {
                            Text(
                                text = activity.hostName,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = titleColor
                            )
                            // Badge Coach ou Individual
                            when (activityType) {
                                com.example.damandroid.presentation.homefeed.model.ActivityTypeFilter.COACH -> {
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = appColors.accentPurple,
                                        modifier = Modifier.height(18.dp)
                                    ) {
                                        Text(
                                            text = "Coach",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = appColors.iconOnAccent,
                                            modifier = Modifier
                                                .fillMaxHeight()
                                                .padding(horizontal = 6.dp)
                                                .wrapContentHeight(Alignment.CenterVertically),
                                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                        )
                                    }
                                }
                                com.example.damandroid.presentation.homefeed.model.ActivityTypeFilter.INDIVIDUAL,
                                com.example.damandroid.presentation.homefeed.model.ActivityTypeFilter.MINE -> {
                                    // Pour Individual et Mine, afficher le badge Individual
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = appColors.accentBlue.copy(alpha = 0.2f),
                                        border = BorderStroke(1.dp, appColors.accentBlue.copy(alpha = 0.3f)),
                                        modifier = Modifier.height(18.dp)
                                    ) {
                                        Text(
                                            text = "Individual",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = appColors.accentBlue,
                                            modifier = Modifier
                                                .fillMaxHeight()
                                                .padding(horizontal = 6.dp)
                                                .wrapContentHeight(Alignment.CenterVertically),
                                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                        )
                                    }
                                }
                            }
                        }
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
                        imageVector = if (activity.isSaved) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Save",
                        tint = if (activity.isSaved) accentRed else savedInactive,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = activity.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = titleColor,
                modifier = Modifier.padding(bottom = 10.dp)
            )

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
                    // Pour Coach : toujours afficher "Details" et "Join"
                    // Pour Individual : afficher "Chat Now" ou "Completed"
                    if (activityType == com.example.damandroid.presentation.homefeed.model.ActivityTypeFilter.COACH) {
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
                    } else {
                        // Individual : vérifier si l'activité est complète
                        val isActivityComplete = activity.spotsTaken >= activity.spotsTotal
                        val chatNowCallback = onChatNowClick
                        
                        if (!isActivityComplete && chatNowCallback != null) {
                            // Activité non complète : afficher "Chat Now"
                            Button(
                                onClick = { chatNowCallback() },
                                shape = RoundedCornerShape(24.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = accentGreen
                                ),
                                modifier = Modifier.height(32.dp)
                            ) {
                                Text(
                                    text = "Chat Now",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = appColors.iconOnAccent
                                )
                            }
                        } else if (isActivityComplete) {
                            // Activité complète : afficher "Completed"
                            Surface(
                                shape = RoundedCornerShape(24.dp),
                                color = chipBackground,
                                border = BorderStroke(1.dp, chipBorder),
                                modifier = Modifier.height(32.dp)
                            ) {
                                Text(
                                    text = "Completed",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = secondaryText,
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                                )
                            }
                        }
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
        Box(
            modifier = Modifier
                .offset(x = 0.dp, y = 28.dp)
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

        Box(
            modifier = Modifier
                .size(58.dp)
                .clip(CircleShape)
                .background(glowGradient)
                .blur(16.dp)
                .alpha(0.75f)
                .align(Alignment.Center)
        )

        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(accentGradient)
                .clickable(onClick = onClick)
                .align(Alignment.Center),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.3f),
                                Color.Transparent
                            )
                        )
                    )
                    .clip(CircleShape)
            )

            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Create",
                tint = appColors.iconOnAccent,
                modifier = Modifier
                    .size(28.dp)
                    .zIndex(1f)
            )

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

/**
 * Filtrer les activités selon le type sélectionné
 */
@Composable
private fun getFilteredActivitiesByType(
    activities: List<HomeActivity>,
    filter: com.example.damandroid.presentation.homefeed.model.ActivityTypeFilter,
    usingSampleData: Boolean
): List<HomeActivity> {
    val currentUserId = com.example.damandroid.auth.UserSession.user?.id
    
    return when (filter) {
        com.example.damandroid.presentation.homefeed.model.ActivityTypeFilter.MINE -> {
            // Filtrer les activités créées par l'utilisateur connecté
            if (currentUserId != null && currentUserId.isNotBlank()) {
                // Filtrer en comparant les IDs (comparaison exacte)
                activities.filter { activity ->
                    activity.hostId != null && activity.hostId == currentUserId
                }
            } else {
                emptyList() // Pas d'utilisateur connecté, pas d'activités "Mine"
            }
        }
        com.example.damandroid.presentation.homefeed.model.ActivityTypeFilter.COACH -> {
            // Données statiques pour Coach (toujours statiques, pas depuis le backend)
            sampleCoachActivities
        }
        com.example.damandroid.presentation.homefeed.model.ActivityTypeFilter.INDIVIDUAL -> {
            // Activités dynamiques depuis le backend (toutes les activités du backend)
            activities
        }
    }
}

/**
 * Composant pour les filtres d'activité (Mine, Coach, Individual)
 */
@Composable
private fun ActivityTypeFilterRow(
    selectedFilter: com.example.damandroid.presentation.homefeed.model.ActivityTypeFilter,
    onFilterChange: (com.example.damandroid.presentation.homefeed.model.ActivityTypeFilter) -> Unit,
    appColors: AppThemeColors,
    cardSurface: Color,
    cardBorder: Color,
    titleColor: Color,
    subtitleColor: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(cardSurface)
            .border(1.dp, cardBorder, RoundedCornerShape(20.dp))
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        com.example.damandroid.presentation.homefeed.model.ActivityTypeFilter.values().forEach { filter ->
            val isSelected = filter == selectedFilter
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { onFilterChange(filter) },
                shape = RoundedCornerShape(16.dp),
                color = if (isSelected) {
                    appColors.accentPurple.copy(alpha = if (appColors.isDark) 0.3f else 0.2f)
                } else {
                    Color.Transparent
                },
                border = BorderStroke(
                    width = if (isSelected) 2.dp else 0.dp,
                    color = if (isSelected) appColors.accentPurple else Color.Transparent
                )
            ) {
                Text(
                    text = when (filter) {
                        com.example.damandroid.presentation.homefeed.model.ActivityTypeFilter.MINE -> "Mine"
                        com.example.damandroid.presentation.homefeed.model.ActivityTypeFilter.COACH -> "Coach"
                        com.example.damandroid.presentation.homefeed.model.ActivityTypeFilter.INDIVIDUAL -> "Individual"
                    },
                    fontSize = 13.sp,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                    color = if (isSelected) appColors.accentPurple else subtitleColor,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }
    }
}

/**
 * Données statiques pour les activités Coach
 */
private val sampleCoachActivities = listOf(
    HomeActivity(
        id = "coach-1",
        title = "Morning lap swimming at City Pool",
        sportType = "Swimming",
        sportIcon = "🏊",
        hostName = "Sarah Mitchell",
        hostAvatar = "https://i.ibb.co/xSxQ1Ljf/profile-jpg.png",
        date = "Nov 2, 2025",
        time = "7:00 AM",
        location = "City Aquatic Center",
        distance = "0.8 mi",
        spotsTotal = 5,
        spotsTaken = 3,
        level = "Intermediate",
        isSaved = false
    ),
    HomeActivity(
        id = "coach-2",
        title = "Professional Basketball Training",
        sportType = "Basketball",
        sportIcon = "🏀",
        hostName = "Coach Johnson",
        hostAvatar = "",
        date = "Nov 5, 2025",
        time = "6:00 PM",
        location = "Sports Complex",
        distance = "1.5 mi",
        spotsTotal = 8,
        spotsTaken = 5,
        level = "Advanced",
        isSaved = false
    ),
    HomeActivity(
        id = "coach-3",
        title = "Yoga & Meditation Session",
        sportType = "Yoga",
        sportIcon = "🧘",
        hostName = "Emma Wilson",
        hostAvatar = "",
        date = "Nov 3, 2025",
        time = "9:00 AM",
        location = "Wellness Center",
        distance = "0.3 mi",
        spotsTotal = 12,
        spotsTaken = 8,
        level = "Beginner",
        isSaved = false
    )
)

private val sampleHomeActivities = listOf(
    HomeActivity(
        id = "sample-1",
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
        level = "Intermediate",
        isSaved = false
    ),
    HomeActivity(
        id = "sample-2",
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
        level = "All Levels",
        isSaved = false
    ),
    HomeActivity(
        id = "sample-3",
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
        level = "Advanced",
        isSaved = false
    )
)

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
    var selectedSport by remember(filterSport) {
        mutableStateOf(if (filterSport.equals("all", ignoreCase = true)) "All Sports" else filterSport)
    }

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
                    onExpandedChange = { expandedSport = it }
                ) {
                    OutlinedTextField(
                        value = selectedSport,
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
                                    selectedSport = sport
                                    onSportChange(if (sport == "All Sports") "all" else sport)
                                    expandedSport = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

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

            Slider(
                value = filterDistance,
                onValueChange = onDistanceChange,
                valueRange = 1f..20f,
                steps = 18,
                colors = SliderDefaults.colors(
                    thumbColor = cardSurface,
                    activeTrackColor = accentPurple,
                    inactiveTrackColor = cardBorder
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

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

