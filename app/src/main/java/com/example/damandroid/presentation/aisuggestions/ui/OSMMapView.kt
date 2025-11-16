package com.example.damandroid.presentation.aisuggestions.ui

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.DriveEta
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import com.example.damandroid.domain.model.SuggestedActivity
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import org.osmdroid.util.BoundingBox
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.example.damandroid.location.RouteService
import com.example.damandroid.location.RouteEstimate

/**
 * Composable pour afficher une carte OpenStreetMap native (100% gratuit)
 * Utilise OSMDroid au lieu de WebView pour une meilleure performance
 */
@Composable
fun OSMMapView(
    activities: List<SuggestedActivity>,
    userLatitude: Double? = null,
    userLongitude: Double? = null,
    onActivityClick: ((SuggestedActivity) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedActivity by remember { mutableStateOf<SuggestedActivity?>(null) }
    var routePolyline by remember { mutableStateOf<Polyline?>(null) }
    var mapViewRef by remember { mutableStateOf<MapView?>(null) }
    
    // Configurer OSMDroid
    LaunchedEffect(Unit) {
        Configuration.getInstance().load(context, context.getSharedPreferences("osmdroid", android.content.Context.MODE_PRIVATE))
        Configuration.getInstance().userAgentValue = "DamAndroid/1.0"
    }
    
    AndroidView(
        factory = { ctx ->
            MapView(ctx).apply {
                mapViewRef = this
                setTileSource(TileSourceFactory.MAPNIK) // OpenStreetMap (100% gratuit)
                setMultiTouchControls(true)
                minZoomLevel = 3.0
                maxZoomLevel = 19.0
                
                // Position par défaut (Los Angeles) ou position de l'utilisateur
                val defaultLat = userLatitude ?: 34.0522
                val defaultLng = userLongitude ?: -118.2437
                val startPoint = GeoPoint(defaultLat, defaultLng)
                
                controller.setCenter(startPoint)
                controller.setZoom(13.0)
            }
        },
        modifier = modifier.fillMaxSize(),
        update = { mapView ->
            mapViewRef = mapView
        }
    )
    
    // Mettre à jour la carte de manière optimisée avec LaunchedEffect
    // Utiliser kotlinx.coroutines.Dispatchers.Default pour les opérations coûteuses
    LaunchedEffect(activities, userLatitude, userLongitude) {
        mapViewRef?.let { mapView ->
            // Utiliser Dispatchers.Default pour les opérations coûteuses
            withContext(Dispatchers.Default) {
                // Préparer les icônes en arrière-plan
                val userIcon = if (userLatitude != null && userLongitude != null) {
                    createUserLocationIcon(context)
                } else null
                
                val activityIcons = activities.associate { activity ->
                    val (lat, lng) = extractCoordinatesFromActivity(activity)
                    if (lat != null && lng != null) {
                        activity to createActivityIcon(context, activity.sport)
                    } else {
                        activity to null
                    }
                }
                
                // Retourner sur le thread principal pour les opérations UI
                withContext(Dispatchers.Main) {
                    // Supprimer uniquement les marqueurs et overlays de localisation, garder la route
                    val overlaysToRemove = mapView.overlays.filter { 
                        it !is Polyline && it !is MyLocationNewOverlay
                    }
                    overlaysToRemove.forEach { mapView.overlays.remove(it) }
                    
                    // Supprimer aussi MyLocationNewOverlay s'il existe déjà
                    val existingLocationOverlay = mapView.overlays.find { it is MyLocationNewOverlay }
                    if (existingLocationOverlay != null) {
                        mapView.overlays.remove(existingLocationOverlay)
                    }
                    
                    val defaultLat = userLatitude ?: 34.0522
                    val defaultLng = userLongitude ?: -118.2437
                    val startPoint = GeoPoint(defaultLat, defaultLng)
                    mapView.controller.setCenter(startPoint)
                    
                    // Ajouter la localisation de l'utilisateur
                    if (userLatitude != null && userLongitude != null && userIcon != null) {
                        val myLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(context), mapView)
                        myLocationOverlay.enableMyLocation()
                        mapView.overlays.add(myLocationOverlay)
                        
                        val userMarker = Marker(mapView).apply {
                            position = GeoPoint(userLatitude, userLongitude)
                            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                            title = "Votre position"
                            icon = userIcon
                        }
                        mapView.overlays.add(userMarker)
                    }
                    
                    // Ajouter les marqueurs pour les activités
                    activities.forEach { activity ->
                        val (lat, lng) = extractCoordinatesFromActivity(activity)
                        val icon = activityIcons[activity]
                        if (lat != null && lng != null && icon != null) {
                            val marker = Marker(mapView).apply {
                                position = GeoPoint(lat, lng)
                                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                                title = activity.title
                                snippet = "${activity.sport} • ${activity.location.split(",").firstOrNull() ?: activity.location}"
                                this.icon = icon
                                
                                setOnMarkerClickListener { _, _ ->
                                    selectedActivity = activity
                                    onActivityClick?.invoke(activity)
                                    true
                                }
                            }
                            mapView.overlays.add(marker)
                        }
                    }
                    
                    mapView.invalidate()
                }
            }
        }
    }
    
    DisposableEffect(Unit) {
        onDispose {
            // Nettoyer les ressources si nécessaire
        }
    }
    
    // Dialog pour afficher les informations de l'activité
    selectedActivity?.let { activity ->
        ActivityInfoDialog(
            activity = activity,
            userLatitude = userLatitude,
            userLongitude = userLongitude,
            onDismiss = { 
                selectedActivity = null
                // Supprimer la route quand on ferme le dialog
                routePolyline?.let { polyline ->
                    mapViewRef?.overlays?.remove(polyline)
                    mapViewRef?.invalidate()
                }
                routePolyline = null
            },
            onNavigate = { lat, lng ->
                routePolyline = drawRouteOnMap(mapViewRef, userLatitude, userLongitude, lat, lng)
                // Fermer le dialog après avoir tracé la route
                selectedActivity = null
            }
        )
    }
}

