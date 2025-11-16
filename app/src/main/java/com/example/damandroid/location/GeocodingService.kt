package com.example.damandroid.location

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URLEncoder

/**
 * Service de géocodage utilisant OpenStreetMap Nominatim (100% gratuit)
 * Convertit les adresses en coordonnées latitude/longitude
 */
class GeocodingService {
    
    /**
     * Convertit une adresse en coordonnées latitude/longitude
     * @param address L'adresse à géocoder (ex: "123 Main St, New York, NY")
     * @return Pair<latitude, longitude> ou null si l'adresse n'a pas pu être géocodée
     */
    suspend fun geocode(address: String): Pair<Double, Double>? = withContext(Dispatchers.IO) {
        try {
            // Encoder l'adresse pour l'URL
            val encodedAddress = URLEncoder.encode(address, "UTF-8")
            
            // Appeler l'API Nominatim d'OpenStreetMap (100% gratuit)
            val url = "https://nominatim.openstreetmap.org/search?q=$encodedAddress&format=json&limit=1"
            
            val connection = java.net.URL(url).openConnection()
            connection.setRequestProperty("User-Agent", "DamAndroidApp/1.0")
            connection.connectTimeout = 5000
            connection.readTimeout = 5000
            
            val response = connection.getInputStream().bufferedReader().use { it.readText() }
            
            // Parser la réponse JSON
            val jsonArray = org.json.JSONArray(response)
            if (jsonArray.length() > 0) {
                val firstResult = jsonArray.getJSONObject(0)
                val lat = firstResult.getDouble("lat")
                val lon = firstResult.getDouble("lon")
                return@withContext Pair(lat, lon)
            }
        } catch (e: Exception) {
            android.util.Log.e("GeocodingService", "Error geocoding address '$address': ${e.message}")
        }
        
        null
    }
    
    /**
     * Géocode plusieurs adresses en parallèle
     */
    suspend fun geocodeBatch(addresses: List<String>): Map<String, Pair<Double, Double>> = withContext(Dispatchers.IO) {
        addresses.mapNotNull { address ->
            geocode(address)?.let { coordinates ->
                address to coordinates
            }
        }.toMap()
    }
    
    /**
     * Calcule la distance entre deux points en kilomètres (formule de Haversine)
     */
    fun calculateDistance(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double
    ): Double {
        val R = 6371.0 // Rayon de la Terre en kilomètres
        
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        
        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(dLon / 2) * Math.sin(dLon / 2)
        
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        
        return R * c
    }
}

