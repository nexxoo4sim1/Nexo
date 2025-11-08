package com.example.damandroid

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.damandroid.auth.GoogleSignInHelper
import com.example.damandroid.auth.FacebookSignInHelper
import com.example.damandroid.ui.theme.DamAndroidTheme
import com.example.damandroid.ui.theme.LocalThemeController
import com.example.damandroid.ui.theme.ThemeController
import com.example.damandroid.ui.theme.ThemePreferences
import com.facebook.CallbackManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var googleSignInHelper: GoogleSignInHelper
    private lateinit var facebookSignInHelper: FacebookSignInHelper
    private var onGoogleSignInResult: ((GoogleSignInAccount?) -> Unit)? = null
    
    private val googleSignInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        handleGoogleSignInResult(task)
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        googleSignInHelper = GoogleSignInHelper(this)
        facebookSignInHelper = FacebookSignInHelper(this)
        
        setContent {
            val themePreferences = remember { ThemePreferences(applicationContext) }
            val systemDark = isSystemInDarkTheme()
            var isDarkMode by remember { mutableStateOf(themePreferences.getSavedDarkMode() ?: systemDark) }

            LaunchedEffect(isDarkMode) {
                themePreferences.saveDarkMode(isDarkMode)
            }

            CompositionLocalProvider(
                LocalThemeController provides ThemeController(
                    isDarkMode = isDarkMode,
                    setDarkMode = { isDarkMode = it }
                )
            ) {
                DamAndroidTheme(darkTheme = isDarkMode) {
                    MainApp(
                        onGoogleSignInRequest = { callback ->
                            onGoogleSignInResult = callback
                            val signInIntent = googleSignInHelper.getSignInClient().signInIntent
                            googleSignInLauncher.launch(signInIntent)
                        },
                        facebookSignInHelper = facebookSignInHelper
                    )
                }
            }
        }
    }
    
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Handle Facebook callback
        facebookSignInHelper.getCallbackManager().onActivityResult(requestCode, resultCode, data)
    }
    
    private fun handleGoogleSignInResult(task: Task<GoogleSignInAccount>) {
        val callback = onGoogleSignInResult
        onGoogleSignInResult = null // Reset callback
        
        if (callback != null) {
            try {
                val account = task.getResult(com.google.android.gms.common.api.ApiException::class.java)
                callback(account)
            } catch (e: com.google.android.gms.common.api.ApiException) {
                android.util.Log.e("MainActivity", "Google Sign-In failed: ${e.statusCode}", e)
                callback(null)
            }
        }
    }
}

