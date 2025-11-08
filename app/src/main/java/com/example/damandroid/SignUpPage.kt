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
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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
import com.example.damandroid.ui.theme.AuthScreenPalette
import com.example.damandroid.ui.theme.DamAndroidTheme
import com.example.damandroid.ui.theme.LocalThemeController
import com.example.damandroid.ui.theme.ThemeController
import com.example.damandroid.ui.theme.rememberAppThemeColors
import com.example.damandroid.ui.theme.rememberAuthScreenPalette
import com.example.damandroid.api.AuthRepository
import com.example.damandroid.api.LocationRepository
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.example.damandroid.api.CityLocation
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.ArrowDropUp

@Composable
fun SignUpPage(
    onSignUp: () -> Unit,
    onLoginClick: () -> Unit,
    logoResId: Int = R.drawable.nexo_logo,
    onGoogleSignInRequest: ((GoogleSignInAccount?) -> Unit) -> Unit = {},
    googleSignInHelper: com.example.damandroid.auth.GoogleSignInHelper? = null,
    facebookSignInHelper: com.example.damandroid.auth.FacebookSignInHelper? = null,
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }
    var acceptTerms by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    
    // Location suggestions
    var locationSuggestions by remember { mutableStateOf<List<CityLocation>>(emptyList()) }
    var showLocationSuggestions by remember { mutableStateOf(false) }
    var isSearchingLocation by remember { mutableStateOf(false) }
    
    val context = LocalContext.current
    val authRepository = remember(context) { AuthRepository(context.applicationContext) }
    val locationRepository = remember { LocationRepository() }
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
        LoginOrb(size = 128.dp, color = orbColors.getOrElse(0) { Color(0xFF8B5CF6).copy(alpha = 0.30f) }, top = 80.dp, start = 40.dp, pulseDurationMs = 1600)
        LoginOrb(size = 160.dp, color = orbColors.getOrElse(1) { Color(0xFFEC4899).copy(alpha = 0.30f) }, bottom = 160.dp, end = 40.dp, startDelayMs = 1000, pulseDurationMs = 1600)
        LoginOrb(size = 96.dp, color = orbColors.getOrElse(2) { Color(0xFF0066FF).copy(alpha = 0.30f) }, center = true, startDelayMs = 2000, pulseDurationMs = 1600)
        LoginOrb(size = 80.dp, color = orbColors.getOrElse(3) { Color(0xFF2ECC71).copy(alpha = 0.25f) }, top = 120.dp, end = 80.dp, startDelayMs = 3000, pulseDurationMs = 1600)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(top = 16.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Logo Section
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                Box {
                    // Shimmer effect
                    Box(
                        modifier = Modifier
                            .size(88.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(
                                Brush.linearGradient(
                                    colors = orbColors.map { it.copy(alpha = 0.2f) }
                                )
                            )
                            .blur(24.dp)
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
                    text = "Join NEXO",
                    color = palette.primaryText,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Create your account and start connecting",
                    color = palette.mutedText,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            }

            // Sign Up Form Card - Crystal Glass Effect
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
                        // Name Input
                        Column(
                            modifier = Modifier.padding(bottom = 16.dp)
                        ) {
                            Text(
                                text = "Full Name",
                                color = palette.secondaryText,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            OutlinedTextField(
                                value = name,
                                onValueChange = { name = it },
                                placeholder = {
                                    Text(
                                        "Nom complet",
                                        color = palette.fieldPlaceholder,
                                        maxLines = 1
                                    )
                                },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Person,
                                        contentDescription = null,
                                        tint = palette.fieldIcon,
                                        modifier = Modifier.size(20.dp)
                                    )
                                },
                                singleLine = true,
                                textStyle = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
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

                        // Email Input
                        Column(
                            modifier = Modifier.padding(bottom = 16.dp)
                        ) {
                            Text(
                                text = "Email",
                                color = palette.secondaryText,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            OutlinedTextField(
                                value = email,
                                onValueChange = { email = it },
                                placeholder = {
                                    Text(
                                        "email@exemple.com",
                                        color = palette.fieldPlaceholder,
                                        maxLines = 1
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
                                textStyle = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
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

                        // Location Input with suggestions
                        Column(
                            modifier = Modifier
                                .padding(bottom = 16.dp)
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = "Location",
                                color = palette.secondaryText,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            
                            Box {
                                OutlinedTextField(
                                    value = location,
                                    onValueChange = { newValue ->
                                        location = newValue
                                        showLocationSuggestions = newValue.isNotEmpty()
                                        
                                        // Debounce search
                                        coroutineScope.launch {
                                            delay(300) // Wait 300ms after user stops typing
                                            if (location == newValue && newValue.length >= 2) {
                                                isSearchingLocation = true
                                                val suggestions = locationRepository.searchCities(newValue)
                                                locationSuggestions = suggestions
                                                isSearchingLocation = false
                                                showLocationSuggestions = suggestions.isNotEmpty()
                                            } else if (newValue.length < 2) {
                                                locationSuggestions = emptyList()
                                                showLocationSuggestions = false
                                            }
                                        }
                                    },
                                    placeholder = {
                                        Text(
                                            "Ville",
                                        color = palette.fieldPlaceholder,
                                            maxLines = 1
                                        )
                                    },
                                    leadingIcon = {
                                        Icon(
                                            Icons.Default.LocationOn,
                                            contentDescription = null,
                                        tint = palette.fieldIcon,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    },
                                    trailingIcon = {
                                        if (isSearchingLocation) {
                                            CircularProgressIndicator(
                                                modifier = Modifier.size(20.dp),
                                                strokeWidth = 2.dp,
                                                color = palette.fieldIcon
                                            )
                                        } else if (showLocationSuggestions && locationSuggestions.isNotEmpty()) {
                                            Icon(
                                                Icons.Default.ArrowDropUp,
                                                contentDescription = null,
                                                tint = palette.fieldIcon
                                            )
                                        }
                                    },
                                    singleLine = true,
                                    textStyle = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
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
                                
                                // Dropdown suggestions
                                if (showLocationSuggestions && locationSuggestions.isNotEmpty()) {
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 52.dp),
                                        shape = RoundedCornerShape(16.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = palette.cardSurface
                                        ),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                                    ) {
                                        LazyColumn(
                                            modifier = Modifier.heightIn(max = 200.dp)
                                        ) {
                                            items(locationSuggestions) { city ->
                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .clickable {
                                                            location = city.getDisplayName()
                                                            showLocationSuggestions = false
                                                        }
                                                        .padding(horizontal = 16.dp, vertical = 12.dp),
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Icon(
                                                        Icons.Default.LocationOn,
                                                        contentDescription = null,
                                                        tint = palette.linkText,
                                                        modifier = Modifier.size(20.dp)
                                                    )
                                                    Spacer(modifier = Modifier.width(12.dp))
                                                    Text(
                                                        text = city.getDisplayName(),
                                                        color = palette.primaryText,
                                                        fontSize = 14.sp
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // Password Input
                        Column(
                            modifier = Modifier.padding(bottom = 16.dp)
                        ) {
                            Text(
                                text = "Password",
                                color = palette.secondaryText,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            OutlinedTextField(
                                value = password,
                                onValueChange = { password = it },
                                placeholder = {
                                    Text(
                                        "Mot de passe",
                                        color = palette.fieldPlaceholder,
                                        maxLines = 1
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
                                textStyle = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
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

                        // Confirm Password Input
                        Column(
                            modifier = Modifier.padding(bottom = 8.dp)
                        ) {
                            Text(
                                text = "Confirm Password",
                                color = palette.secondaryText,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            OutlinedTextField(
                                value = confirmPassword,
                                onValueChange = { confirmPassword = it },
                                placeholder = {
                                    Text(
                                        "Confirmer",
                                        color = palette.fieldPlaceholder,
                                        maxLines = 1
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
                                    IconButton(onClick = { showConfirmPassword = !showConfirmPassword }) {
                                        Icon(
                                            imageVector = if (showConfirmPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                            contentDescription = null,
                                            tint = palette.fieldIcon,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                },
                                singleLine = true,
                                visualTransformation = if (showConfirmPassword) VisualTransformation.None else PasswordVisualTransformation(),
                                textStyle = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
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

                        // Terms Agreement
                        Row(
                            modifier = Modifier.padding(bottom = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = acceptTerms,
                                onCheckedChange = { acceptTerms = it },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = palette.linkText,
                                    uncheckedColor = palette.fieldBorder
                                )
                            )
                            Text(
                                text = "I agree to the Terms of Service and Privacy Policy",
                                color = palette.mutedText,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(start = 4.dp)
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

                        // Sign Up Button
                        Button(
                            onClick = {
                                if (name.isBlank() || email.isBlank() || location.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
                                    errorMessage = "Please fill in all fields"
                                    return@Button
                                }
                                if (password != confirmPassword) {
                                    errorMessage = "Passwords do not match"
                                    return@Button
                                }
                                if (password.length < 6) {
                                    errorMessage = "Password must be at least 6 characters long"
                                    return@Button
                                }
                                if (!acceptTerms) {
                                    errorMessage = "Please accept the terms and conditions"
                                    return@Button
                                }
                                isLoading = true
                                errorMessage = null
                                coroutineScope.launch {
                                    val result = authRepository.register(
                                        email = email.trim(),
                                        password = password,
                                        name = name.trim(),
                                        location = location.trim()
                                    )
                                    isLoading = false
                                    when (result) {
                                        is AuthRepository.AuthResult.Success -> {
                                            // Registration successful - navigate back to login page
                                            // Empty token indicates registration-only (no auto-login)
                                            if (result.token.isEmpty()) {
                                                onLoginClick()
                                            } else {
                                                // If somehow we got a token, save it and navigate to home
                                                // TODO: Save token to SharedPreferences or secure storage
                                                onSignUp()
                                            }
                                        }
                                        is AuthRepository.AuthResult.Error -> {
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
                                        "Create Account",
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
                                    "or sign up with",
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

                        // Social Sign Up Buttons
                        SocialButton(
                            text = "Google",
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
                                                
                                                android.util.Log.d("SignUpPage", "Google Sign-In successful: $email")
                                                
                                                // Send to backend API (will create user if doesn't exist, or login if exists)
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
                                                        android.util.Log.d("SignUpPage", "Google signup/login backend successful")
                                                        onSignUp()
                                                    }
                                                    is AuthRepository.AuthResult.Error -> {
                                                        errorMessage = result.message
                                                        android.util.Log.e("SignUpPage", "Google signup/login backend error: ${result.message}")
                                                    }
                                                }
                                            } catch (e: Exception) {
                                                isLoading = false
                                                errorMessage = "Error connecting to server: ${e.message}"
                                                android.util.Log.e("SignUpPage", "Exception in Google signup: ${e.message}", e)
                                            }
                                        }
                                    } else {
                                        android.util.Log.e("SignUpPage", "Google Sign-In failed or cancelled")
                                    }
                                }
                            },
                            palette = palette,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp)
                        )

                        SocialButton(
                            text = "Facebook",
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
                                                
                                                android.util.Log.d("SignUpPage", "Facebook Sign-In successful: $email")
                                                
                                                // Send to backend API (will create user if doesn't exist, or login if exists)
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
                                                        android.util.Log.d("SignUpPage", "Facebook signup/login backend successful")
                                                        onSignUp()
                                                    }
                                                    is AuthRepository.AuthResult.Error -> {
                                                        errorMessage = result.message
                                                        android.util.Log.e("SignUpPage", "Facebook signup/login backend error: ${result.message}")
                                                    }
                                                }
                                            }
                                            is com.example.damandroid.auth.FacebookSignInHelper.FacebookLoginResult.Cancelled -> {
                                                isLoading = false
                                                android.util.Log.d("SignUpPage", "Facebook Sign-In cancelled")
                                            }
                                        }
                                    } catch (e: Exception) {
                                        isLoading = false
                                        errorMessage = "Error connecting to Facebook: ${e.message}"
                                        android.util.Log.e("SignUpPage", "Exception in Facebook signup: ${e.message}", e)
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

            // Login Link
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Already have an account? ",
                color = palette.mutedText,
                    fontSize = 14.sp
                )
                Text(
                    text = "Sign In",
                color = palette.linkText,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onLoginClick() }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
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

@Preview(
    name = "Sign Up Page - Light Mode",
    showBackground = true,
    showSystemUi = true,
    device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=portrait"
)
@Composable
fun SignUpPagePreview() {
    val controller = ThemeController(isDarkMode = false) { }
    androidx.compose.runtime.CompositionLocalProvider(LocalThemeController provides controller) {
        DamAndroidTheme(darkTheme = controller.isDarkMode) {
            SignUpPage(
                onSignUp = { },
                onLoginClick = { }
            )
        }
    }
}

@Preview(
    name = "Sign Up Page - Dark Mode",
    showBackground = true,
    showSystemUi = true,
    device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=portrait"
)
@Composable
fun SignUpPageDarkPreview() {
    val controller = ThemeController(isDarkMode = true) { }
    androidx.compose.runtime.CompositionLocalProvider(LocalThemeController provides controller) {
        DamAndroidTheme(darkTheme = controller.isDarkMode) {
            SignUpPage(
                onSignUp = { },
                onLoginClick = { }
            )
        }
    }
}
