package com.example.damandroid.data.mapper

import com.example.damandroid.data.model.CreateActivityRequestDto
import com.example.damandroid.data.model.CreateActivityResponseDto
import com.example.damandroid.data.model.SportCategoryDto
import com.example.damandroid.domain.model.ActivityVisibility
import com.example.damandroid.domain.model.CreateActivityForm
import com.example.damandroid.domain.model.CreateActivityResult
import com.example.damandroid.domain.model.SkillLevel
import com.example.damandroid.domain.model.SportCategory

fun SportCategoryDto.toCreateActivityDomain(): SportCategory = SportCategory(
    id = id,
    name = name,
    icon = icon
)

fun CreateActivityForm.toDto(): CreateActivityRequestDto = CreateActivityRequestDto(
    sportCategoryId = sportType?.id ?: "",
    title = title,
    description = description,
    location = location,
    date = date,
    time = time,
    participants = participants,
    level = (level ?: SkillLevel.BEGINNER).name.lowercase(),
    visibility = visibility.name.lowercase()
)

fun CreateActivityResponseDto.toDomain(): CreateActivityResult = CreateActivityResult(
    activityId = activityId,
    shareLink = shareLink
)

