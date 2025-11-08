package com.example.damandroid

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.damandroid.ui.theme.DamAndroidTheme
import com.example.damandroid.ui.theme.LocalThemeController

data class Chat(
    val id: String,
    val participantNames: String,
    val participantAvatars: List<String>,
    val lastMessage: String,
    val lastMessageTime: String,
    val unreadCount: Int,
    val isGroup: Boolean = false
)

val mockChats = listOf(
    Chat(
        id = "1",
        participantNames = "Swimming Group",
        participantAvatars = listOf(
            "https://i.pravatar.cc/400?img=12",
            "https://i.pravatar.cc/400?img=15"
        ),
        lastMessage = "See you tomorrow at 7!",
        lastMessageTime = "2m ago",
        unreadCount = 2,
        isGroup = true
    ),
    Chat(
        id = "2",
        participantNames = "Mike Johnson",
        participantAvatars = listOf("https://i.pravatar.cc/400?img=25"),
        lastMessage = "Thanks for joining the game!",
        lastMessageTime = "1h ago",
        unreadCount = 0
    ),
    Chat(
        id = "3",
        participantNames = "Emma Davis",
        participantAvatars = listOf("https://i.pravatar.cc/400?img=40"),
        lastMessage = "What time should we meet?",
        lastMessageTime = "3h ago",
        unreadCount = 1
    )
)

@Stable
private data class ChatListThemeColors(
    val isDark: Boolean,
    val backgroundGradient: List<Color>,
    val headerText: Color,
    val searchText: Color,
    val searchPlaceholder: Color,
    val searchIcon: Color,
    val searchContainer: Color,
    val chatName: Color,
    val chatTime: Color,
    val chatMessage: Color,
    val chatChevron: Color,
    val chatCardBackground: Color,
    val chatCardBorder: Color,
    val avatarBorder: Color
)

@Composable
private fun rememberChatListThemeColors(): ChatListThemeColors {
    val isDark = LocalThemeController.current.isDarkMode
    return remember(isDark) {
        if (isDark) {
            ChatListThemeColors(
                isDark = true,
                backgroundGradient = listOf(
                    Color(0xFF0F172A),
                    Color(0xFF1E1B4B),
                    Color(0xFF111827)
                ),
                headerText = Color(0xFFE2E8F0),
                searchText = Color(0xFFF8FAFC),
                searchPlaceholder = Color(0xFF94A3B8),
                searchIcon = Color(0xFF94A3B8),
                searchContainer = Color(0xFF1F2937),
                chatName = Color(0xFFE2E8F0),
                chatTime = Color(0xFF94A3B8),
                chatMessage = Color(0xFFCBD5F5).copy(alpha = 0.85f),
                chatChevron = Color(0xFF64748B),
                chatCardBackground = Color(0xFF1F2937),
                chatCardBorder = Color(0xFF334155),
                avatarBorder = Color(0xFF111827)
            )
        } else {
            ChatListThemeColors(
                isDark = false,
                backgroundGradient = listOf(
                    Color(0xFFE8D5F2),
                    Color(0xFFFFE4F1),
                    Color(0xFFE5E5F0)
                ),
                headerText = Color(0xFF111827),
                searchText = Color(0xFF111827),
                searchPlaceholder = Color(0xFF9CA3AF),
                searchIcon = Color(0xFF9CA3AF),
                searchContainer = Color.White.copy(alpha = 0.9f),
                chatName = Color(0xFF111827),
                chatTime = Color(0xFF6B7280),
                chatMessage = Color(0xFF4B5563),
                chatChevron = Color(0xFF9CA3AF),
                chatCardBackground = Color.White,
                chatCardBorder = Color.Transparent,
                avatarBorder = Color.White
            )
        }
    }
}

@Composable
fun ChatList(
    onChatSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    val colors = rememberChatListThemeColors()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = colors.backgroundGradient
                )
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                Text(
                    text = "Messages",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.headerText,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                // Search Input
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = {
                        Text(
                            "Search conversations...",
                            color = colors.searchPlaceholder,
                            fontSize = 15.sp
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = colors.searchIcon,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = colors.searchText,
                        unfocusedTextColor = colors.searchText,
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        cursorColor = colors.searchText,
                        focusedContainerColor = colors.searchContainer,
                        unfocusedContainerColor = colors.searchContainer,
                    ),
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyMedium.copy(fontSize = 15.sp)
                )
            }

            // Chat List
            val filteredChats = mockChats.filter {
                it.participantNames.contains(searchQuery, ignoreCase = true) ||
                it.lastMessage.contains(searchQuery, ignoreCase = true)
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    top = 0.dp,
                    end = 16.dp,
                    bottom = 100.dp
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = filteredChats,
                    key = { it.id }
                ) { chat ->
                    ChatItem(
                        chat = chat,
                        onClick = { onChatSelect(chat.id) },
                        colors = colors
                    )
                }
            }
        }
    }
}


@Composable
private fun ChatItem(
    chat: Chat,
    onClick: () -> Unit,
    colors: ChatListThemeColors
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .border(
                width = if (colors.chatCardBorder == Color.Transparent) 0.dp else 1.dp,
                color = colors.chatCardBorder,
                shape = RoundedCornerShape(16.dp)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.chatCardBackground
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Avatar(s)
            Box(
                modifier = Modifier.size(48.dp)
            ) {
                if (chat.isGroup && chat.participantAvatars.size > 1) {
                    // Group avatar - overlapping avatars
                    AsyncImage(
                        model = chat.participantAvatars[0],
                        contentDescription = null,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .border(2.dp, colors.avatarBorder, CircleShape)
                            .align(Alignment.TopStart)
                    )
                    AsyncImage(
                        model = chat.participantAvatars[1],
                        contentDescription = null,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .border(2.dp, colors.avatarBorder, CircleShape)
                            .align(Alignment.BottomEnd)
                    )
                } else {
                    // Single avatar
                    AsyncImage(
                        model = chat.participantAvatars[0],
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                    )
                }
            }

            // Content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = chat.participantNames,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.chatName,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = chat.lastMessageTime,
                        fontSize = 12.sp,
                        color = colors.chatTime,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = chat.lastMessage,
                        fontSize = 14.sp,
                        color = colors.chatMessage,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    
                    // Unread Badge and Chevron
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (chat.unreadCount > 0) {
                            Surface(
                                modifier = Modifier.size(24.dp),
                                shape = CircleShape,
                                color = Color(0xFF86EFAC) // bg-[#86EFAC] - bright green
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = if (chat.unreadCount > 99) "99+" else chat.unreadCount.toString(),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint = colors.chatChevron,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ChatListPreview() {
    DamAndroidTheme {
        ChatList(
            onChatSelect = { }
        )
    }
}

