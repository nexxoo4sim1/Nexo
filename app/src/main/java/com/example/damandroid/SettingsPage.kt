package com.example.damandroid

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.damandroid.ui.theme.AppThemeColors
import com.example.damandroid.ui.theme.DamAndroidTheme
import com.example.damandroid.ui.theme.LocalThemeController
import com.example.damandroid.ui.theme.ThemeController
import com.example.damandroid.ui.theme.rememberAppThemeColors

enum class VerificationStatus {
    NONE, PENDING, APPROVED
}

data class SettingsItem(
    val icon: ImageVector,
    val label: String,
    val action: SettingsAction,
    val value: Boolean = false,
    val extra: String? = null
)

enum class SettingsAction {
    NAVIGATE, TOGGLE
}

@Composable
fun SettingsPage(
    onBack: () -> Unit,
    onApplyVerification: (() -> Unit)? = null,
    onLogout: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var verificationStatus by remember { mutableStateOf(VerificationStatus.NONE) }
    val themeController = LocalThemeController.current
    val theme = rememberAppThemeColors(themeController.isDarkMode)

    val settingsSections = listOf(
        SettingsSection(
            title = "Account",
            items = listOf(
                SettingsItem(Icons.Default.Person, "Edit Profile", SettingsAction.NAVIGATE),
                SettingsItem(Icons.Default.Lock, "Change Password", SettingsAction.NAVIGATE)
            )
        ),
        SettingsSection(
            title = "AI Preferences",
            items = listOf(
                SettingsItem(Icons.Default.AutoAwesome, "AI Suggestions", SettingsAction.TOGGLE, true),
                SettingsItem(Icons.Default.AutoAwesome, "Motivation Tips", SettingsAction.TOGGLE, true),
                SettingsItem(Icons.Default.AutoAwesome, "Coach Recommendations", SettingsAction.TOGGLE, true),
                SettingsItem(Icons.Default.AutoAwesome, "Smart Notifications", SettingsAction.TOGGLE, false)
            )
        ),
        SettingsSection(
            title = "Privacy",
            items = listOf(
                SettingsItem(Icons.Default.VerifiedUser, "Public Profile", SettingsAction.TOGGLE, true),
                SettingsItem(Icons.Default.VerifiedUser, "Show Location", SettingsAction.TOGGLE, true),
                SettingsItem(Icons.Default.Person, "Blocked Users", SettingsAction.NAVIGATE)
            )
        ),
        SettingsSection(
            title = "Notifications",
            items = listOf(
                SettingsItem(Icons.Default.Notifications, "Push Notifications", SettingsAction.TOGGLE, true),
                SettingsItem(Icons.Default.Notifications, "Email Notifications", SettingsAction.TOGGLE, false),
                SettingsItem(Icons.Default.Notifications, "Sound", SettingsAction.TOGGLE, true)
            )
        ),
        SettingsSection(
            title = "App Info",
            items = listOf(
                SettingsItem(Icons.Default.Info, "Terms of Service", SettingsAction.NAVIGATE),
                SettingsItem(Icons.Default.Info, "Privacy Policy", SettingsAction.NAVIGATE),
                SettingsItem(Icons.Default.Info, "Contact Support", SettingsAction.NAVIGATE),
                SettingsItem(Icons.Default.Info, "About", SettingsAction.NAVIGATE, extra = "v1.0.0")
            )
        )
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(theme.backgroundGradient)
    ) {
        FloatingSettingsOrbs(theme)

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = theme.cardSurface,
                    border = BorderStroke(2.dp, theme.cardBorder),
                    shadowElevation = 8.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
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
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Text(
                            text = "Dashboard",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = theme.primaryText
                        )
                    }
                }
            }

            // Verification Status Card
            if (onApplyVerification != null) {
                item {
                    VerificationStatusCard(
                        status = verificationStatus,
                        onApplyClick = onApplyVerification,
                        theme = theme,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }

            // Settings Sections
            settingsSections.forEach { section ->
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = section.title,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = theme.primaryText,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                        SettingsSectionCard(
                            items = section.items,
                            theme = theme,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            // Logout Button
            if (onLogout != null) {
                item {
                    Button(
                        onClick = onLogout,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 16.dp)
                            .height(44.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = theme.danger
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = null,
                            tint = theme.iconOnAccent,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Log Out",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = theme.iconOnAccent
                        )
                    }
                }
            }
        }
    }
}

data class SettingsSection(
    val title: String,
    val items: List<SettingsItem>
)

