package com.example.damandroid.domain.repository

import com.example.damandroid.domain.model.ChatPreview

interface ChatRepository {
    suspend fun getChats(): List<ChatPreview>
    suspend fun searchChats(query: String): List<ChatPreview>
}

