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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import coil.compose.AsyncImage
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
fun AIMatchmaker(
    onBack: () -> Unit,
    onJoinActivity: (String) -> Unit = {},
    onViewProfile: (String) -> Unit = {}
) {
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

    // Handle pending option click
    LaunchedEffect(pendingOption) {
        pendingOption?.let { option ->
            delay(800)
            val aiResponse = generateAIResponse(option)
            messages = messages + aiResponse
            pendingOption = null
        }
    }

    // Handle pending user input
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
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFF5F6F8),
                        Color(0xFFF0F2F5),
                        Color(0xFFEBEDF0)
                    )
                )
            )
    ) {
        // Floating Orbs
        AIMatchmakerOrb(
            size = 128.dp,
            color = Color(0xFF8B5CF6).copy(alpha = 0.30f),
            top = 80.dp,
            start = 40.dp,
            pulseDurationMs = 1600
        )
        AIMatchmakerOrb(
            size = 160.dp,
            color = Color(0xFFEC4899).copy(alpha = 0.35f),
            bottom = 160.dp,
            end = 40.dp,
            pulseDurationMs = 1600,
            startDelayMs = 1000
        )
        AIMatchmakerOrb(
            size = 96.dp,
            color = Color(0xFF0066FF).copy(alpha = 0.30f),
            center = true,
            pulseDurationMs = 1600,
            startDelayMs = 2000
        )
        AIMatchmakerOrb(
            size = 80.dp,
            color = Color(0xFF2ECC71).copy(alpha = 0.25f),
            top = 120.dp,
            end = 80.dp,
            pulseDurationMs = 1600,
            startDelayMs = 3000
        )

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.7f),
                                Color.White.copy(alpha = 0.5f)
                            )
                        )
                    )
                    .border(2.dp, Color.White.copy(alpha = 0.8f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
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
                            onClick = onBack,
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.Black,
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
                                            Brush.linearGradient(
                                                colors = listOf(
                                                    Color(0xFF8B5CF6).copy(alpha = 0.3f),
                                                    Color.White.copy(alpha = 0.5f),
                                                    Color(0xFFEC4899).copy(alpha = 0.3f)
                                                )
                                            )
                                        )
                                        .blur(10.dp)
                                        .offset(x = 4.dp, y = 4.dp)
                                )
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(Color.White.copy(alpha = 0.8f))
                                        .border(2.dp, Color.White.copy(alpha = 0.9f), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = null,
                                        modifier = Modifier.size(20.dp),
                                        tint = Color(0xFF8B5CF6)
                                    )
                                }
                            }
                            Text(
                                text = "AI Matchmaker",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1A202C),
                                letterSpacing = (-0.5).sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(48.dp))
                }
            }

            // Messages
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
                            onViewProfile = onViewProfile
                        )
                    } else {
                        UserMessage(message = message)
                    }
                }
            }

            // Input
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.7f),
                                Color.White.copy(alpha = 0.5f)
                            )
                        )
                    )
                    .border(2.dp, Color.White.copy(alpha = 0.8f))
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = inputValue,
                    onValueChange = { inputValue = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Ask me anything...", fontSize = 14.sp) },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White.copy(alpha = 0.6f),
                        focusedContainerColor = Color.White.copy(alpha = 0.8f),
                        unfocusedBorderColor = Color.White.copy(alpha = 0.6f),
                        focusedBorderColor = Color(0xFF8B5CF6).copy(alpha = 0.5f)
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
                                        Color(0xFF8B5CF6).copy(alpha = 0.2f),
                                        Color(0xFFEC4899).copy(alpha = 0.2f),
                                        Color(0xFF0066FF).copy(alpha = 0.2f)
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
                                    colors = listOf(
                                        Color.White.copy(alpha = 0.7f),
                                        Color.White.copy(alpha = 0.5f)
                                    )
                                )
                            )
                            .border(2.dp, Color.White.copy(alpha = 0.7f), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Send",
                            modifier = Modifier.size(16.dp),
                            tint = Color.Black
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
    onViewProfile: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier.padding(end = 10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF8B5CF6).copy(alpha = 0.2f),
                                Color.White.copy(alpha = 0.4f),
                                Color(0xFFEC4899).copy(alpha = 0.2f)
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
                    .background(Color.White.copy(alpha = 0.7f))
                    .border(2.dp, Color.White.copy(alpha = 0.8f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = Color(0xFF8B5CF6)
                )
            }
        }

        Column(
            modifier = Modifier.weight(1f)
        ) {
            if (message.text != null) {
                Box {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(24.dp))
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(
                                        Color(0xFF8B5CF6).copy(alpha = 0.1f),
                                        Color(0xFFEC4899).copy(alpha = 0.1f),
                                        Color(0xFF0066FF).copy(alpha = 0.1f)
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
                            containerColor = Color.White.copy(alpha = 0.7f)
                        ),
                        border = BorderStroke(2.dp, Color.White.copy(alpha = 0.8f))
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
                                        Brush.verticalGradient(
                                            colors = listOf(
                                                Color.White.copy(alpha = 0.4f),
                                                Color.Transparent
                                            )
                                        )
                                    )
                                    .offset(y = (-12).dp)
                            )
                            Text(
                                text = message.text,
                                fontSize = 14.sp,
                                color = Color.Black,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            }

            // Options
            if (message.options != null) {
                Row(
                    modifier = Modifier.padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    message.options.forEach { option ->
                        Box {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(24.dp))
                                    .background(
                                        Brush.linearGradient(
                                            colors = listOf(
                                                Color(0xFF8B5CF6).copy(alpha = 0.2f),
                                                Color(0xFFEC4899).copy(alpha = 0.2f),
                                                Color(0xFF0066FF).copy(alpha = 0.2f)
                                            )
                                        )
                                    )
                                    .blur(8.dp)
                                    .offset(x = 2.dp, y = 2.dp)
                            )
                            Button(
                                onClick = { onOptionClick(option) },
                                modifier = Modifier,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Transparent
                                ),
                                shape = RoundedCornerShape(24.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .background(
                                            Brush.linearGradient(
                                                colors = listOf(
                                                    Color.White.copy(alpha = 0.7f),
                                                    Color.White.copy(alpha = 0.5f)
                                                )
                                            ),
                                            RoundedCornerShape(24.dp)
                                        )
                                        .border(2.dp, Color.White.copy(alpha = 0.7f), RoundedCornerShape(24.dp))
                                        .padding(horizontal = 16.dp, vertical = 8.dp)
                                ) {
                                    Text(
                                        text = option,
                                        color = Color.Black,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Results
            if (message.results != null) {
                Column(
                    modifier = Modifier.padding(top = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    when (message.results.type) {
                        ResultType.PEOPLE -> {
                            (message.results.items as? List<PersonResult>)?.forEach { person ->
                                PersonResultCard(
                                    person = person,
                                    onViewProfile = { onViewProfile(person.id) },
                                    onConnect = { /* TODO */ }
                                )
                            }
                        }
                        ResultType.EVENTS -> {
                            (message.results.items as? List<EventResult>)?.forEach { event ->
                                EventResultCard(
                                    event = event,
                                    onJoinActivity = { onJoinActivity(event.id) }
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
private fun UserMessage(message: Message) {
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
                                Color(0xFF8B5CF6).copy(alpha = 0.2f),
                                Color(0xFFEC4899).copy(alpha = 0.2f)
                            )
                        )
                    )
                    .blur(8.dp)
                    .offset(x = 2.dp, y = 2.dp)
            )
            Card(
                modifier = Modifier.widthIn(max = 280.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent
                )
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFFA855F7),
                                    Color(0xFFEC4899)
                                )
                            ),
                            RoundedCornerShape(24.dp)
                        )
                        .padding(12.dp)
                ) {
                    Text(
                        text = message.text ?: "",
                        fontSize = 14.sp,
                        color = Color.White
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
    onConnect: () -> Unit
) {
    Box {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF8B5CF6).copy(alpha = 0.15f),
                            Color(0xFFEC4899).copy(alpha = 0.15f),
                            Color(0xFF0066FF).copy(alpha = 0.15f)
                        )
                    )
                )
                .blur(12.dp)
                .offset(x = 2.dp, y = 2.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White.copy(alpha = 0.7f)
            ),
            border = BorderStroke(2.dp, Color.White.copy(alpha = 0.8f))
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
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
                            .border(2.dp, Color.White.copy(alpha = 0.6f), CircleShape)
                    )

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = person.name,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black
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
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Text(
                            text = person.sport,
                            fontSize = 12.sp,
                            color = Color.Black.copy(alpha = 0.7f),
                            modifier = Modifier.padding(top = 2.dp)
                        )
                        Text(
                            text = person.bio,
                            fontSize = 12.sp,
                            color = Color.Black.copy(alpha = 0.6f),
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
                            tint = Color.Black.copy(alpha = 0.6f)
                        )
                        Text(
                            text = person.distance,
                            fontSize = 12.sp,
                            color = Color.Black.copy(alpha = 0.6f)
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
                            tint = Color(0xFF2ECC71)
                        )
                        Text(
                            text = person.availability,
                            fontSize = 12.sp,
                            color = Color(0xFF2ECC71)
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
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(
                                            Color.White.copy(alpha = 0.6f),
                                            Color.White.copy(alpha = 0.5f)
                                        )
                                    ),
                                    RoundedCornerShape(24.dp)
                                )
                                .border(2.dp, Color.White.copy(alpha = 0.6f), RoundedCornerShape(24.dp))
                                .padding(vertical = 8.dp)
                        ) {
                            Text(
                                text = "View Profile",
                                color = Color.Black,
                                fontSize = 13.sp
                            )
                        }
                    }

                    Button(
                        onClick = onConnect,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent
                        ),
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
                                color = Color.White,
                                fontSize = 13.sp
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
    onJoinActivity: () -> Unit
) {
    Box {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF2ECC71).copy(alpha = 0.2f),
                            Color(0xFF10B981).copy(alpha = 0.15f)
                        )
                    )
                )
                .blur(12.dp)
                .offset(x = 2.dp, y = 2.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White.copy(alpha = 0.7f)
            ),
            border = BorderStroke(2.dp, Color.White.copy(alpha = 0.8f))
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
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
                            .background(Color.White.copy(alpha = 0.6f))
                            .border(2.dp, Color.White.copy(alpha = 0.6f), RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = event.sportIcon,
                            fontSize = 18.sp
                        )
                    }

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = event.title,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black
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
                                    color = Color.White,
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
                                tint = Color.Black.copy(alpha = 0.6f)
                            )
                            Text(
                                text = "${event.date} • ${event.time}",
                                fontSize = 12.sp,
                                color = Color.Black.copy(alpha = 0.6f)
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
                                tint = Color.Black.copy(alpha = 0.6f)
                            )
                            Text(
                                text = "${event.location} • ${event.distance}",
                                fontSize = 12.sp,
                                color = Color.Black.copy(alpha = 0.6f)
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
                            tint = Color.Black.copy(alpha = 0.7f)
                        )
                        Text(
                            text = "${event.participants}/${event.maxParticipants} joined",
                            fontSize = 12.sp,
                            color = Color.Black.copy(alpha = 0.7f)
                        )
                    }
                }

                Button(
                    onClick = onJoinActivity,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Color(0xFF2ECC71),
                                RoundedCornerShape(24.dp)
                            )
                            .padding(vertical = 8.dp)
                    ) {
                        Text(
                            text = "Join Activity",
                            color = Color.White,
                            fontSize = 13.sp
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
                            id = "1",
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
                            id = "2",
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
    startDelayMs: Int = 0,
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

