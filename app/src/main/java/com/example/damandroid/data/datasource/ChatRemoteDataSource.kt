package com.example.damandroid.data.datasource

import com.example.damandroid.data.model.ChatPreviewDto

interface ChatRemoteDataSource {
    suspend fun fetchChats(): List<ChatPreviewDto>
}

