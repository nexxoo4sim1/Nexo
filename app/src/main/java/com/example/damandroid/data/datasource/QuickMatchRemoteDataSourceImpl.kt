package com.example.damandroid.data.datasource

import com.example.damandroid.api.LikeProfileRequest
import com.example.damandroid.api.LikeProfileResponse
import com.example.damandroid.api.PassProfileRequest
import com.example.damandroid.api.RetrofitClient
import com.example.damandroid.data.mapper.toMatchUserProfileDto
import com.example.damandroid.data.model.MatchUserProfileDto
import retrofit2.HttpException
import java.io.IOException

/**
 * Implémentation de la data source pour QuickMatch
 * 
 * Récupère les profils utilisateurs depuis l'API backend NestJS.
 * Le backend filtre automatiquement pour retourner uniquement les utilisateurs
 * qui ont au moins un sport/intérêt commun (sportsInterests) avec l'utilisateur connecté.
 */
class QuickMatchRemoteDataSourceImpl : QuickMatchRemoteDataSource {
    
    private val quickMatchApiService = RetrofitClient.quickMatchApiService

    override suspend fun fetchProfiles(): List<MatchUserProfileDto> {
        return try {
            // Récupérer la première page avec 20 résultats par défaut
            val response = quickMatchApiService.getProfiles(page = 1, limit = 20)
            
            if (response.isSuccessful) {
                // Extraire la liste des profils depuis la réponse paginée
                response.body()?.profiles?.map { it.toMatchUserProfileDto() } ?: emptyList()
            } else {
                when (response.code()) {
                    401 -> throw Exception("Unauthorized: Please login again")
                    403 -> throw Exception("Forbidden: Access denied")
                    404 -> throw Exception("No profiles found")
                    500 -> throw Exception("Server error: Please try again later")
                    else -> throw Exception("Failed to fetch profiles: ${response.code()}")
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

    override suspend fun likeProfile(profileId: String): LikeProfileResponse {
        return try {
            val response = quickMatchApiService.likeProfile(
                LikeProfileRequest(profileId = profileId)
            )
            
            if (response.isSuccessful) {
                response.body() ?: throw Exception("Empty response")
            } else {
                when (response.code()) {
                    401 -> throw Exception("Unauthorized: Please login again")
                    403 -> throw Exception("Forbidden: Access denied")
                    404 -> throw Exception("Profile not found")
                    409 -> throw Exception("Profile already liked or passed")
                    500 -> throw Exception("Server error: Please try again later")
                    else -> throw Exception("Failed to like profile: ${response.code()}")
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

    override suspend fun passProfile(profileId: String) {
        try {
            val response = quickMatchApiService.passProfile(
                PassProfileRequest(profileId = profileId)
            )
            
            if (!response.isSuccessful) {
                when (response.code()) {
                    401 -> throw Exception("Unauthorized: Please login again")
                    403 -> throw Exception("Forbidden: Access denied")
                    404 -> throw Exception("Profile not found")
                    409 -> throw Exception("Profile already passed or liked")
                    500 -> throw Exception("Server error: Please try again later")
                    else -> throw Exception("Failed to pass profile: ${response.code()}")
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
}

