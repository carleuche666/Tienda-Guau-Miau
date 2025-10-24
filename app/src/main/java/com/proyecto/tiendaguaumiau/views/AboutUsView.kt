package com.vivitasol.tiendaguaumiau.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun AboutUsView() {
    // 1. Ubicación de la tienda (Ahora: Estadio Monumental)
    val storeLocation = LatLng(-33.5069, -70.6053)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(storeLocation, 17f) // Aumenté el zoom
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Sobre Nosotros",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Guau&Miau es una tienda apasionada por el bienestar de tus mascotas. Ofrecemos productos innovadores y sostenibles, seleccionados con amor y cuidado para asegurar la felicidad de tus fieles compañeros.",
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = "Encuéntranos cerca de ti:",
            style = MaterialTheme.typography.titleMedium
        )

        // 2. Componente de GoogleMap
        GoogleMap(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            cameraPositionState = cameraPositionState
        ) {
            // 3. Marcador en la nueva ubicación
            Marker(
                state = MarkerState(position = storeLocation),
                title = "Guau&Miau (Cerca del Estadio Monumental)",
                snippet = "¡Visítanos!"
            )
        }
    }
}