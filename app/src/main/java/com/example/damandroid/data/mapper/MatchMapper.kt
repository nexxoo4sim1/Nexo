package com.example.damandroid.data.mapper

import com.example.damandroid.data.model.MatchUserProfileDto
import com.example.damandroid.data.model.SportDto
import com.example.damandroid.domain.model.MatchUserProfile
import com.example.damandroid.domain.model.Sport

fun MatchUserProfileDto.toDomain(): MatchUserProfile = MatchUserProfile(
    id = id,
    name = name,
    age = age,
    avatarUrl = avatarUrl,
    coverImageUrl = coverImageUrl,
    location = location,
    distance = distance,
    bio = bio,
    sports = sports.map(SportDto::toDomain),
    interests = interests,
    rating = rating,
    activitiesJoined = activitiesJoined
)

private fun SportDto.toDomain(): Sport = Sport(
    name = name,
    icon = icon,
    level = level
)

