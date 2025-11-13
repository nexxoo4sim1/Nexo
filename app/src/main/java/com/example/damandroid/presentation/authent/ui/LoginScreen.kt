package com.example.damandroid

import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.border
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.semantics.Role
import com.example.damandroid.api.AuthRepository
import com.example.damandroid.auth.RememberMeStore
import com.example.damandroid.ui.theme.AuthScreenPalette
import com.example.damandroid.ui.theme.DamAndroidTheme
import com.example.damandroid.ui.theme.LocalThemeController
import com.example.damandroid.ui.theme.ThemeController
import com.example.damandroid.ui.theme.rememberAppThemeColors
import com.example.damandroid.ui.theme.rememberAuthScreenPalette
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    onLogin: () -> Unit,
    onSignUpClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    logoResId: Int = R.drawable.nexo_logo,
    onGoogleSignInRequest: ((GoogleSignInAccount?) -> Unit) -> Unit = {},
    googleSignInHelper: com.example.damandroid.auth.GoogleSignInHelper? = null,
    facebookSignInHelper: com.example.damandroid.auth.FacebookSignInHelper? = null,
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showVerificationPrompt by remember { mutableStateOf(false) }
    var verificationMessage by remember { mutableStateOf<String?>(null) }
    var isResending by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val rememberMeStore = remember(context) { RememberMeStore(context.applicationContext) }
    var rememberMe by remember { mutableStateOf(rememberMeStore.isRememberMeEnabled()) }

    val authRepository = remember(context) { AuthRepository(context.applicationContext) }
    val coroutineScope = rememberCoroutineScope()
    val themeController = LocalThemeController.current
    val appTheme = rememberAppThemeColors(themeController.isDarkMode)
    val palette = rememberAuthScreenPalette(appTheme)
    val orbColors = palette.orbColors

    LaunchedEffect(Unit) {
        if (rememberMe) {
            rememberMeStore.getEmail()?.let { savedEmail ->
                email = savedEmail
            }
            rememberMeStore.getPassword()?.let { savedPassword ->
                password = savedPassword
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(palette.background)
    ) {
        // Floating Orbs for Depth - Neon Electric Accents
        LoginOrb(
            size = 128.dp,
            color = orbColors.getOrElse(0) { Color(0xFF8B5CF6).copy(alpha = 0.30f) },
            top = 80.dp,
            start = 40.dp,
            pulseDurationMs = 1600
        )
        LoginOrb(
            size = 160.dp,
            color = orbColors.getOrElse(1) { Color(0xFFEC4899).copy(alpha = 0.35f) },
            bottom = 160.dp,
            end = 40.dp,
            pulseDurationMs = 1600,
            startDelayMs = 1000
        )
        LoginOrb(
            size = 96.dp,
            color = orbColors.getOrElse(2) { Color(0xFF0066FF).copy(alpha = 0.30f) },
            center = true,
            pulseDurationMs = 1600,
            startDelayMs = 2000
        )
        LoginOrb(
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
                .padding(horizontal = 24.dp, vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo Section
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = 32.dp)
            ) {
                Box {
                    // Shimmer effect
                    Box(
                        modifier = Modifier
                            .size(88.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(
                                        Color(0xFF8B5CF6).copy(alpha = 0.2f),
                                        Color.White.copy(alpha = 0.4f),
                                        Color(0xFFEC4899).copy(alpha = 0.2f)
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
                            .border(2.dp, palette.glassBorder, RoundedCornerShape(24.dp))
                    ) {
                        // Inner glow
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(24.dp))
                                .background(palette.highlightGradient)
                        )

                        Image(
                            painter = painterResource(id = logoResId),
                            contentDescription = "NEXO Logo",
                            modifier = Modifier
                                .size(56.dp)
                                .align(Alignment.Center),
                            contentScale = ContentScale.Fit
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Welcome Back",
                    color = palette.primaryText,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Sign in to continue your fitness journey",
                    color = palette.mutedText,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            }

            // Login Form Card - Crystal Glass Effect
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
                                    colors = orbColors.map { it.copy(alpha = 0.35f) }
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
                            modifier = Modifier.padding(bottom = 16.dp)
                        ) {
                            Text(
                                text = "Email",
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
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(60.dp)
                            )
                        }

                        // Password Input
                        Column(
                            modifier = Modifier.padding(bottom = 8.dp)
                        ) {
                            Text(
                                text = "Password",
                                color = palette.secondaryText,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            OutlinedTextField(
                                value = password,
                                onValueChange = { password = it },
                                placeholder = {
                                    Text(
                                        "Mot de passe",
                                        color = palette.fieldPlaceholder
                                    )
                                },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Lock,
                                        contentDescription = null,
                                        tint = palette.fieldIcon,
                                        modifier = Modifier.size(20.dp)
                                    )
                                },
                                trailingIcon = {
                                    IconButton(onClick = { showPassword = !showPassword }) {
                                        Icon(
                                            imageVector = if (showPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                            contentDescription = null,
                                            tint = palette.fieldIcon,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                },
                                singleLine = true,
                                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
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
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp)
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
                                    .padding(top = 8.dp, bottom = 4.dp)
                            )
                        }

                        if (showVerificationPrompt) {
                            VerificationPromptCard(
                                palette = palette,
                                email = email.trim(),
                                isResending = isResending,
                                statusMessage = verificationMessage,
                                onResend = {
                                    if (email.trim().isBlank()) {
                                        verificationMessage = "Enter your email first"
                                        return@VerificationPromptCard
                                    }
                                    verificationMessage = null
                                    coroutineScope.launch {
                                        isResending = true
                                        val result = authRepository.sendVerificationEmail(email.trim())
                                        isResending = false
                                        verificationMessage = when (result) {
                                            is AuthRepository.PasswordResetResult.Success -> result.message
                                            is AuthRepository.PasswordResetResult.Error -> result.message
                                        }
                                    }
                                }
                            )
                        }

                        // Remember Me & Forgot Password
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .toggleable(
                                        value = rememberMe,
                                        role = Role.Checkbox,
                                        onValueChange = { checked ->
                                            rememberMe = checked
                                            rememberMeStore.setEnabled(checked)
                                        }
                                    )
                            ) {
                                Checkbox(
                                    checked = rememberMe,
                                    onCheckedChange = null,
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = palette.linkText,
                                        uncheckedColor = palette.secondaryText,
                                        checkmarkColor = Color.White
                                    )
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    "Remember me",
                                    color = palette.secondaryText,
                                    fontSize = 14.sp
                                )
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            TextButton(onClick = onForgotPasswordClick) {
                                Text(
                                    "Forgot password?",
                                    color = palette.linkText,
                                    fontSize = 14.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Login Button
                        Button(
                            onClick = {
                                if (email.isBlank() || password.isBlank()) {
                                    errorMessage = "Please fill in all fields"
                                    return@Button
                                }
                                isLoading = true
                                errorMessage = null
                                showVerificationPrompt = false
                                verificationMessage = null
                                coroutineScope.launch {
                                    val result = authRepository.login(
                                        email = email.trim(),
                                        password = password,
                                        rememberMe = rememberMe
                                    )
                                    isLoading = false
                                    when (result) {
                                        is AuthRepository.AuthResult.Success -> {
                                            // TODO: Save token to SharedPreferences or secure storage
                                            showVerificationPrompt = false
                                            if (rememberMe) {
                                                rememberMeStore.saveCredentials(email.trim(), password)
                                            } else {
                                                rememberMeStore.setEnabled(false)
                                            }
                                            onLogin()
                                        }
                                        is AuthRepository.AuthResult.Error -> {
                                            errorMessage = result.message
                                            showVerificationPrompt = result.message.contains("Email not verified", ignoreCase = true)
                                            if (!showVerificationPrompt) {
                                                verificationMessage = null
                                            }
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
                                containerColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent.copy(alpha = 0.5f)
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
                                        "Login",
                                        color = palette.buttonText,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Divider
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(2.dp)
                                    .background(palette.dividerColor)
                            )
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = palette.dividerLabelBackground,
                                border = BorderStroke(1.dp, palette.glassBorder),
                                modifier = Modifier.padding(horizontal = 12.dp)
                            ) {
                                Text(
                                    "or",
                                    color = palette.secondaryText,
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(2.dp)
                                    .background(palette.dividerColor)
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Social Login Buttons
                        SocialButton(
                            text = "Continue with Google",
                            iconResId = R.drawable.google,
                            onClick = {
                                onGoogleSignInRequest { account ->
                                    if (account != null) {
                                        // Handle successful Google Sign-In
                                        coroutineScope.launch {
                                            isLoading = true
                                            errorMessage = null
                                            
                                            try {
                                                val email = account.email ?: ""
                                                val name = account.displayName ?: ""
                                                val idToken = account.idToken
                                                val photoUrl = account.photoUrl?.toString()
                                                
                                                android.util.Log.d("LoginScreen", "Google Sign-In successful: $email")
                                                
                                                // Send to backend API
                                                val result = authRepository.loginWithGoogle(
                                                    email = email,
                                                    name = name,
                                                    idToken = idToken,
                                                    photoUrl = photoUrl
                                                )
                                                
                                                isLoading = false
                                                
                                                when (result) {
                                                    is AuthRepository.AuthResult.Success -> {
                                                        // TODO: Save token to SharedPreferences or secure storage
                                                        android.util.Log.d("LoginScreen", "Google login backend successful")
                                                        onLogin()
                                                    }
                                                    is AuthRepository.AuthResult.Error -> {
                                                        errorMessage = result.message
                                                        android.util.Log.e("LoginScreen", "Google login backend error: ${result.message}")
                                                    }
                                                }
                                            } catch (e: Exception) {
                                                isLoading = false
                                                errorMessage = "Error connecting to server: ${e.message}"
                                                android.util.Log.e("LoginScreen", "Exception in Google login: ${e.message}", e)
                                            }
                                        }
                                    } else {
                                        android.util.Log.e("LoginScreen", "Google Sign-In failed or cancelled")
                                    }
                                }
                            },
                            palette = palette,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp)
                        )

                        SocialButton(
                            text = "Continue with Facebook",
                            iconResId = R.drawable.fb,
                            onClick = {
                                coroutineScope.launch {
                                    isLoading = true
                                    errorMessage = null
                                    
                                    try {
                                        val loginResult = facebookSignInHelper?.login() 
                                            ?: throw IllegalStateException("FacebookSignInHelper not provided")
                                        
                                        when (loginResult) {
                                            is com.example.damandroid.auth.FacebookSignInHelper.FacebookLoginResult.Success -> {
                                                val email = loginResult.email ?: ""
                                                val name = loginResult.name ?: ""
                                                val userId = loginResult.userId
                                                val accessToken = loginResult.accessToken.token
                                                val photoUrl = loginResult.photoUrl
                                                
                                                android.util.Log.d("LoginScreen", "Facebook Sign-In successful: $email")
                                                
                                                // Send to backend API
                                                val result = authRepository.loginWithFacebook(
                                                    email = email,
                                                    name = name,
                                                    userId = userId,
                                                    accessToken = accessToken,
                                                    photoUrl = photoUrl
                                                )
                                                
                                                isLoading = false
                                                
                                                when (result) {
                                                    is AuthRepository.AuthResult.Success -> {
                                                        // TODO: Save token to SharedPreferences or secure storage
                                                        android.util.Log.d("LoginScreen", "Facebook login backend successful")
                                                        onLogin()
                                                    }
                                                    is AuthRepository.AuthResult.Error -> {
                                                        errorMessage = result.message
                                                        android.util.Log.e("LoginScreen", "Facebook login backend error: ${result.message}")
                                                    }
                                                }
                                            }
                                            is com.example.damandroid.auth.FacebookSignInHelper.FacebookLoginResult.Cancelled -> {
                                                isLoading = false
                                                android.util.Log.d("LoginScreen", "Facebook Sign-In cancelled")
                                            }
                                        }
                                    } catch (e: Exception) {
                                        isLoading = false
                                        errorMessage = "Error connecting to Facebook: ${e.message}"
                                        android.util.Log.e("LoginScreen", "Exception in Facebook login: ${e.message}", e)
                                    }
                                }
                            },
                            palette = palette,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Sign Up Link
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Don't have an account? ",
                    color = palette.mutedText,
                    fontSize = 14.sp
                )
                Text(
                    text = "Sign Up",
                    color = palette.linkText,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onSignUpClick() }
                )
            }
        }
    }
}

@Composable
private fun SocialButton(
    text: String,
    iconResId: Int,
    onClick: () -> Unit,
    palette: AuthScreenPalette,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(palette.socialGradient, RoundedCornerShape(16.dp))
                .border(2.dp, palette.socialBorder, RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = iconResId),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = text,
                    color = palette.socialText,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun LoginOrb(
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
    val infinite = rememberInfiniteTransition(label = "login-orb")
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

@Composable
private fun VerificationPromptCard(
    palette: AuthScreenPalette,
    email: String,
    isResending: Boolean,
    statusMessage: String?,
    onResend: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
        colors = CardDefaults.cardColors(containerColor = palette.cardSurface),
        border = BorderStroke(2.dp, palette.glassBorder),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.size(40.dp),
                    color = palette.linkText.copy(alpha = 0.15f),
                    shape = CircleShape
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.MailOutline,
                            contentDescription = null,
                            tint = palette.primaryText,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Verify your email",
                        color = palette.primaryText,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "We sent a verification link to $email. Please verify to continue.",
                        color = palette.secondaryText,
                        fontSize = 13.sp
                    )
                }
            }

            Button(
                onClick = onResend,
                enabled = !isResending,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = palette.linkText,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
            ) {
                if (isResending) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Resend verification email", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                }
            }

            statusMessage?.let { message ->
                Text(
                    text = message,
                    color = palette.primaryText,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Preview(
    name = "Login Screen - Light Mode",
    showBackground = true,
    showSystemUi = true,
    device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=portrait"
)
@Composable
fun LoginScreenPreview() {
    val controller = ThemeController(isDarkMode = false) { }
    androidx.compose.runtime.CompositionLocalProvider(LocalThemeController provides controller) {
        DamAndroidTheme(darkTheme = controller.isDarkMode) {
            LoginScreen(
                onLogin = { },
                onSignUpClick = { },
                onForgotPasswordClick = { }
            )
        }
    }
}

@Preview(
    name = "Login Screen - Dark Mode",
    showBackground = true,
    showSystemUi = true,
    device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=portrait"
)
@Composable
fun LoginScreenDarkPreview() {
    val controller = ThemeController(isDarkMode = true) { }
    androidx.compose.runtime.CompositionLocalProvider(LocalThemeController provides controller) {
        DamAndroidTheme(darkTheme = controller.isDarkMode) {
            LoginScreen(
                onLogin = { },
                onSignUpClick = { },
                onForgotPasswordClick = { }
            )
        }
    }
}
