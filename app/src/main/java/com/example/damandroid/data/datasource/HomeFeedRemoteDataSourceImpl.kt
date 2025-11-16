package com.example.damandroid.data.datasource

import com.example.damandroid.api.RetrofitClient
import com.example.damandroid.data.mapper.toHomeActivityDto
import com.example.damandroid.data.model.HomeFeedDto
import com.example.damandroid.data.model.SportCategoryDto
import retrofit2.HttpException
import java.io.IOException

class HomeFeedRemoteDataSourceImpl : HomeFeedRemoteDataSource {

    private val activityApiService = RetrofitClient.activityApiService

    override suspend fun fetchHomeFeed(): HomeFeedDto {
        return try {
            // Récupérer les activités publiques (pas d'authentification requise)
            val response = activityApiService.getActivities(visibility = "public")
            
            if (response.isSuccessful) {
                val activities = response.body()?.map { it.toHomeActivityDto() } ?: emptyList()
                
                // Récupérer les catégories de sport (pour l'instant, on utilise des valeurs hardcodées)
                val sportCategories = getSportCategories()
                
                HomeFeedDto(
                    activities = activities,
                    sportCategories = sportCategories
                )
            } else {
                when (response.code()) {
                    401 -> throw Exception("Unauthorized: Please login again")
                    403 -> throw Exception("Forbidden: Access denied")
                    404 -> throw Exception("Activities not found")
                    500 -> throw Exception("Server error: Please try again later")
                    else -> throw Exception("Failed to fetch activities: ${response.code()}")
                }
            }
        } catch (e: HttpException) {
            throw Exception("Network error: ${e.message}")
        } catch (e: IOException) {
            throw Exception("Connection error: Please check your internet connection")
        } catch (e: Exception) {
            throw e
        }
    }
    
    override suspend fun fetchMyActivities(): HomeFeedDto {
        return try {
            // Récupérer les activités créées par l'utilisateur connecté
            val response = activityApiService.getMyActivities()
            
            if (response.isSuccessful) {
                val activities = response.body()?.map { it.toHomeActivityDto() } ?: emptyList()
                
                // Récupérer les catégories de sport (pour l'instant, on utilise des valeurs hardcodées)
                val sportCategories = getSportCategories()
                
                HomeFeedDto(
                    activities = activities,
                    sportCategories = sportCategories
                )
            } else {
                when (response.code()) {
                    401 -> throw Exception("Unauthorized: Please login again")
                    403 -> throw Exception("Forbidden: Access denied")
                    404 -> throw Exception("No activities found")
                    500 -> throw Exception("Server error: Please try again later")
                    else -> throw Exception("Failed to fetch my activities: ${response.code()}")
                }
            }
        } catch (e: HttpException) {
            throw Exception("Network error: ${e.message}")
        } catch (e: IOException) {
            throw Exception("Connection error: Please check your internet connection")
        } catch (e: Exception) {
            throw e
        }
    }
    
    private fun getSportCategories(): List<SportCategoryDto> {
        return listOf(
            SportCategoryDto(id = "football", name = "Football", icon = "⚽"),
            SportCategoryDto(id = "basketball", name = "Basketball", icon = "🏀"),
            SportCategoryDto(id = "running", name = "Running", icon = "🏃"),
            SportCategoryDto(id = "cycling", name = "Cycling", icon = "🚴"),
            SportCategoryDto(id = "tennis", name = "Tennis", icon = "🎾"),
            SportCategoryDto(id = "swimming", name = "Swimming", icon = "🏊"),
            SportCategoryDto(id = "yoga", name = "Yoga", icon = "🧘"),
            SportCategoryDto(id = "volleyball", name = "Volleyball", icon = "🏐")
        )
    }
}