@Composable
fun MainApp(
    onGoogleSignInRequest: ((GoogleSignInAccount?) -> Unit) -> Unit = {},
    facebookSignInHelper: FacebookSignInHelper? = null
) {
    val context = LocalContext.current
    var showSplash by remember { mutableStateOf(true) }
    var showOnboarding by remember { mutableStateOf(false) }
    var isLoggedIn by remember { mutableStateOf(false) }
    var showSignUp by remember { mutableStateOf(false) }
    var showResetPassword by remember { mutableStateOf(false) }
    
    val googleSignInHelper = remember { GoogleSignInHelper(context) }

    when {
        showSplash -> SplashScreen(
            onComplete = { 
                showSplash = false
                showOnboarding = true
            },
            logoResId = R.drawable.nexo_logo
        )

        showOnboarding -> OnboardingScreens(
            onComplete = { showOnboarding = false }
        )

        showResetPassword -> ResetPasswordPage(
            onBackToLogin = {
                showResetPassword = false
            }
        )

        showSignUp -> SignUpPage(
            onSignUp = {
                showSignUp = false
                isLoggedIn = true
            },
            onLoginClick = {
                showSignUp = false
            },
            onGoogleSignInRequest = onGoogleSignInRequest,
            googleSignInHelper = googleSignInHelper,
            facebookSignInHelper = facebookSignInHelper
        )

        !isLoggedIn -> LoginScreen(
            onLogin = { isLoggedIn = true },
            onSignUpClick = { showSignUp = true },
            onForgotPasswordClick = { showResetPassword = true },
            logoResId = R.drawable.nexo_logo,
            onGoogleSignInRequest = onGoogleSignInRequest,
            googleSignInHelper = googleSignInHelper,
            facebookSignInHelper = facebookSignInHelper
        )

        else -> MainHomeScreen(
            onLogout = { isLoggedIn = false }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainHomeScreen(
    onLogout: () -> Unit = {}
) {
    var activeTab by remember { mutableStateOf("home") }
    var showQuickMatch by remember { mutableStateOf(false) }
    var showAIMatchmaker by remember { mutableStateOf(false) }
    var showAICoach by remember { mutableStateOf(false) }
    var showAchievements by remember { mutableStateOf(false) }
    var showEventDetails by remember { mutableStateOf(false) }
    var showCoachProfile by remember { mutableStateOf(false) }
    var showNotifications by remember { mutableStateOf(false) }
    var showSearchDiscovery by remember { mutableStateOf(false) }
    var showCreateActivity by remember { mutableStateOf(false) }
    var showSettings by remember { mutableStateOf(false) }
    var showApplyVerification by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            showCreateActivity -> CreateActivity(
                onBack = { showCreateActivity = false },
                modifier = Modifier.fillMaxSize()
            )
            showSearchDiscovery -> SearchDiscovery(
                onBack = { showSearchDiscovery = false },
                onCoachClick = { showAIMatchmaker = true },
                modifier = Modifier.fillMaxSize()
            )
            showNotifications -> {
                NotificationsScreen(
                    onBack = { showNotifications = false },
                    modifier = Modifier.fillMaxSize()
                )
            }
            showCoachProfile -> CoachProfilePage(
                onBack = { showCoachProfile = false },
                onMessage = { /* TODO */ },
                modifier = Modifier.fillMaxSize()
            )
            showEventDetails -> EventDetailsPage(
                onBack = { showEventDetails = false },
                onJoin = { /* TODO */ },
                onViewCoach = { showCoachProfile = true },
                onMessage = { /* TODO */ },
                modifier = Modifier.fillMaxSize()
            )
            showAchievements -> AchievementsPage(
                onBack = { showAchievements = false },
                modifier = Modifier.fillMaxSize()
            )
            showAICoach -> AICoach(
                onBack = { showAICoach = false },
                onStartChallenge = { showAchievements = true },
                onFindPartner = { /* TODO */ },
                modifier = Modifier.fillMaxSize()
            )
            showAIMatchmaker -> AIMatchmaker(
                onBack = { showAIMatchmaker = false },
                onJoinActivity = { /* TODO */ },
                onViewProfile = { /* TODO */ }
            )
            showQuickMatch -> QuickMatchPage(
                onBack = { showQuickMatch = false }
            )
            activeTab == "home" -> HomeFeed(
                onActivityClick = { activity ->
                    // Handle activity click
                },
                onSearchClick = { showSearchDiscovery = true },
                onQuickMatchClick = { showQuickMatch = true },
                onAIMatchmakerClick = { showAIMatchmaker = true },
                onAISuggestionsClick = { /* Not used - AI Suggestions is in Sessions tab */ },
                onEventDetailsClick = { showEventDetails = true },
                onCreateClick = { showCreateActivity = true },
                onNotificationsClick = { showNotifications = true }
            )
            activeTab == "map" -> AISuggestionsPage(
                onBack = { activeTab = "home" },
                onActivityClick = { activity ->
                    // Handle activity click
                },
                showBackButton = false,
                modifier = Modifier.fillMaxSize()
            )
            activeTab == "chat" -> ChatList(
                onChatSelect = { chatId ->
                    // Handle chat selection
                },
                modifier = Modifier.fillMaxSize()
            )
            showApplyVerification -> ApplyVerificationPage(
                onBack = { showApplyVerification = false },
                modifier = Modifier.fillMaxSize()
            )
            showSettings -> SettingsPage(
                onBack = { showSettings = false },
                onApplyVerification = { showApplyVerification = true },
                modifier = Modifier.fillMaxSize()
            )
            activeTab == "profile" -> ProfilePage(
                onSettingsClick = { showSettings = true },
                onAchievementsClick = { showAchievements = true },
                onLogoutClick = { onLogout() },
                modifier = Modifier.fillMaxSize()
            )
        }

        if (!showQuickMatch && !showAIMatchmaker && !showAICoach && !showAchievements && !showEventDetails && !showCoachProfile && !showNotifications && !showSearchDiscovery && !showCreateActivity && !showSettings && !showApplyVerification) {
            BottomNav(
                activeTab = activeTab,
                onTabChange = { activeTab = it },
                onAICoachClick = { showAICoach = true },
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DamAndroidTheme {
        Greeting("Android")
    }
}
