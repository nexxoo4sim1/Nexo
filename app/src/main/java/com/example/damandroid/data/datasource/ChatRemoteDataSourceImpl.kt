package com.example.damandroid.data.datasource

import com.example.damandroid.data.model.ChatPreviewDto

class ChatRemoteDataSourceImpl : ChatRemoteDataSource {
    override suspend fun fetchChats(): List<ChatPreviewDto> = listOf(
        ChatPreviewDto(
            id = "1",
            name = "Alex Thompson",
            lastMessage = "See you at the court tomorrow!",
            timestamp = "2m ago",
            unreadCount = 2,
            avatarUrl = "https://api.dicebear.com/7.x/avataaars/svg?seed=alex",
            isOnline = true
        ),
        ChatPreviewDto(
            id = "2",
            name = "Jessica Lee",
            lastMessage = "Thanks for the session 💪",
            timestamp = "1h ago",
            unreadCount = 0,
            avatarUrl = "https://api.dicebear.com/7.x/avataaars/svg?seed=jessica",
            isOnline = false
        ),
        ChatPreviewDto(
            id = "3",
            name = "Running Club",
            lastMessage = "Reminder: Sunday morning run!",
            timestamp = "3h ago",
            unreadCount = 5,
            avatarUrl = "https://api.dicebear.com/7.x/identicon/svg?seed=runningclub",
            isOnline = true
        )
    )
}

