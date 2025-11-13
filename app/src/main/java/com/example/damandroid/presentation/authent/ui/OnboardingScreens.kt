package com.example.damandroid

import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.example.damandroid.ui.theme.AuthScreenPalette
import com.example.damandroid.ui.theme.LocalThemeController
import com.example.damandroid.ui.theme.rememberAppThemeColors
import com.example.damandroid.ui.theme.rememberAuthScreenPalette

data class OnboardingStep(
    val title: String,
    val subtitle: String,
    val imageUrl: String,
    val icon: ImageVector
)

@Composable
fun OnboardingScreens(
    onComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentStep by remember { mutableStateOf(0) }
    val themeController = LocalThemeController.current
    val appTheme = rememberAppThemeColors(themeController.isDarkMode)
    val palette = rememberAuthScreenPalette(appTheme)
    val orbColors = palette.orbColors

    val steps = listOf(
        OnboardingStep(
            title = "Welcome to NEXO",
            subtitle = "Connect with people who love sports as much as you do",
            imageUrl = "https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?w=1080&q=80",
            icon = Icons.Default.FitnessCenter
        ),
        OnboardingStep(
            title = "Find Your Sport Partners",
            subtitle = "Discover nearby activities and join sessions with like-minded people",
            imageUrl = "https://images.unsplash.com/photo-1534438327276-14e5300c3a48?w=1080&q=80",
            icon = Icons.Default.Group
        ),
        OnboardingStep(
            title = "Stay Active Together",
            subtitle = "Create your own activities or join existing ones near you",
            imageUrl = "https://images.unsplash.com/photo-1576678927484-cc907957088c?w=1080&q=80",
            icon = Icons.Default.LocationOn
        )
    )

    val currentStepData = steps[currentStep]

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(palette.background)
    ) {
        // Floating Orbs for Depth - Neon Electric Accents
        OnboardingOrb(
            size = 128.dp,
            color = orbColors.getOrElse(0) { Color(0xFF8B5CF6).copy(alpha = 0.30f) },
            top = 80.dp,
            start = 40.dp,
            pulseDurationMs = 1600
        )
        OnboardingOrb(
            size = 160.dp,
            color = orbColors.getOrElse(1) { Color(0xFFEC4899).copy(alpha = 0.35f) },
            bottom = 160.dp,
            end = 40.dp,
            pulseDurationMs = 1600,
            startDelayMs = 1000
        )
        OnboardingOrb(
            size = 96.dp,
            color = orbColors.getOrElse(2) { Color(0xFF0066FF).copy(alpha = 0.30f) },
            center = true,
            pulseDurationMs = 1600,
            startDelayMs = 2000
        )
        OnboardingOrb(
            size = 80.dp,
            color = orbColors.getOrElse(3) { Color(0xFF2ECC71).copy(alpha = 0.25f) },
            top = 120.dp,
            end = 80.dp,
            pulseDurationMs = 1600,
            startDelayMs = 3000
        )

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Skip button
            if (currentStep < steps.size - 1) {
                Box(
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(24.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(24.dp))
                            .background(palette.dividerLabelBackground)
                            .border(2.dp, palette.glassBorder, RoundedCornerShape(24.dp))
                            .clickable { onComplete() }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "Skip",
                            color = palette.linkText,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Image Section
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                // Background image
                SubcomposeAsyncImage(
                    model = currentStepData.imageUrl,
                    contentDescription = currentStepData.title,
                    modifier = Modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    loading = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            orbColors.getOrElse(3) { Color(0xFF2ECC71) }.copy(alpha = 0.3f),
                                            orbColors.getOrElse(2) { Color(0xFF3498DB) }.copy(alpha = 0.2f)
                                        )
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = currentStepData.icon,
                                contentDescription = null,
                                modifier = Modifier.size(80.dp),
                                tint = palette.buttonText.copy(alpha = 0.6f)
                            )
                        }
                    },
                    error = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            orbColors.getOrElse(3) { Color(0xFF2ECC71) }.copy(alpha = 0.35f),
                                            orbColors.getOrElse(2) { Color(0xFF3498DB) }.copy(alpha = 0.25f)
                                        )
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = currentStepData.icon,
                                contentDescription = null,
                                modifier = Modifier.size(80.dp),
                                tint = palette.buttonText.copy(alpha = 0.7f)
                            )
                        }
                    },
                    success = {
                        SubcomposeAsyncImageContent()
                    }
                )

                // Gradient overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Transparent,
                                    (if (appTheme.isDark) Color(0xFF0F172A) else Color.White).copy(alpha = 0.85f)
                                )
                            )
                        )
                )

                // Centered icon with crystal glass effect
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(bottom = 40.dp)
                ) {
                    // Outer shimmer effect
                    Box(
                        modifier = Modifier
                            .size(144.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    colors = orbColors.map { it.copy(alpha = 0.2f) }
                                )
                            )
                            .blur(32.dp)
                            .scale(1.1f)
                    )

                    // Crystal glass container
                    Box(
                        modifier = Modifier
                            .size(128.dp)
                            .clip(CircleShape)
                            .background(palette.glassSurface)
                            .border(2.dp, palette.glassBorder, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        // Top highlight
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .clip(RoundedCornerShape(topStart = 64.dp, topEnd = 64.dp))
                            .background(palette.highlightGradient)
                                .offset(y = (-64).dp)
                        )

                        Icon(
                            imageVector = currentStepData.icon,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                        tint = palette.linkText
                        )
                    }
                }
            }

            // Content Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .padding(bottom = 48.dp, top = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Title and subtitle
                Text(
                    text = currentStepData.title,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = palette.primaryText,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Text(
                    text = currentStepData.subtitle,
                    fontSize = 16.sp,
                    color = palette.mutedText,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                // Progress indicators - Crystal Glass Pills
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 32.dp)
                ) {
                    steps.forEachIndexed { index, _ ->
                        val isActive = index == currentStep
                        val width by animateFloatAsState(
                            targetValue = if (isActive) 32f else 8f,
                            animationSpec = tween(durationMillis = 300),
                            label = "progress-indicator"
                        )

                        Box(
                            modifier = Modifier
                                .height(8.dp)
                                .width(width.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(
                                    if (isActive) {
                                        Brush.horizontalGradient(
                                            colors = listOf(
                                                appTheme.accentPurple,
                                                appTheme.accentPink
                                            )
                                        )
                                    } else {
                                        Brush.linearGradient(
                                            colors = listOf(
                                                palette.dividerLabelBackground,
                                                palette.dividerLabelBackground.copy(alpha = 0.7f)
                                            )
                                        )
                                    }
                                )
                                .then(
                                    if (!isActive) {
                                        Modifier.border(
                                            1.dp,
                                            palette.glassBorder,
                                            RoundedCornerShape(4.dp)
                                        )
                                    } else {
                                        Modifier
                                    }
                                )
                        )
                    }
                }

                // Action button
                Box {
                    // Outer glow
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .offset(x = 4.dp, y = 4.dp)
                            .clip(RoundedCornerShape(28.dp))
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(
                                        appTheme.accentPurple.copy(alpha = 0.35f),
                                        appTheme.accentBlue.copy(alpha = 0.3f)
                                    )
                                )
                            )
                            .blur(16.dp)
                    )

                    Button(
                        onClick = {
                            if (currentStep < steps.size - 1) {
                                currentStep++
                            } else {
                                onComplete()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(28.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    palette.buttonGradient,
                                    RoundedCornerShape(28.dp)
                                )
                                .border(2.dp, palette.buttonBorder, RoundedCornerShape(28.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (currentStep < steps.size - 1) "Next" else "Continue",
                                    color = palette.buttonText,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                if (currentStep < steps.size - 1) {
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Icon(
                                        imageVector = Icons.Default.ChevronRight,
                                        contentDescription = null,
                                        modifier = Modifier.size(20.dp),
                                        tint = palette.buttonText
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
private fun OnboardingOrb(
    size: Dp,
    color: Color,
    top: Dp? = null,
    start: Dp? = null,
    end: Dp? = null,
    bottom: Dp? = null,
    center: Boolean = false,
    pulseDurationMs: Int,
    startDelayMs: Int = 0,
) {
    val infinite = rememberInfiniteTransition(label = "onboarding-orb")
    val scale by infinite.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = InfiniteRepeatableSpec(
            animation = tween(durationMillis = pulseDurationMs, easing = LinearEasing, delayMillis = startDelayMs),
            repeatMode = RepeatMode.Reverse
        ),
        label = "orb-scale"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        var mod = Modifier
            .size(size * scale)
            .blur(48.dp)
            .background(color = color, shape = CircleShape)

        mod = if (center) {
            mod.align(Alignment.Center)
        } else {
            val alignment = when {
                bottom != null && end != null -> Alignment.BottomEnd
                bottom != null -> Alignment.BottomStart
                end != null -> Alignment.TopEnd
                else -> Alignment.TopStart
            }
            mod.align(alignment)
        }

        if (top != null) mod = mod.offset(y = top)
        if (start != null) mod = mod.offset(x = start)
        if (end != null) mod = mod.offset(x = (-end))
        if (bottom != null) mod = mod.offset(y = (-bottom))

        Box(modifier = mod) {}
    }
}
