package com.vivitasol.tiendaguaumiau.model

import androidx.annotation.DrawableRes
import com.vivitasol.tiendaguaumiau.R

data class Product(
    val name: String,
    val description: String,
    val price: String,
    @DrawableRes val imageRes: Int
)

val sampleProducts = listOf(
    Product("Pelota Interactiva", "Mantiene a tu perro entretenido por horas.", "$9.990", R.drawable.pelota_interactiva),
    Product("Rascador para Gatos", "Protege tus muebles y entretiene a tu gato.", "$14.990", R.drawable.rascador_gato),
    Product("Hueso de Goma", "Juguete resistente para perros con mordida fuerte.", "$7.490", R.drawable.hueso_goma),
    Product("Varita con Plumas", "Estimula el instinto de caza de tu gato.", "$5.990", R.drawable.varita_gato),
    Product("Comida para Cachorros", "Fórmula especial para el crecimiento saludable.", "$24.990", R.drawable.comida_cachorro)
)
