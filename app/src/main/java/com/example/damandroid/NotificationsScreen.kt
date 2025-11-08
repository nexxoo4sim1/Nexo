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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.ui.zIndex
import com.example.damandroid.ui.theme.AppThemeColors
import com.example.damandroid.ui.theme.DamAndroidTheme
import com.example.damandroid.ui.theme.LocalThemeController
import com.example.damandroid.ui.theme.ThemeController
import com.example.damandroid.ui.theme.rememberAppThemeColors

data class Notification(
    val id: String,
    val icon: String,
    val message: String,
    val time: String,
    val actionText: String? = null
)

val mockNotifications = listOf(
    Notification(
        id = "1",
        icon = "✨",
        message = "AI found 3 new matches near you that fit your schedule",
        time = "2m ago",
        actionText = "View"
    ),
    Notification(
        id = "2",
        icon = "🏆",
        message = "Your coach session \"HIIT Bootcamp\" is starting soon",
        time = "15m ago",
        actionText = "Join"
    ),
    Notification(
        id = "3",
        icon = "☀️",
        message = "Perfect weather for outdoor training today ☀️",
        time = "30m ago",
        actionText = null
    ),
    Notification(
        id = "4",
        icon = "⚽",
        message = "Mike Johnson joined your football match",
        time = "1h ago",
        actionText = "View"
    ),
    Notification(
        id = "5",
        icon = "🏊",
        message = "Your swimming session starts in 2 hours",
        time = "2h ago",
        actionText = "Join"
    ),
    Notification(
        id = "6",
        icon = "💪",
        message = "AI Coach: You're on a 7-day streak! Keep it up! 🔥",
        time = "3h ago",
        actionText = null
    ),
    Notification(
        id = "7",
        icon = "✓",
        message = "New verified coach session",
        time = "3h ago",
        actionText = "View"
    )
)

@Composable
fun NotificationsScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var notifications by remember { mutableStateOf(mockNotifications) }
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
            // Header - Light purple background
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = theme.glassSurface,
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .windowInsetsPadding(WindowInsets.statusBars)
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = theme.primaryText,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                    Text(
                        text = "Notifications",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = theme.primaryText,
                        letterSpacing = (-0.5).sp
                    )
                }
            }

            // Notifications List
            if (notifications.isEmpty()) {
                // Empty State
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(32.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(96.dp)
                                .clip(CircleShape)
                                .background(theme.glassSurface),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "🔔",
                                fontSize = 48.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = "No notifications",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = theme.primaryText
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "We'll notify you when something happens",
                            fontSize = 14.sp,
                            color = theme.mutedText,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(
                        start = 20.dp,
                        top = 20.dp,
                        end = 20.dp,
                        bottom = 180.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(
                        items = notifications,
                        key = { it.id }
                    ) { notification ->
                        NotificationCard(
                            notification = notification,
                            onDismiss = {
                                notifications = notifications.filterNot { it.id == notification.id }
                            },
                            onAction = {
                                notifications = notifications.filterNot { it.id == notification.id }
                            }
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun NotificationCard(
    notification: Notification,
    onAction: () -> Unit,
    onDismiss: () -> Unit
) {
    val theme = rememberAppThemeColors()

    Box {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp)
                .offset(x = 4.dp, y = 4.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            theme.accentPurple.copy(alpha = 0.15f),
                            theme.accentPink.copy(alpha = 0.15f)
                        )
                    )
                )
                .blur(16.dp)
                .zIndex(0f)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp)
                .clip(RoundedCornerShape(24.dp))
                .zIndex(1f),
            colors = CardDefaults.cardColors(
                containerColor = theme.glassSurface
            ),
            border = BorderStroke(2.dp, theme.glassBorder)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            modifier = Modifier.weight(1f),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(theme.glassSurface)
                                    .border(2.dp, theme.glassBorder, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = notification.icon,
                                    fontSize = 22.sp
                                )
                            }

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = notification.message,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = theme.primaryText,
                                    maxLines = 2
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = notification.time,
                                    fontSize = 12.sp,
                                    color = theme.mutedText
                                )
                            }
                        }

                        IconButton(onClick = onDismiss) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Dismiss",
                                tint = theme.mutedText
                            )
                        }
                    }

                    if (notification.actionText != null) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Button(
                                onClick = onAction,
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = theme.accentPurple
                                ),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = notification.actionText,
                                    fontSize = 12.sp,
                                    color = theme.iconOnAccent
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun NotificationsScreenPreview() {
    CompositionLocalProvider(
        LocalThemeController provides ThemeController(
            isDarkMode = false,
            setDarkMode = {}
        )
    ) {
        DamAndroidTheme(darkTheme = false) {
            NotificationsScreen(onBack = { })
        }
    }
}

