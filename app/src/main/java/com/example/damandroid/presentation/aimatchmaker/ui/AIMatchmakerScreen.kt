package com.example.damandroid.presentation.aimatchmaker.ui

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.damandroid.domain.model.MatchmakerProfile
import com.example.damandroid.presentation.aimatchmaker.model.AIMatchmakerUiState
import com.example.damandroid.presentation.aimatchmaker.viewmodel.AIMatchmakerViewModel
import com.example.damandroid.ui.theme.AppThemeColors
import com.example.damandroid.ui.theme.LocalThemeController
import com.example.damandroid.ui.theme.rememberAppThemeColors
import kotlinx.coroutines.delay

data class Message(
    val id: String,
    val type: MessageType,
    val text: String? = null,
    val options: List<String>? = null,
    val results: MessageResults? = null
)

enum class MessageType {
    AI, USER
}

data class MessageResults(
    val type: ResultType,
    val items: List<Any>
)

enum class ResultType {
    PEOPLE, EVENTS
}

data class PersonResult(
    val id: String,
    val name: String,
    val avatar: String,
    val sport: String,
    val distance: String,
    val matchScore: Int,
    val bio: String,
    val availability: String
)

data class EventResult(
    val id: String,
    val title: String,
    val sportIcon: String,
    val date: String,
    val time: String,
    val location: String,
    val distance: String,
    val participants: Int,
    val maxParticipants: Int,
    val matchScore: Int
)

