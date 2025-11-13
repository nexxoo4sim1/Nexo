package com.example.damandroid.data.mapper

import com.example.damandroid.data.model.HomeActivityDto
import com.example.damandroid.data.model.HomeFeedDto
import com.example.damandroid.data.model.SportCategoryDto
import com.example.damandroid.domain.model.HomeActivity
import com.example.damandroid.domain.model.HomeFeed
import com.example.damandroid.domain.model.SportCategory

fun HomeFeedDto.toDomain(): HomeFeed = HomeFeed(
    activities = activities.map { it.toDomain() },
    sportCategories = sportCategories.map { it.toDomain() }
)

fun HomeActivityDto.toDomain(): HomeActivity = HomeActivity(
    id = id,
    title = title,
    sportType = sportType,
    sportIcon = sportIcon,
    hostName = hostName,
    hostAvatar = hostAvatar,
    date = date,
    time = time,
    location = location,
    distance = distance,
    spotsTotal = spotsTotal,
    spotsTaken = spotsTaken,
    level = level,
    isSaved = isSaved
)

fun SportCategoryDto.toDomain(): SportCategory = SportCategory(
    id = id,
    name = name,
    icon = icon
)

