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
import com.example.damandroid.auth.UserSession
import com.example.damandroid.ui.theme.DamAndroidTheme
import com.example.damandroid.ui.theme.LocalThemeController
import com.example.damandroid.ui.theme.ThemeController
import com.example.damandroid.ui.theme.ThemePreferences
import com.example.damandroid.data.datasource.*
import com.example.damandroid.data.repository.*
import com.example.damandroid.domain.usecase.*
import com.example.damandroid.presentation.discover.ui.DiscoverRoute
import com.example.damandroid.presentation.discover.viewmodel.DiscoverViewModel
import com.example.damandroid.presentation.notifications.ui.NotificationsRoute
import com.example.damandroid.presentation.notifications.viewmodel.NotificationsViewModel
import com.example.damandroid.presentation.profile.ui.ProfileRoute
import com.example.damandroid.presentation.profile.ui.EditProfileRoute
import com.example.damandroid.presentation.profile.viewmodel.ProfileViewModel
import com.example.damandroid.presentation.settings.ui.SettingsRoute
import com.example.damandroid.presentation.settings.viewmodel.SettingsViewModel
import com.example.damandroid.presentation.chat.ui.ChatListRoute
import com.example.damandroid.presentation.chat.viewmodel.ChatListViewModel
import com.example.damandroid.presentation.ai.ui.AICoachRoute
import com.example.damandroid.presentation.ai.viewmodel.AICoachViewModel
import com.example.damandroid.presentation.quickmatch.ui.QuickMatchRoute
import com.example.damandroid.presentation.quickmatch.viewmodel.QuickMatchViewModel
import com.example.damandroid.presentation.homefeed.ui.HomeFeedRoute
import com.example.damandroid.presentation.homefeed.viewmodel.HomeFeedViewModel
import com.example.damandroid.presentation.createactivity.ui.CreateActivityRoute
import com.example.damandroid.presentation.createactivity.viewmodel.CreateActivityViewModel
import com.example.damandroid.presentation.applyverification.ui.ApplyVerificationRoute
import com.example.damandroid.presentation.applyverification.viewmodel.ApplyVerificationViewModel
import com.example.damandroid.presentation.achievements.ui.AchievementsRoute
import com.example.damandroid.presentation.achievements.viewmodel.AchievementsViewModel
import com.example.damandroid.presentation.eventdetails.ui.EventDetailsRoute
import com.example.damandroid.presentation.eventdetails.viewmodel.EventDetailsViewModel
import com.example.damandroid.presentation.aimatchmaker.ui.AIMatchmakerRoute
import com.example.damandroid.presentation.aimatchmaker.viewmodel.AIMatchmakerViewModel
import com.example.damandroid.presentation.aisuggestions.ui.AISuggestionsRoute
import com.example.damandroid.presentation.aisuggestions.viewmodel.AISuggestionsViewModel
import com.example.damandroid.presentation.coachprofile.ui.CoachProfileRoute
import com.example.damandroid.presentation.coachprofile.viewmodel.CoachProfileViewModel
import com.facebook.CallbackManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.launch
import com.example.damandroid.presentation.settings.ui.ChangePasswordRoute
import com.example.damandroid.domain.model.HomeActivity
import com.example.damandroid.presentation.homefeed.ui.ActivityRoomRoute

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

    @RequiresApi(Build.VERSION_CODES.O)
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
        facebookSignInHelper.getCallbackManager().onActivityResult(requestCode, resultCode, data)
    }

    private fun handleGoogleSignInResult(task: Task<GoogleSignInAccount>) {
        val callback = onGoogleSignInResult
        onGoogleSignInResult = null

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

@RequiresApi(Build.VERSION_CODES.O)
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
        showOnboarding -> OnboardingScreens(onComplete = { showOnboarding = false })
        showResetPassword -> ResetPasswordPage(onBackToLogin = { showResetPassword = false })
        showSignUp -> SignUpPage(
            onSignUp = {
                showSignUp = false
                isLoggedIn = true
            },
            onLoginClick = { showSignUp = false },
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
        else -> MainHomeScreen(onLogout = {
            UserSession.clear()
            isLoggedIn = false
        })
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainHomeScreen(
    onLogout: () -> Unit = {}
) {
    var activeTab by remember { mutableStateOf("home") }
    var overlay by remember { mutableStateOf<OverlayScreen?>(null) }

    val discoverViewModel = remember {
        DiscoverViewModel(GetDiscoverOverview(DiscoverRepositoryImpl(DiscoverRemoteDataSourceImpl())))
    }
    val notificationsRepository = remember { NotificationsRepositoryImpl(NotificationsRemoteDataSourceImpl()) }
    val notificationsViewModel = remember {
        NotificationsViewModel(
            getNotifications = GetNotifications(notificationsRepository),
            markNotificationAsRead = MarkNotificationAsRead(notificationsRepository),
            markAllNotificationsAsRead = MarkAllNotificationsAsRead(notificationsRepository)
        )
    }
    val profileViewModel = remember {
        val profileRepository = ProfileRepositoryImpl(ProfileRemoteDataSourceImpl())
        ProfileViewModel(
            getCurrentUserProfile = GetCurrentUserProfile(profileRepository),
            updateUserProfile = UpdateUserProfile(profileRepository),
            uploadProfileImage = UploadProfileImage(profileRepository),
            changeUserPassword = ChangeUserPassword(profileRepository)
        )
    }
    val settingsRepository = remember { SettingsRepositoryImpl(SettingsRemoteDataSourceImpl()) }
    val settingsViewModel = remember {
        SettingsViewModel(
            getUserSettings = GetUserSettings(settingsRepository),
            updateSettingsToggle = UpdateSettingsToggle(settingsRepository)
        )
    }
    val homeFeedViewModel = remember {
        val remoteDataSource = HomeFeedRemoteDataSourceImpl()
        val repository = HomeFeedRepositoryImpl(remoteDataSource)
        HomeFeedViewModel(
            getHomeFeed = GetHomeFeed(repository),
            toggleActivitySaved = ToggleActivitySaved(repository)
        )
    }
    val chatRepository = remember { ChatRepositoryImpl(ChatRemoteDataSourceImpl()) }
    val chatListViewModel = remember {
        ChatListViewModel(
            getChatPreviews = GetChatPreviews(chatRepository),
            searchChats = SearchChats(chatRepository)
        )
    }
    val aiCoachViewModel = remember {
        AICoachViewModel(
            getAICoachOverview = GetAICoachOverview(AICoachRepositoryImpl(AICoachRemoteDataSourceImpl()))
        )
    }
    val quickMatchViewModel = remember {
        QuickMatchViewModel(
            getQuickMatchProfiles = GetQuickMatchProfiles(QuickMatchRepositoryImpl(QuickMatchRemoteDataSourceImpl()))
        )
    }
    val createActivityRepository = remember { CreateActivityRepositoryImpl(CreateActivityRemoteDataSourceImpl()) }
    val createActivityViewModel = remember {
        CreateActivityViewModel(
            getSportCategories = GetSportCategories(createActivityRepository),
            createActivity = CreateActivityUseCase(createActivityRepository)
        )
    }
    val verificationRepository = remember { VerificationRepositoryImpl(VerificationRemoteDataSourceImpl()) }
    val applyVerificationViewModel = remember {
        ApplyVerificationViewModel(
            getVerificationFormOptions = GetVerificationFormOptions(verificationRepository),
            submitVerificationApplication = SubmitVerificationApplication(verificationRepository)
        )
    }
    val achievementsViewModel = remember {
        AchievementsViewModel(
            getAchievementsOverview = GetAchievementsOverview(AchievementsRepositoryImpl(AchievementsRemoteDataSourceImpl()))
        )
    }
    val eventDetailsViewModel = remember {
        EventDetailsViewModel(
            getEventDetails = GetEventDetails(EventDetailsRepositoryImpl(EventDetailsRemoteDataSourceImpl()))
        )
    }
    val matchmakerViewModel = remember {
        AIMatchmakerViewModel(
            getRecommendations = GetMatchmakerRecommendations(AIMatchmakerRepositoryImpl(AIMatchmakerRemoteDataSourceImpl()))
        )
    }
    val sessionsViewModel = remember {
        AISuggestionsViewModel(
            getSessionsRecommendation = GetSessionsRecommendation(AISuggestionsRepositoryImpl(AISuggestionsRemoteDataSourceImpl()))
        )
    }
    val coachProfileViewModel = remember {
        CoachProfileViewModel(
            getCoachProfile = GetCoachProfile(CoachProfileRepositoryImpl(CoachProfileRemoteDataSourceImpl()))
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when (val currentOverlay = overlay) {
            OverlayScreen.CreateActivity -> CreateActivityRoute(
                viewModel = createActivityViewModel,
                onBack = { overlay = null },
                modifier = Modifier.fillMaxSize()
            )
            OverlayScreen.Discover -> DiscoverRoute(
                viewModel = discoverViewModel,
                onBack = { overlay = null },
                onCoachClick = { coachId -> overlay = OverlayScreen.CoachProfile(coachId) },
                modifier = Modifier.fillMaxSize()
            )
            is OverlayScreen.CoachProfile -> CoachProfileRoute(
                coachId = currentOverlay.coachId,
                viewModel = coachProfileViewModel,
                onBack = { overlay = null },
                onMessage = { /* TODO */ },
                onBookSession = { overlay = OverlayScreen.CreateActivity },
                modifier = Modifier.fillMaxSize()
            )
            is OverlayScreen.EventDetails -> EventDetailsRoute(
                eventId = currentOverlay.eventId,
                viewModel = eventDetailsViewModel,
                onBack = { overlay = null },
                onCoachProfile = { coachId ->
                    overlay = OverlayScreen.CoachProfile(coachId)
                },
                onMessageCoach = { /* TODO message coach */ },
                modifier = Modifier.fillMaxSize()
            )
            OverlayScreen.Achievements -> AchievementsRoute(
                viewModel = achievementsViewModel,
                onBack = { overlay = null },
                modifier = Modifier.fillMaxSize()
            )
            OverlayScreen.AICoach -> AICoachRoute(
                viewModel = aiCoachViewModel,
                onBack = { overlay = null },
                modifier = Modifier.fillMaxSize()
            )
            OverlayScreen.AIMatchmaker -> AIMatchmakerRoute(
                viewModel = matchmakerViewModel,
                onBack = { overlay = null },
                modifier = Modifier.fillMaxSize()
            )
            OverlayScreen.QuickMatch -> QuickMatchRoute(
                viewModel = quickMatchViewModel,
                onBack = { overlay = null },
                modifier = Modifier.fillMaxSize()
            )
            is OverlayScreen.ActivityRoom -> ActivityRoomRoute(
                activity = currentOverlay.activity,
                onBack = { overlay = null },
                onLeave = { overlay = null },
                onMarkComplete = { overlay = null },
                modifier = Modifier.fillMaxSize()
            )
            OverlayScreen.EditProfile -> EditProfileRoute(
                viewModel = profileViewModel,
                onBack = { overlay = null },
                onSave = { overlay = null },
                modifier = Modifier.fillMaxSize()
            )
            OverlayScreen.ChangePassword -> ChangePasswordRoute(
                viewModel = profileViewModel,
                onBack = { overlay = null },
                modifier = Modifier.fillMaxSize()
            )
            OverlayScreen.Settings -> SettingsRoute(
                viewModel = settingsViewModel,
                onBack = { overlay = null },
                onApplyVerification = { overlay = OverlayScreen.ApplyVerification },
                onEditProfile = { overlay = OverlayScreen.EditProfile },
                onChangePassword = { overlay = OverlayScreen.ChangePassword },
                onLogout = { onLogout() },
                modifier = Modifier.fillMaxSize()
            )
            OverlayScreen.ApplyVerification -> ApplyVerificationRoute(
                viewModel = applyVerificationViewModel,
                onBack = { overlay = null },
                modifier = Modifier.fillMaxSize()
            )
            null -> when (activeTab) {
                "home" -> HomeFeedRoute(
                    viewModel = homeFeedViewModel,
                    onActivityClick = { activity -> overlay = OverlayScreen.ActivityRoom(activity) },
                    onSearchClick = { overlay = OverlayScreen.Discover },
                    onAISuggestionsClick = { activeTab = "map" },
                    onQuickMatchClick = { overlay = OverlayScreen.QuickMatch },
                    onAIMatchmakerClick = { overlay = OverlayScreen.AIMatchmaker },
                    onEventDetailsClick = { eventId -> overlay = OverlayScreen.EventDetails(eventId) },
                    onCreateClick = { overlay = OverlayScreen.CreateActivity },
                    onNotificationsClick = { activeTab = "notifications" },
                    modifier = Modifier.fillMaxSize()
                )
                "map" -> AISuggestionsRoute(
                    viewModel = sessionsViewModel,
                    onBack = { activeTab = "home" },
                    modifier = Modifier.fillMaxSize()
                )
                "chat" -> ChatListRoute(
                    viewModel = chatListViewModel,
                    onChatSelected = { /* TODO */ },
                    modifier = Modifier.fillMaxSize()
                )
                "notifications" -> NotificationsRoute(
                    viewModel = notificationsViewModel,
                    onBack = { activeTab = "home" },
                    modifier = Modifier.fillMaxSize()
                )
                "profile" -> ProfileRoute(
                    viewModel = profileViewModel,
                    onSettingsClick = { overlay = OverlayScreen.Settings },
                    onAchievementsClick = { overlay = OverlayScreen.Achievements },
                    onLogoutClick = { onLogout() },
                    modifier = Modifier.fillMaxSize()
                )
                else -> Unit
            }
        }

        if (overlay == null) {
            BottomNav(
                activeTab = activeTab,
                onTabChange = { activeTab = it },
                onAICoachClick = { overlay = OverlayScreen.AICoach },
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

private sealed interface OverlayScreen {
    data object QuickMatch : OverlayScreen
    data object AIMatchmaker : OverlayScreen
    data object AICoach : OverlayScreen
    data object Achievements : OverlayScreen
    data class CoachProfile(val coachId: String) : OverlayScreen
    data class EventDetails(val eventId: String) : OverlayScreen
    data object Discover : OverlayScreen
    data object CreateActivity : OverlayScreen
    data object Settings : OverlayScreen
    data object ApplyVerification : OverlayScreen
    data object EditProfile : OverlayScreen
    data object ChangePassword : OverlayScreen
    data class ActivityRoom(val activity: HomeActivity) : OverlayScreen
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
