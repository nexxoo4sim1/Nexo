package com.example.damandroid.domain.model

data class ChatPreview(
    val id: String,
    val name: String,
    val lastMessage: String,
    val timestamp: String,
    val unreadCount: Int,
    val avatarUrl: String,
    val isOnline: Boolean
)

