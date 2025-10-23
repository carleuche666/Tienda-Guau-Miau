package com.vivitasol.carcasamvvm.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Pantalla de Detalle para la navegación con argumento
 */
@Composable
fun Option2DetailView(
    id: String,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Detalle", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text("ID recibido en la ruta: $id", style = MaterialTheme.typography.bodyLarge)
        Button(onClick = onBack) { Text("Volver") }
    }
}