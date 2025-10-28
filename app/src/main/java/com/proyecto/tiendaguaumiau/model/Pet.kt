package com.vivitasol.tiendaguaumiau.model

data class Pet(
    val id: Int,
    val name: String = "",
    val type: String = "",
    val photoUri: String? = null
)