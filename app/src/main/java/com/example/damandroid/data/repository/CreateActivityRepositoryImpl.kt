package com.example.damandroid.data.repository

import com.example.damandroid.data.datasource.CreateActivityRemoteDataSource
import com.example.damandroid.data.mapper.toDomain
import com.example.damandroid.data.mapper.toDto
import com.example.damandroid.domain.model.CreateActivityForm
import com.example.damandroid.domain.model.CreateActivityResult
import com.example.damandroid.domain.model.SportCategory
import com.example.damandroid.domain.repository.CreateActivityRepository

class CreateActivityRepositoryImpl(
    private val remoteDataSource: CreateActivityRemoteDataSource
) : CreateActivityRepository {
    override suspend fun getSportCategories(): List<SportCategory> =
        remoteDataSource.fetchSportCategories().map { it.toDomain() }

    override suspend fun createActivity(form: CreateActivityForm): CreateActivityResult =
        remoteDataSource.createActivity(form.toDto()).toDomain()
}

