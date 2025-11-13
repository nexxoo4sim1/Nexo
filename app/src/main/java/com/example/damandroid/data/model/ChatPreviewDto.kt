package com.example.damandroid.data.model

data class ChatPreviewDto(
    val id: String,
    val name: String,
    val lastMessage: String,
    val timestamp: String,
    val unreadCount: Int,
    val avatarUrl: String,
    val isOnline: Boolean
)

