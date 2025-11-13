package com.example.damandroid.data.datasource

import com.example.damandroid.data.model.HomeActivityDto
import com.example.damandroid.data.model.HomeFeedDto
import com.example.damandroid.data.model.SportCategoryDto

class HomeFeedRemoteDataSourceImpl : HomeFeedRemoteDataSource {

    override suspend fun fetchHomeFeed(): HomeFeedDto {
        val activities = listOf(
            HomeActivityDto(
                id = "activity-1",
                title = "Morning Run Group",
                sportType = "Running",
                sportIcon = "🏃",
                hostName = "Sarah Williams",
                hostAvatar = "https://i.pravatar.cc/400?img=32",
                date = "Today",
                time = "7:00 AM",
                location = "Central Park",
                distance = "1.2 mi",
                spotsTotal = 20,
                spotsTaken = 12,
                level = "All Levels",
                isSaved = true
            ),
            HomeActivityDto(
                id = "activity-2",
                title = "Sunset Yoga on the Pier",
                sportType = "Yoga",
                sportIcon = "🧘",
                hostName = "Emma Clark",
                hostAvatar = "https://i.pravatar.cc/400?img=47",
                date = "Today",
                time = "6:30 PM",
                location = "Seaside Pier",
                distance = "2.8 mi",
                spotsTotal = 16,
                spotsTaken = 9,
                level = "Beginner Friendly",
                isSaved = false
            ),
            HomeActivityDto(
                id = "activity-3",
                title = "Pickup Basketball Night",
                sportType = "Basketball",
                sportIcon = "🏀",
                hostName = "Mike Johnson",
                hostAvatar = "https://i.pravatar.cc/400?img=18",
                date = "Tomorrow",
                time = "8:00 PM",
                location = "Downtown Court",
                distance = "0.9 mi",
                spotsTotal = 10,
                spotsTaken = 6,
                level = "Intermediate",
                isSaved = false
            ),
            HomeActivityDto(
                id = "activity-4",
                title = "Weekend Cycling Club",
                sportType = "Cycling",
                sportIcon = "🚴",
                hostName = "Nate Alvarez",
                hostAvatar = "https://i.pravatar.cc/400?img=60",
                date = "Saturday",
                time = "9:00 AM",
                location = "River Trail",
                distance = "5.4 mi",
                spotsTotal = 25,
                spotsTaken = 21,
                level = "Advanced",
                isSaved = true
            ),
            HomeActivityDto(
                id = "activity-5",
                title = "Volleyball at the Beach",
                sportType = "Volleyball",
                sportIcon = "🏐",
                hostName = "Lena Kim",
                hostAvatar = "https://i.pravatar.cc/400?img=12",
                date = "Sunday",
                time = "4:30 PM",
                location = "Sunny Beach",
                distance = "3.6 mi",
                spotsTotal = 12,
                spotsTaken = 7,
                level = "All Levels",
                isSaved = false
            )
        )

        val sportCategories = listOf(
            SportCategoryDto(id = "running", name = "Running", icon = "🏃"),
            SportCategoryDto(id = "yoga", name = "Yoga", icon = "🧘"),
            SportCategoryDto(id = "basketball", name = "Basketball", icon = "🏀"),
            SportCategoryDto(id = "cycling", name = "Cycling", icon = "🚴"),
            SportCategoryDto(id = "volleyball", name = "Volleyball", icon = "🏐"),
            SportCategoryDto(id = "swimming", name = "Swimming", icon = "🏊")
        )

        return HomeFeedDto(activities = activities, sportCategories = sportCategories)
    }
}
