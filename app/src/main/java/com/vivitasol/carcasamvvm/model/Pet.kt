package com.vivitasol.carcasamvvm.model

data class Pet(
    val id: Int,
    val name: String = "",
    val type: String = "",
    val photoUri: String? = null // <-- Campo añadido
)