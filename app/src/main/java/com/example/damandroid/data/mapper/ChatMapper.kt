package com.example.damandroid.data.mapper

import com.example.damandroid.data.model.ChatPreviewDto
import com.example.damandroid.domain.model.ChatPreview

fun ChatPreviewDto.toDomain(): ChatPreview = ChatPreview(
    id = id,
    name = name,
    lastMessage = lastMessage,
    timestamp = timestamp,
    unreadCount = unreadCount,
    avatarUrl = avatarUrl,
    isOnline = isOnline
)