@Composable
private fun FloatingSettingsOrbs(theme: AppThemeColors) {
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
                            theme.accentPurple.copy(alpha = pulse1 * 0.4f),
                            theme.accentPink.copy(alpha = pulse1 * 0.4f),
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
                            theme.accentBlue.copy(alpha = pulse2 * 0.3f),
                            theme.accentPurple.copy(alpha = pulse2 * 0.3f),
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
                            theme.accentPink.copy(alpha = pulse3 * 0.3f),
                            theme.accentPurple.copy(alpha = pulse3 * 0.3f),
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
                            theme.accentTeal.copy(alpha = pulse4 * 0.25f),
                            theme.accentBlue.copy(alpha = pulse4 * 0.25f),
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
private fun VerificationStatusCard(
    status: VerificationStatus,
    onApplyClick: () -> Unit,
    theme: AppThemeColors,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = theme.cardSurface,
        border = BorderStroke(2.dp, theme.cardBorder),
        shadowElevation = 8.dp
    ) {
        when (status) {
            VerificationStatus.NONE -> {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Surface(
                        modifier = Modifier.size(44.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = theme.accentBlue.copy(alpha = 0.2f),
                        border = BorderStroke(2.dp, theme.cardBorder)
                    ) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = null,
                            tint = theme.accentBlue,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(10.dp)
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Become a Verified Coach",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = theme.primaryText
                        )
                        Text(
                            text = "Get verified to host paid sessions and build trust",
                            fontSize = 12.sp,
                            color = theme.secondaryText,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = onApplyClick,
                            modifier = Modifier.height(32.dp),
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = theme.accentBlue
                            )
                        ) {
                            Text(
                                text = "Apply Now",
                                fontSize = 13.sp,
                                color = theme.iconOnAccent
                            )
                        }
                    }
                }
            }
            VerificationStatus.PENDING -> {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Surface(
                        modifier = Modifier.size(44.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = theme.accentGold.copy(alpha = 0.2f),
                        border = BorderStroke(2.dp, theme.cardBorder)
                    ) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = null,
                            tint = theme.accentOrange,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(10.dp)
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Verification Pending",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = theme.primaryText
                            )
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = theme.accentGold.copy(alpha = 0.3f)
                            ) {
                                Text(
                                    text = "Under Review",
                                    fontSize = 11.sp,
                                    color = theme.accentOrange,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                        Text(
                            text = "We're reviewing your application. This typically takes 2-3 business days.",
                            fontSize = 12.sp,
                            color = theme.secondaryText,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
            VerificationStatus.APPROVED -> {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Surface(
                        modifier = Modifier.size(44.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = theme.accentGreen.copy(alpha = 0.2f),
                        border = BorderStroke(2.dp, theme.cardBorder)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = theme.accentGreen,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(10.dp)
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Verified Coach",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = theme.primaryText
                            )
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = theme.accentGreen
                            ) {
                                Text(
                                    text = "✓ Verified",
                                    fontSize = 11.sp,
                                    color = theme.iconOnAccent,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                        Text(
                            text = "You can now create paid sessions and access coach features",
                            fontSize = 12.sp,
                            color = theme.secondaryText,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingsSectionCard(
    items: List<SettingsItem>,
    theme: AppThemeColors,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = theme.cardSurface,
        border = BorderStroke(2.dp, theme.cardBorder),
        shadowElevation = 8.dp
    ) {
        Column {
            items.forEachIndexed { index, item ->
                if (index > 0) {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        color = theme.cardBorder.copy(alpha = 0.5f),
                        thickness = 2.dp
                    )
                }
                SettingsItemRow(
                    item = item,
                    theme = theme,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun SettingsItemRow(
    item: SettingsItem,
    theme: AppThemeColors,
    modifier: Modifier = Modifier
) {
    var isToggled by remember { mutableStateOf(item.value) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(36.dp),
                shape = RoundedCornerShape(12.dp),
                color = theme.accentPurple.copy(alpha = 0.2f),
                border = BorderStroke(2.dp, theme.cardBorder)
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = null,
                    tint = theme.accentPurple,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                )
            }
            Text(
                text = item.label,
                fontSize = 14.sp,
                color = theme.primaryText
            )
        }

        when (item.action) {
            SettingsAction.TOGGLE -> {
                Switch(
                    checked = isToggled,
                    onCheckedChange = { isToggled = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = theme.accentPurple,
                        checkedTrackColor = theme.accentPurple.copy(alpha = 0.5f)
                    )
                )
            }
            SettingsAction.NAVIGATE -> {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (item.extra != null) {
                        Text(
                            text = item.extra,
                            fontSize = 12.sp,
                            color = theme.mutedText
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = theme.mutedText,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SettingsPagePreview() {
    val controller = ThemeController(isDarkMode = false, setDarkMode = {})
    CompositionLocalProvider(LocalThemeController provides controller) {
        DamAndroidTheme(darkTheme = controller.isDarkMode) {
            SettingsPage(
                onBack = { },
                onApplyVerification = { },
                onLogout = { }
            )
        }
    }
}