/**
 * Dialog pour afficher les informations d'une activité avec un bouton de navigation
 */
@Composable
private fun ActivityInfoDialog(
    activity: SuggestedActivity,
    userLatitude: Double?,
    userLongitude: Double?,
    onDismiss: () -> Unit,
    onNavigate: (Double, Double) -> Unit
) {
    val (activityLat, activityLng) = extractCoordinatesFromActivity(activity)
    val canNavigate = userLatitude != null && userLongitude != null && activityLat != null && activityLng != null
    
    // État pour l'estimation de route
    var routeEstimate by remember { mutableStateOf<RouteEstimate?>(null) }
    var isLoadingEstimate by remember { mutableStateOf(false) }
    
    // Charger l'estimation de route si les coordonnées sont disponibles
    LaunchedEffect(userLatitude, userLongitude, activityLat, activityLng) {
        if (canNavigate && activityLat != null && activityLng != null) {
            isLoadingEstimate = true
            routeEstimate = RouteService.getRouteEstimate(
                startLat = userLatitude!!,
                startLng = userLongitude!!,
                endLat = activityLat,
                endLng = activityLng
            )
            isLoadingEstimate = false
        }
    }
    
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = MaterialTheme.shapes.large,
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // En-tête avec titre et bouton de fermeture
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = activity.title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Fermer")
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Sport
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Text(
                        text = getSportEmoji(activity.sport),
                        fontSize = 20.sp,
                        modifier = Modifier.width(32.dp)
                    )
                    Text(
                        text = activity.sport,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Description
                if (activity.description.isNotEmpty()) {
                    Text(
                        text = activity.description,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                
                // Date et heure
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Icon(
                        Icons.Default.Schedule,
                        contentDescription = null,
                        modifier = Modifier.width(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${formatDate(activity.date)} à ${formatTime(activity.time)}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Localisation
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        modifier = Modifier.width(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = activity.location.split(",").firstOrNull() ?: activity.location,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f)
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Organisateur
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.width(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Organisé par ${activity.organizer}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Participants
                Text(
                    text = "${activity.participantsCount}/${activity.capacity} participants",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Estimation de route en voiture
                if (canNavigate) {
                    if (isLoadingEstimate) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            Icon(
                                Icons.Default.DriveEta,
                                contentDescription = null,
                                modifier = Modifier.width(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Calcul de l'itinéraire...",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    } else {
                        routeEstimate?.let { estimate ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(vertical = 4.dp)
                            ) {
                                Icon(
                                    Icons.Default.DriveEta,
                                    contentDescription = null,
                                    modifier = Modifier.width(24.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "${estimate.durationMinutes} min • ${String.format("%.1f", estimate.distanceKm)} km",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
                
                // Bouton de navigation
                if (canNavigate && activityLat != null && activityLng != null) {
                    Button(
                        onClick = { onNavigate(activityLat, activityLng) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(
                            Icons.Default.Directions,
                            contentDescription = null,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text("Y aller")
                    }
                } else {
                    Text(
                        text = "Navigation non disponible",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
        }
    }
}

/**
 * Trace une route sur la carte entre la position de l'utilisateur et la destination
 */
private fun drawRouteOnMap(
    mapView: MapView?,
    userLat: Double?,
    userLng: Double?,
    destinationLat: Double,
    destinationLng: Double
): Polyline? {
    if (mapView == null || userLat == null || userLng == null) {
        return null
    }
    
    // Supprimer toutes les anciennes routes (Polylines) pour éviter les doublons
    val existingRoutes = mapView.overlays.filter { it is Polyline }
    existingRoutes.forEach { mapView.overlays.remove(it) }
    
    // Créer une nouvelle polyline simple pour la route (ligne droite)
    val route = Polyline().apply {
        setPoints(
            listOf(
                GeoPoint(userLat, userLng),
                GeoPoint(destinationLat, destinationLng)
            )
        )
        color = android.graphics.Color.parseColor("#4CAF50") // Vert
        width = 10f
        outlinePaint.strokeWidth = 12f
        outlinePaint.color = android.graphics.Color.parseColor("#2E7D32") // Vert foncé pour le contour
    }
    
    // Ajouter la route au début des overlays pour qu'elle soit en dessous des marqueurs
    mapView.overlays.add(0, route)
    
    // Centrer la carte pour montrer les deux points
    val userPoint = GeoPoint(userLat, userLng)
    val destPoint = GeoPoint(destinationLat, destinationLng)
    
    // Calculer les limites pour inclure les deux points
    val minLat = minOf(userLat, destinationLat)
    val maxLat = maxOf(userLat, destinationLat)
    val minLng = minOf(userLng, destinationLng)
    val maxLng = maxOf(userLng, destinationLng)
    
    // Créer une bounding box avec un peu de padding
    val padding = 0.01 // ~1km de padding
    val boundingBox = BoundingBox(
        maxLat + padding,
        maxLng + padding,
        minLat - padding,
        minLng - padding
    )
    
    // Zoomer pour montrer la route avec un peu de padding
    mapView.zoomToBoundingBox(boundingBox, true, 100)
    
    mapView.invalidate()
    
    return route
}

/**
 * Formate une date pour l'affichage
 */
private fun formatDate(dateString: String): String {
    return try {
        val inputFormat = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", java.util.Locale.getDefault())
        val outputFormat = java.text.SimpleDateFormat("dd MMM yyyy", java.util.Locale.getDefault())
        val date = inputFormat.parse(dateString)
        if (date != null) {
            outputFormat.format(date)
        } else {
            dateString
        }
    } catch (e: Exception) {
        dateString
    }
}

/**
 * Formate une heure pour l'affichage
 */
private fun formatTime(timeString: String): String {
    return try {
        val inputFormat = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", java.util.Locale.getDefault())
        val outputFormat = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault())
        val date = inputFormat.parse(timeString)
        if (date != null) {
            outputFormat.format(date)
        } else {
            timeString
        }
    } catch (e: Exception) {
        timeString
    }
}

/**
 * Extrait les coordonnées depuis une activité
 */
private fun extractCoordinatesFromActivity(activity: SuggestedActivity): Pair<Double?, Double?> {
    // Si la location contient des coordonnées (format: "Location, lat, lng")
    val parts = activity.location.split(",")
    if (parts.size >= 3) {
        try {
            val lat = parts[parts.size - 2].trim().toDoubleOrNull()
            val lng = parts[parts.size - 1].trim().toDoubleOrNull()
            if (lat != null && lng != null) {
                return Pair(lat, lng)
            }
        } catch (e: Exception) {
            // Ignorer
        }
    }
    return Pair(null, null)
}

/**
 * Crée une icône pour la position de l'utilisateur
 */
private fun createUserLocationIcon(context: android.content.Context): android.graphics.drawable.Drawable {
    val size = 40
    val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    
    // Cercle vert pour la position de l'utilisateur
    val paint = android.graphics.Paint().apply {
        color = android.graphics.Color.parseColor("#4CAF50")
        style = android.graphics.Paint.Style.FILL
        isAntiAlias = true
    }
    val borderPaint = android.graphics.Paint().apply {
        color = android.graphics.Color.WHITE
        style = android.graphics.Paint.Style.STROKE
        strokeWidth = 4f
        isAntiAlias = true
    }
    
    val centerX = size / 2f
    val centerY = size / 2f
    val radius = size / 2f - 4f
    
    canvas.drawCircle(centerX, centerY, radius, paint)
    canvas.drawCircle(centerX, centerY, radius, borderPaint)
    
    return BitmapDrawable(context.resources, bitmap)
}

/**
 * Crée une icône pour une activité selon le sport
 */
private fun createActivityIcon(context: android.content.Context, sport: String): android.graphics.drawable.Drawable {
    val size = 50
    val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    
    // Cercle violet pour les activités
    val paint = android.graphics.Paint().apply {
        color = android.graphics.Color.parseColor("#A855F7")
        style = android.graphics.Paint.Style.FILL
        isAntiAlias = true
    }
    val borderPaint = android.graphics.Paint().apply {
        color = android.graphics.Color.WHITE
        style = android.graphics.Paint.Style.STROKE
        strokeWidth = 3f
        isAntiAlias = true
    }
    
    val centerX = size / 2f
    val centerY = size / 2f
    val radius = size / 2f - 3f
    
    canvas.drawCircle(centerX, centerY, radius, paint)
    canvas.drawCircle(centerX, centerY, radius, borderPaint)
    
    // Ajouter un emoji ou une icône selon le sport (optionnel)
    val textPaint = android.graphics.Paint().apply {
        color = android.graphics.Color.WHITE
        textSize = size * 0.4f
        textAlign = android.graphics.Paint.Align.CENTER
        isAntiAlias = true
    }
    
    val sportEmoji = getSportEmoji(sport)
    val textY = centerY + (textPaint.descent() + textPaint.ascent()) / 2
    canvas.drawText(sportEmoji, centerX, textY, textPaint)
    
    return BitmapDrawable(context.resources, bitmap)
}

/**
 * Retourne un emoji selon le sport
 */
private fun getSportEmoji(sport: String): String {
    val sportLower = sport.lowercase()
    return when {
        sportLower.contains("run") -> "🏃"
        sportLower.contains("yoga") -> "🧘"
        sportLower.contains("volley") -> "🏐"
        sportLower.contains("basket") -> "🏀"
        sportLower.contains("swim") -> "🏊"
        sportLower.contains("cycle") -> "🚴"
        sportLower.contains("tennis") -> "🎾"
        sportLower.contains("football") || sportLower.contains("soccer") -> "⚽"
        else -> "🎯"
    }
}

