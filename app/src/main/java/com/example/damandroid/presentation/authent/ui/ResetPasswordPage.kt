package com.example.damandroid

import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.damandroid.api.AuthRepository
import com.example.damandroid.ui.theme.AuthScreenPalette
import com.example.damandroid.ui.theme.LocalThemeController
import com.example.damandroid.ui.theme.rememberAppThemeColors
import com.example.damandroid.ui.theme.rememberAuthScreenPalette
import kotlinx.coroutines.launch

@Composable
fun ResetPasswordPage(
    onBackToLogin: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var isEmailSent by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    
    val context = LocalContext.current
    val authRepository = remember(context) { AuthRepository(context.applicationContext) }
    val coroutineScope = rememberCoroutineScope()
    val themeController = LocalThemeController.current
    val appTheme = rememberAppThemeColors(themeController.isDarkMode)
    val palette = rememberAuthScreenPalette(appTheme)
    val orbColors = palette.orbColors

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(palette.background)
    ) {
        // Floating Orbs for Depth - Neon Electric Accents
        ResetPasswordOrb(
            size = 128.dp,
            color = orbColors.getOrElse(0) { Color(0xFF8B5CF6).copy(alpha = 0.30f) },
            top = 80.dp,
            start = 40.dp,
            pulseDurationMs = 1600
        )
        ResetPasswordOrb(
            size = 160.dp,
            color = orbColors.getOrElse(1) { Color(0xFFEC4899).copy(alpha = 0.35f) },
            bottom = 160.dp,
            end = 40.dp,
            pulseDurationMs = 1600,
            startDelayMs = 1000
        )
        ResetPasswordOrb(
            size = 96.dp,
            color = orbColors.getOrElse(2) { Color(0xFF0066FF).copy(alpha = 0.30f) },
            center = true,
            pulseDurationMs = 1600,
            startDelayMs = 2000
        )
        ResetPasswordOrb(
            size = 80.dp,
            color = orbColors.getOrElse(3) { Color(0xFF2ECC71).copy(alpha = 0.25f) },
            top = 120.dp,
            end = 80.dp,
            pulseDurationMs = 1600,
            startDelayMs = 3000
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(top = 16.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (!isEmailSent) {
                // Reset Password Form
                Spacer(modifier = Modifier.height(40.dp))

                // Logo Section
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(bottom = 40.dp)
                ) {
                    Box {
                        // Shimmer effect
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(24.dp))
                                .background(
                                    Brush.linearGradient(
                                        colors = orbColors.map { it.copy(alpha = 0.2f) }
                                    )
                                )
                                .blur(24.dp)
                                .scale(1.1f)
                        )

                        // Crystal Glass Container
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(24.dp))
                                .background(palette.glassSurface)
                                .border(2.dp, palette.glassBorder, RoundedCornerShape(24.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            // Inner glow
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(24.dp))
                                    .background(palette.highlightGradient)
                            )

                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = null,
                                modifier = Modifier.size(40.dp),
                                tint = palette.linkText
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Reset Password",
                        color = palette.primaryText,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Enter your email address and we'll send you a link to reset your password",
                        color = palette.mutedText,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.widthIn(max = 320.dp)
                    )
                }

                // Reset Password Form Card - Crystal Glass Effect
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 384.dp)
                ) {
                    // Outer glow
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .offset(x = 4.dp, y = 4.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(
                                Brush.linearGradient(
                                    colors = orbColors.map { it.copy(alpha = 0.3f) }
                                )
                            )
                            .blur(24.dp)
                    )

                    // Main card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = palette.cardSurface
                        ),
                        border = BorderStroke(2.dp, palette.glassBorder)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp)
                        ) {
                            // Top highlight
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(80.dp)
                                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                                    .background(palette.highlightGradient)
                                    .offset(y = (-24).dp)
                            )

                            // Email Input
                            Column(
                                modifier = Modifier.padding(bottom = 24.dp)
                            ) {
                                Text(
                                    text = "Email Address",
                                    color = palette.secondaryText,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                                OutlinedTextField(
                                    value = email,
                                    onValueChange = { email = it },
                                    placeholder = {
                                        Text(
                                            "email@exemple.com",
                                            color = palette.fieldPlaceholder
                                        )
                                    },
                                    leadingIcon = {
                                        Icon(
                                            Icons.Default.Email,
                                            contentDescription = null,
                                            tint = palette.fieldIcon,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    },
                                    singleLine = true,
                                    textStyle = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = palette.fieldText,
                                        unfocusedTextColor = palette.fieldText,
                                        focusedPlaceholderColor = palette.fieldPlaceholder,
                                        unfocusedPlaceholderColor = palette.fieldPlaceholder,
                                        focusedBorderColor = palette.fieldBorderFocused,
                                        unfocusedBorderColor = palette.fieldBorder,
                                        cursorColor = palette.fieldText,
                                        focusedContainerColor = palette.fieldContainer,
                                        unfocusedContainerColor = palette.fieldContainer,
                                    ),
                                    shape = RoundedCornerShape(20.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(60.dp)
                                )
                            }

                            // Error Message
                            if (errorMessage != null) {
                                Text(
                                    text = errorMessage!!,
                                    color = palette.errorText,
                                    fontSize = 12.sp,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 8.dp)
                                )
                            }

                            // Send Reset Link Button
                            Button(
                                onClick = {
                                    if (email.isBlank()) {
                                        errorMessage = "Please enter your email address"
                                        return@Button
                                    }
                                    isLoading = true
                                    errorMessage = null
                                    coroutineScope.launch {
                                        val result = authRepository.forgotPassword(email.trim())
                                        isLoading = false
                                        when (result) {
                                            is AuthRepository.PasswordResetResult.Success -> {
                                                isEmailSent = true
                                            }
                                            is AuthRepository.PasswordResetResult.Error -> {
                                                errorMessage = result.message
                                            }
                                        }
                                    }
                                },
                                enabled = !isLoading,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Transparent
                                )
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(
                                            palette.buttonGradient,
                                            RoundedCornerShape(16.dp)
                                        )
                                        .border(2.dp, palette.buttonBorder, RoundedCornerShape(16.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (isLoading) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(20.dp),
                                            color = palette.spinnerColor,
                                            strokeWidth = 2.dp
                                        )
                                    } else {
                                        Text(
                                            "Send Reset Link",
                                            color = palette.buttonText,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Back to Login Link
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Remember your password? ",
                        color = palette.mutedText,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Back to Sign In",
                        color = palette.linkText,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { onBackToLogin() }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            } else {
                // Success State
                Spacer(modifier = Modifier.height(20.dp))

                // Logo Section - Success
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(bottom = 32.dp)
                ) {
                    Box {
                        // Shimmer effect
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(24.dp))
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(
                                            appTheme.accentGreen.copy(alpha = 0.25f),
                                            Color.White.copy(alpha = if (appTheme.isDark) 0.1f else 0.4f),
                                            appTheme.accentGreen.copy(alpha = 0.2f)
                                        )
                                    )
                                )
                                .blur(24.dp)
                                .scale(1.1f)
                        )

                        // Crystal Glass Container
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(24.dp))
                                .background(palette.glassSurface)
                                .border(2.dp, palette.glassBorder, RoundedCornerShape(24.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            // Inner glow
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(24.dp))
                                    .background(palette.highlightGradient)
                            )

                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                modifier = Modifier.size(40.dp),
                                tint = Color(0xFF2ECC71)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Check Your Email",
                        color = palette.primaryText,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "We've sent a password reset link to",
                        color = palette.mutedText,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.widthIn(max = 320.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = email,
                        color = palette.primaryText,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Success Card - Crystal Glass Effect
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 384.dp)
                ) {
                    // Outer glow
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .offset(x = 4.dp, y = 4.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(
                                Brush.linearGradient(
                                    colors = orbColors.map { it.copy(alpha = 0.3f) }
                                )
                            )
                            .blur(24.dp)
                    )

                    // Main card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = palette.cardSurface
                        ),
                        border = BorderStroke(2.dp, palette.glassBorder)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp)
                        ) {
                            // Top highlight
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(80.dp)
                                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                                    .background(palette.highlightGradient)
                                    .offset(y = (-24).dp)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Info Box
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(palette.socialGradient)
                                    .border(2.dp, palette.socialBorder, RoundedCornerShape(16.dp))
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = "Click the link in the email to reset your password. If you don't see the email, check your spam folder.",
                                    color = palette.secondaryText,
                                    fontSize = 14.sp,
                                    textAlign = TextAlign.Center
                                )
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            // Back to Sign In Button
                            Button(
                                onClick = {
                                    isEmailSent = false
                                    onBackToLogin()
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Transparent
                                )
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(
                                            palette.buttonGradient,
                                            RoundedCornerShape(16.dp)
                                        )
                                        .border(2.dp, palette.buttonBorder, RoundedCornerShape(16.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        "Back to Sign In",
                                        color = palette.buttonText,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Resend Link
                            Text(
                                text = "Didn't receive the email? Send again",
                                color = palette.linkText,
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        if (email.isNotBlank() && !isLoading) {
                                            isLoading = true
                                            errorMessage = null
                                            coroutineScope.launch {
                                                val result = authRepository.forgotPassword(email.trim())
                                                isLoading = false
                                                when (result) {
                                                    is AuthRepository.PasswordResetResult.Success -> {
                                                        // Email sent again
                                                    }
                                                    is AuthRepository.PasswordResetResult.Error -> {
                                                        errorMessage = result.message
                                                        isEmailSent = false // Show form again on error
                                                    }
                                                }
                                            }
                                        }
                                    }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun ResetPasswordOrb(
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
    val infinite = rememberInfiniteTransition(label = "reset-password-orb")
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

