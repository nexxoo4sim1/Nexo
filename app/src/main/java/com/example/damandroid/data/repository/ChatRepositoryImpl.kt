package com.example.damandroid.data.repository

import com.example.damandroid.data.datasource.ChatRemoteDataSource
import com.example.damandroid.data.mapper.toDomain
import com.example.damandroid.domain.model.ChatPreview
import com.example.damandroid.domain.repository.ChatRepository

class ChatRepositoryImpl(
    private val remoteDataSource: ChatRemoteDataSource
) : ChatRepository {
    override suspend fun getChats(): List<ChatPreview> =
        remoteDataSource.fetchChats().map { it.toDomain() }

    override suspend fun searchChats(query: String): List<ChatPreview> =
        getChats().filter { chat ->
            chat.name.contains(query, ignoreCase = true) ||
                chat.lastMessage.contains(query, ignoreCase = true)
        }
}