@Composable
fun AIMatchmakerRoute(
    viewModel: AIMatchmakerViewModel,
    onBack: (() -> Unit)? = null,
    onRefresh: (() -> Unit)? = null,
    onJoinActivity: ((MatchmakerProfile) -> Unit)? = null,
    onViewProfile: ((MatchmakerProfile) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    AIMatchmakerScreen(
        state = uiState,
        onBack = onBack,
        onRefresh = onRefresh ?: viewModel::refresh,
        onJoinActivity = onJoinActivity,
        onViewProfile = onViewProfile,
        modifier = modifier
    )
}

@Composable
fun AIMatchmakerScreen(
    state: AIMatchmakerUiState,
    onBack: (() -> Unit)? = null,
    onRefresh: () -> Unit,
    onJoinActivity: ((MatchmakerProfile) -> Unit)? = null,
    onViewProfile: ((MatchmakerProfile) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    when {
        state.isLoading -> LoadingState(modifier)
        state.error != null -> ErrorState(message = state.error, onRetry = onRefresh, modifier = modifier)
        else -> LegacyAIMatchmaker(
            modifier = modifier,
            onBack = onBack,
            onJoinActivity = { eventId ->
                state.profiles.firstOrNull { it.id == eventId }?.let { profile ->
                    onJoinActivity?.invoke(profile)
                }
            },
            onViewProfile = { profileId ->
                state.profiles.firstOrNull { it.id == profileId }?.let { profile ->
                    onViewProfile?.invoke(profile)
                }
            }
        )
    }
}

@Composable
private fun LoadingState(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorState(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(text = message)
            Button(onClick = onRetry) { Text("Retry") }
        }
    }
}

@Composable
private fun LegacyAIMatchmaker(
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)?,
    onJoinActivity: (String) -> Unit,
    onViewProfile: (String) -> Unit
) {
    val theme = rememberAppThemeColors(LocalThemeController.current.isDarkMode)
    val isDark = theme.isDark
    val backgroundGradient = if (isDark) {
        theme.backgroundGradient
    } else {
        Brush.linearGradient(
            colors = listOf(
                Color(0xFFF5F6F8),
                Color(0xFFF0F2F5),
                Color(0xFFEBEDF0)
            )
        )
    }

    var messages by remember {
        mutableStateOf(
            listOf(
                Message(
                    id = "1",
                    type = MessageType.AI,
                    text = "Hi! I'm your AI matchmaker. I can help you find the perfect sport partners or activities. What would you like to do today?",
                    options = listOf(
                        "Find a running partner",
                        "Join a group activity",
                        "Discover new sports"
                    )
                )
            )
        )
    }
    var inputValue by remember { mutableStateOf("") }
    var pendingOption by remember { mutableStateOf<String?>(null) }
    var pendingUserInput by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(pendingOption) {
        pendingOption?.let { option ->
            delay(800)
            val aiResponse = generateAIResponse(option)
            messages = messages + aiResponse
            pendingOption = null
        }
    }

    LaunchedEffect(pendingUserInput) {
        pendingUserInput?.let {
            delay(800)
            val aiResponse = Message(
                id = (System.currentTimeMillis() + 1).toString(),
                type = MessageType.AI,
                text = "Let me find the best matches for you...",
                options = listOf(
                    "Show me runners nearby",
                    "Find group activities",
                    "Something else"
                )
            )
            messages = messages + aiResponse
            pendingUserInput = null
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundGradient)
    ) {
        AIMatchmakerOrb(
            size = 128.dp,
            color = theme.accentPurple.copy(alpha = if (isDark) 0.22f else 0.30f),
            top = 80.dp,
            start = 40.dp,
            pulseDurationMs = 1600
        )
        AIMatchmakerOrb(
            size = 160.dp,
            color = theme.accentPink.copy(alpha = if (isDark) 0.26f else 0.35f),
            bottom = 160.dp,
            end = 40.dp,
            pulseDurationMs = 1600,
            startDelayMs = 1000
        )
        AIMatchmakerOrb(
            size = 96.dp,
            color = theme.accentBlue.copy(alpha = if (isDark) 0.24f else 0.30f),
            center = true,
            pulseDurationMs = 1600,
            startDelayMs = 2000
        )
        AIMatchmakerOrb(
            size = 80.dp,
            color = theme.accentGreen.copy(alpha = if (isDark) 0.2f else 0.25f),
            top = 120.dp,
            end = 80.dp,
            pulseDurationMs = 1600,
            startDelayMs = 3000
        )

        Column(modifier = Modifier.fillMaxSize()) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.Transparent
            ) {
                val headerBrush = if (isDark) {
                    Brush.linearGradient(
                        colors = listOf(
                            theme.glassSurface,
                            theme.glassSurface.copy(alpha = 0.85f)
                        )
                    )
                } else {
                    Brush.linearGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.7f),
                            Color.White.copy(alpha = 0.5f)
                        )
                    )
                }
                val headerBorder = if (isDark) theme.glassBorder else Color.White.copy(alpha = 0.8f)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(headerBrush)
                        .border(2.dp, headerBorder)
                        .windowInsetsPadding(WindowInsets.statusBars)
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { onBack?.invoke() },
                            modifier = Modifier.size(48.dp),
                            enabled = onBack != null
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = theme.primaryText,
                                modifier = Modifier.size(26.dp)
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if (isDark) {
                                                Brush.linearGradient(
                                                    listOf(
                                                        theme.accentPurple.copy(alpha = 0.28f),
                                                        theme.accentBlue.copy(alpha = 0.2f)
                                                    )
                                                )
                                            } else {
                                                Brush.linearGradient(
                                                    listOf(
                                                        theme.accentPurple.copy(alpha = 0.3f),
                                                        Color.White.copy(alpha = 0.5f),
                                                        theme.accentPink.copy(alpha = 0.3f)
                                                    )
                                                )
                                            }
                                        )
                                        .blur(10.dp)
                                        .offset(x = 4.dp, y = 4.dp)
                                )
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(if (isDark) theme.glassSurface else Color.White.copy(alpha = 0.8f))
                                        .border(2.dp, if (isDark) theme.glassBorder else Color.White.copy(alpha = 0.9f), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = null,
                                        modifier = Modifier.size(20.dp),
                                        tint = theme.accentPurple
                                    )
                                }
                            }
                            Text(
                                text = "AI Matchmaker",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = theme.primaryText,
                                letterSpacing = (-0.5).sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(48.dp))
                }
            }

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(messages) { message ->
                    if (message.type == MessageType.AI) {
                        AIMessage(
                            message = message,
                            onOptionClick = { option ->
                                val userMsg = Message(
                                    id = System.currentTimeMillis().toString(),
                                    type = MessageType.USER,
                                    text = option
                                )
                                messages = messages + userMsg
                                pendingOption = option
                            },
                            onJoinActivity = onJoinActivity,
                            onViewProfile = onViewProfile,
                            appColors = theme
                        )
                    } else {
                        UserMessage(message, appColors = theme)
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        if (isDark) {
                            Brush.linearGradient(
                                colors = listOf(
                                    theme.glassSurface,
                                    theme.glassSurface.copy(alpha = 0.85f)
                                )
                            )
                        } else {
                            Brush.linearGradient(
                                colors = listOf(
                                    Color.White.copy(alpha = 0.7f),
                                    Color.White.copy(alpha = 0.5f)
                                )
                            )
                        }
                    )
                    .border(2.dp, if (isDark) theme.glassBorder else Color.White.copy(alpha = 0.8f))
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = inputValue,
                    onValueChange = { inputValue = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Ask me anything...", fontSize = 14.sp, color = theme.mutedText) },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = if (isDark) theme.glassSurface else Color.White.copy(alpha = 0.6f),
                        focusedContainerColor = if (isDark) theme.glassSurface.copy(alpha = 0.95f) else Color.White.copy(alpha = 0.8f),
                        unfocusedBorderColor = if (isDark) theme.glassBorder else Color.White.copy(alpha = 0.6f),
                        focusedBorderColor = theme.accentPurple.copy(alpha = if (isDark) 0.7f else 0.5f),
                        cursorColor = theme.primaryText,
                        focusedTextColor = theme.primaryText,
                        unfocusedTextColor = theme.primaryText
                    ),
                    shape = RoundedCornerShape(24.dp),
                    singleLine = true
                )

                Box {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(
                                        theme.accentPurple.copy(alpha = if (isDark) 0.25f else 0.2f),
                                        theme.accentPink.copy(alpha = if (isDark) 0.25f else 0.2f),
                                        theme.accentBlue.copy(alpha = if (isDark) 0.25f else 0.2f)
                                    )
                                )
                            )
                            .blur(8.dp)
                            .offset(x = 2.dp, y = 2.dp)
                    )
                    IconButton(
                        onClick = {
                            if (inputValue.isNotBlank()) {
                                val userMsg = Message(
                                    id = System.currentTimeMillis().toString(),
                                    type = MessageType.USER,
                                    text = inputValue
                                )
                                messages = messages + userMsg
                                pendingUserInput = inputValue
                                inputValue = ""
                            }
                        },
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    colors = if (isDark) {
                                        listOf(theme.accentPurple, theme.accentPink)
                                    } else {
                                        listOf(Color.White.copy(alpha = 0.7f), Color.White.copy(alpha = 0.5f))
                                    }
                                )
                            )
                            .border(2.dp, if (isDark) theme.glassBorder else Color.White.copy(alpha = 0.7f), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Send",
                            modifier = Modifier.size(16.dp),
                            tint = if (isDark) theme.iconOnAccent else Color.Black
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AIMessage(
    message: Message,
    onOptionClick: (String) -> Unit,
    onJoinActivity: (String) -> Unit,
    onViewProfile: (String) -> Unit,
    appColors: AppThemeColors
) {
    val isDark = appColors.isDark
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.Top
    ) {
        Box(modifier = Modifier.padding(end = 10.dp)) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                appColors.accentPurple.copy(alpha = if (isDark) 0.25f else 0.2f),
                                if (isDark) appColors.glassSurface else Color.White.copy(alpha = 0.4f),
                                appColors.accentPink.copy(alpha = if (isDark) 0.25f else 0.2f)
                            )
                        )
                    )
                    .blur(8.dp)
                    .offset(x = 2.dp, y = 2.dp)
            )
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(if (isDark) appColors.glassSurface else Color.White.copy(alpha = 0.7f))
                    .border(2.dp, if (isDark) appColors.glassBorder else Color.White.copy(alpha = 0.8f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = appColors.accentPurple
                )
            }
        }

        Column(modifier = Modifier.weight(1f)) {
            message.text?.let { body ->
                Box {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(24.dp))
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(
                                        appColors.accentPurple.copy(alpha = if (isDark) 0.18f else 0.1f),
                                        appColors.accentPink.copy(alpha = if (isDark) 0.18f else 0.1f),
                                        appColors.accentBlue.copy(alpha = if (isDark) 0.16f else 0.1f)
                                    )
                                )
                            )
                            .blur(8.dp)
                            .offset(x = 2.dp, y = 2.dp)
                    )
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isDark) appColors.glassSurface else Color.White.copy(alpha = 0.7f)
                        ),
                        border = BorderStroke(2.dp, if (isDark) appColors.glassBorder else Color.White.copy(alpha = 0.8f))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                                    .background(
                                        if (isDark) {
                                            Brush.verticalGradient(
                                                colors = listOf(
                                                    appColors.glassSurface.copy(alpha = 0.45f),
                                                    Color.Transparent
                                                )
                                            )
                                        } else {
                                            Brush.verticalGradient(
                                                colors = listOf(
                                                    Color.White.copy(alpha = 0.4f),
                                                    Color.Transparent
                                                )
                                            )
                                        }
                                    )
                                    .offset(y = (-12).dp)
                            )

                            Text(
                                text = body,
                                fontSize = 14.sp,
                                color = appColors.primaryText,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            }

            message.options?.let { options ->
                Row(
                    modifier = Modifier.padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    options.forEach { option ->
                        Box {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(24.dp))
                                    .background(
                                        Brush.linearGradient(
                                            colors = listOf(
                                                appColors.accentPurple.copy(alpha = if (isDark) 0.28f else 0.2f),
                                                appColors.accentPink.copy(alpha = if (isDark) 0.26f else 0.2f),
                                                appColors.accentBlue.copy(alpha = if (isDark) 0.24f else 0.2f)
                                            )
                                        )
                                    )
                                    .blur(8.dp)
                                    .offset(x = 2.dp, y = 2.dp)
                            )
                            Button(
                                onClick = { onOptionClick(option) },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                                shape = RoundedCornerShape(24.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .background(
                                            Brush.linearGradient(
                                                colors = listOf(
                                                    if (isDark) appColors.glassSurface.copy(alpha = 0.95f) else Color.White.copy(alpha = 0.7f),
                                                    if (isDark) appColors.glassSurface.copy(alpha = 0.8f) else Color.White.copy(alpha = 0.5f)
                                                )
                                            ),
                                            RoundedCornerShape(24.dp)
                                        )
                                        .border(2.dp, if (isDark) appColors.glassBorder else Color.White.copy(alpha = 0.7f), RoundedCornerShape(24.dp))
                                        .padding(horizontal = 16.dp, vertical = 8.dp)
                                ) {
                                    Text(
                                        text = option,
                                        color = appColors.primaryText,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
            }

            message.results?.let { results ->
                Column(
                    modifier = Modifier.padding(top = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    when (results.type) {
                        ResultType.PEOPLE -> {
                            (results.items as? List<PersonResult>)?.forEach { person ->
                                PersonResultCard(
                                    person = person,
                                    onViewProfile = { onViewProfile(person.id) },
                                    onConnect = { /* placeholder */ },
                                    appColors = appColors
                                )
                            }
                        }

                        ResultType.EVENTS -> {
                            (results.items as? List<EventResult>)?.forEach { event ->
                                EventResultCard(
                                    event = event,
                                    onJoinActivity = { onJoinActivity(event.id) },
                                    appColors = appColors
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
private fun UserMessage(message: Message, appColors: AppThemeColors) {
    val isDark = appColors.isDark
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        Box {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                appColors.accentPurple.copy(alpha = if (isDark) 0.28f else 0.2f),
                                appColors.accentPink.copy(alpha = if (isDark) 0.26f else 0.2f)
                            )
                        )
                    )
                    .blur(8.dp)
                    .offset(x = 2.dp, y = 2.dp)
            )
            Card(
                modifier = Modifier.widthIn(max = 280.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    appColors.accentPurple,
                                    appColors.accentPink
                                )
                            ),
                            RoundedCornerShape(24.dp)
                        )
                        .padding(12.dp)
                ) {
                    Text(
                        text = message.text.orEmpty(),
                        fontSize = 14.sp,
                        color = appColors.iconOnAccent
                    )
                }
            }
        }
    }
}

@Composable
private fun PersonResultCard(
    person: PersonResult,
    onViewProfile: () -> Unit,
    onConnect: () -> Unit,
    appColors: AppThemeColors
) {
    val isDark = appColors.isDark
    val cardGlow = Brush.linearGradient(
        colors = listOf(
            appColors.accentPurple.copy(alpha = if (isDark) 0.3f else 0.15f),
            appColors.accentPink.copy(alpha = if (isDark) 0.28f else 0.15f),
            appColors.accentBlue.copy(alpha = if (isDark) 0.25f else 0.15f)
        )
    )
    val cardSurface = if (isDark) appColors.glassSurface else Color.White.copy(alpha = 0.7f)
    val cardBorder = if (isDark) appColors.glassBorder else Color.White.copy(alpha = 0.8f)
    val primaryText = appColors.primaryText
    val secondaryText = appColors.secondaryText
    val mutedText = appColors.mutedText
    Box {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(cardGlow)
                .blur(12.dp)
                .offset(x = 2.dp, y = 2.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = cardSurface),
            border = BorderStroke(2.dp, cardBorder)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.White.copy(alpha = 0.4f),
                                    Color.Transparent
                                )
                            )
                        )
                        .offset(y = (-12).dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AsyncImage(
                        model = person.avatar,
                        contentDescription = null,
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .border(2.dp, cardBorder, CircleShape)
                    )

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = person.name,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = primaryText
                            )
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color.Transparent,
                                modifier = Modifier
                                    .background(
                                        Brush.horizontalGradient(
                                            colors = listOf(
                                                Color(0xFFA855F7),
                                                Color(0xFFEC4899)
                                            )
                                        ),
                                        RoundedCornerShape(12.dp)
                                    )
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "${person.matchScore}% match",
                                    fontSize = 10.sp,
                                    color = appColors.iconOnAccent,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Text(
                            text = person.sport,
                            fontSize = 12.sp,
                            color = secondaryText,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                        Text(
                            text = person.bio,
                            fontSize = 12.sp,
                            color = mutedText,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = mutedText
                        )
                        Text(
                            text = person.distance,
                            fontSize = 12.sp,
                            color = mutedText
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.FlashOn,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = appColors.success
                        )
                        Text(
                            text = person.availability,
                            fontSize = 12.sp,
                            color = appColors.success
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onViewProfile,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(
                                            if (isDark) appColors.glassSurface.copy(alpha = 0.95f) else Color.White.copy(alpha = 0.6f),
                                            if (isDark) appColors.glassSurface.copy(alpha = 0.85f) else Color.White.copy(alpha = 0.5f)
                                        )
                                    ),
                                    RoundedCornerShape(24.dp)
                                )
                                .border(2.dp, if (isDark) appColors.glassBorder else Color.White.copy(alpha = 0.6f), RoundedCornerShape(24.dp))
                                .padding(vertical = 8.dp)
                        ) {
                            Text(
                                text = "View Profile",
                                color = primaryText,
                                fontSize = 13.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Button(
                        onClick = onConnect,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(
                                            Color(0xFFA855F7),
                                            Color(0xFFEC4899)
                                        )
                                    ),
                                    RoundedCornerShape(24.dp)
                                )
                                .padding(vertical = 8.dp)
                        ) {
                            Text(
                                text = "Connect",
                                color = appColors.iconOnAccent,
                                fontSize = 13.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EventResultCard(
    event: EventResult,
    onJoinActivity: () -> Unit,
    appColors: AppThemeColors
) {
    val isDark = appColors.isDark
    val cardGlow = Brush.linearGradient(
        colors = listOf(
            appColors.success.copy(alpha = if (isDark) 0.26f else 0.2f),
            appColors.accentTeal.copy(alpha = if (isDark) 0.22f else 0.15f)
        )
    )
    val cardSurface = if (isDark) appColors.glassSurface else Color.White.copy(alpha = 0.7f)
    val cardBorder = if (isDark) appColors.glassBorder else Color.White.copy(alpha = 0.8f)
    val primaryText = appColors.primaryText
    val mutedText = appColors.mutedText
    Box {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(cardGlow)
                .blur(12.dp)
                .offset(x = 2.dp, y = 2.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = cardSurface),
            border = BorderStroke(2.dp, cardBorder)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.White.copy(alpha = 0.4f),
                                    Color.Transparent
                                )
                            )
                        )
                        .offset(y = (-12).dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isDark) appColors.glassSurface else Color.White.copy(alpha = 0.6f))
                            .border(2.dp, if (isDark) appColors.glassBorder else Color.White.copy(alpha = 0.6f), RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = event.sportIcon, fontSize = 18.sp)
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = event.title,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = primaryText
                            )
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color.Transparent,
                                modifier = Modifier
                                    .background(
                                        Brush.horizontalGradient(
                                            colors = listOf(
                                                Color(0xFFA855F7),
                                                Color(0xFFEC4899)
                                            )
                                        ),
                                        RoundedCornerShape(12.dp)
                                    )
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "${event.matchScore}% match",
                                    fontSize = 10.sp,
                                    color = appColors.iconOnAccent,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.padding(top = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = mutedText
                            )
                            Text(
                                text = "${event.date} • ${event.time}",
                                fontSize = 12.sp,
                                color = mutedText
                            )
                        }

                        Row(
                            modifier = Modifier.padding(top = 2.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = mutedText
                            )
                            Text(
                                text = "${event.location} • ${event.distance}",
                                fontSize = 12.sp,
                                color = mutedText
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Group,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = appColors.secondaryText
                        )
                        Text(
                            text = "${event.participants}/${event.maxParticipants} joined",
                            fontSize = 12.sp,
                            color = appColors.secondaryText
                        )
                    }
                }

                Button(
                    onClick = onJoinActivity,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(appColors.success, appColors.accentTeal)
                                ),
                                RoundedCornerShape(24.dp)
                            )
                            .padding(vertical = 8.dp)
                    ) {
                        Text(
                            text = "Join Activity",
                            color = appColors.iconOnAccent,
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

private fun generateAIResponse(option: String): Message {
    return when {
        option.contains("running partner", ignoreCase = true) -> {
            Message(
                id = System.currentTimeMillis().toString(),
                type = MessageType.AI,
                text = "Great! I found 3 runners near you who are free this evening. They match your pace and skill level.",
                results = MessageResults(
                    type = ResultType.PEOPLE,
                    items = listOf(
                        PersonResult(
                            id = "1",
                            name = "Sarah M.",
                            avatar = "https://api.dicebear.com/7.x/avataaars/svg?seed=Sarah",
                            sport = "🏃 Running",
                            distance = "0.8 mi away",
                            matchScore = 95,
                            bio = "Marathon runner, looking for morning run buddies",
                            availability = "Free today 6PM"
                        ),
                        PersonResult(
                            id = "2",
                            name = "Mike R.",
                            avatar = "https://api.dicebear.com/7.x/avataaars/svg?seed=Mike",
                            sport = "🏃 Running",
                            distance = "1.2 mi away",
                            matchScore = 88,
                            bio = "Trail running enthusiast",
                            availability = "Free today 7PM"
                        )
                    )
                )
            )
        }

        option.contains("group activity", ignoreCase = true) -> {
            Message(
                id = System.currentTimeMillis().toString(),
                type = MessageType.AI,
                text = "I found some popular group activities happening near you this week:",
                results = MessageResults(
                    type = ResultType.EVENTS,
                    items = listOf(
                        EventResult(
                            id = "event-1",
                            title = "Beach Volleyball Meetup",
                            sportIcon = "🏐",
                            date = "Nov 5, 2025",
                            time = "5:00 PM",
                            location = "Venice Beach",
                            distance = "2.1 mi",
                            participants = 8,
                            maxParticipants = 12,
                            matchScore = 92
                        ),
                        EventResult(
                            id = "event-2",
                            title = "Morning Yoga Flow",
                            sportIcon = "🧘",
                            date = "Nov 6, 2025",
                            time = "7:00 AM",
                            location = "Sunset Park",
                            distance = "1.5 mi",
                            participants = 6,
                            maxParticipants = 15,
                            matchScore = 85
                        )
                    )
                )
            )
        }

        else -> {
            Message(
                id = System.currentTimeMillis().toString(),
                type = MessageType.AI,
                text = "Based on your profile, here are some sports you might enjoy:",
                options = listOf("Swimming", "Tennis", "Cycling", "Yoga")
            )
        }
    }
}

@Composable
private fun AIMatchmakerOrb(
    size: androidx.compose.ui.unit.Dp,
    color: Color,
    top: androidx.compose.ui.unit.Dp? = null,
    start: androidx.compose.ui.unit.Dp? = null,
    end: androidx.compose.ui.unit.Dp? = null,
    bottom: androidx.compose.ui.unit.Dp? = null,
    center: Boolean = false,
    pulseDurationMs: Int,
    startDelayMs: Int = 0
) {
    val infinite = rememberInfiniteTransition(label = "aimatchmaker-orb")
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
        var orbModifier = Modifier
            .size(size * scale)
            .blur(48.dp)
            .background(color = color, shape = CircleShape)

        orbModifier = if (center) {
            orbModifier.align(Alignment.Center)
        } else {
            val alignment = when {
                bottom != null && end != null -> Alignment.BottomEnd
                bottom != null -> Alignment.BottomStart
                end != null -> Alignment.TopEnd
                else -> Alignment.TopStart
            }
            orbModifier.align(alignment)
        }

        top?.let { orbModifier = orbModifier.offset(y = it) }
        start?.let { orbModifier = orbModifier.offset(x = it) }
        end?.let { orbModifier = orbModifier.offset(x = (-it)) }
        bottom?.let { orbModifier = orbModifier.offset(y = (-it)) }

        Box(modifier = orbModifier)
    }
}
