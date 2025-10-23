package com.vivitasol.carcasamvvm.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pets
import androidx.compose.ui.graphics.vector.ImageVector

data class Product(
    val name: String,
    val description: String,
    val price: String,
    val icon: ImageVector
)

val sampleProducts = listOf(
    Product("Pelota Interactiva", "Mantiene a tu perro entretenido por horas.", "$9.990", Icons.Default.Pets),
    Product("Rascador para Gatos", "Protege tus muebles y entretiene a tu gato.", "$14.990", Icons.Default.Pets),
    Product("Hueso de Goma", "Juguete resistente para perros con mordida fuerte.", "$7.490", Icons.Default.Pets),
    Product("Varita con Plumas", "Estimula el instinto de caza de tu gato.", "$5.990", Icons.Default.Pets),
    Product("Comida para Cachorros", "Fórmula especial para el crecimiento saludable.", "$24.990", Icons.Default.Pets)
)